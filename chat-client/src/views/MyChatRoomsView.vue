<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title class="text-h5 text-center">내 채팅 목록</v-card-title>
          <v-card-text>
            <v-table>
              <thead>
                <tr>
                  <th>채팅방 이름</th>
                  <th>읽지 않은 메시지</th>
                  <th>액션</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="chat in chats" :key="chat.roomId">
                  <td>{{ chat.roomName }}</td>
                  <td>{{ chat.unReadCount }}</td>
                  <td>
                    <v-btn color="primary" @click="enterChatRoom(chat.roomId)">입장</v-btn>
                    <v-btn color="secondary" :disabled="chat.isGroupChat === 'N'" @click="leaveChatRoom(chat.roomId)"
                      >나가기</v-btn
                    >
                  </td>
                </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import axios from 'axios';

  export default {
    data() {
      return {
        chats: [],
      };
    },
    async created() {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/chat/my/rooms`);
      this.chats = response.data;
    },
    methods: {
      enterChatRoom(roomId) {
        this.$router.push(`/chat/${roomId}`);
      },
      async leaveChatRoom(roomId) {
        await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/api/chat/group/room/${roomId}/leave`);
        this.chats = this.chats.filter((chat) => chat.roomId !== roomId);
      },
    },
  };
</script>
