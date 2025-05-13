<template>
  <v-container class="mt-5">
    <v-row justify="center" align="start">
      <v-col cols="12" sm="10" md="8" lg="6">
        <v-card>
          <v-card-title class="text-h5 text-center">내 채팅 목록</v-card-title>
          <v-card-text class="px-4 py-0" style="overflow-x: auto">
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
                    <v-btn color="primary" class="my-2 mr-1" @click="enterChatRoom(chat.roomId)">입장</v-btn>
                    <v-btn
                      color="secondary"
                      class="my-2"
                      :disabled="chat.isGroupChat === 'N'"
                      @click="leaveChatRoom(chat.roomId)"
                    >
                      나가기
                    </v-btn>
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
  import { EventSourcePolyfill } from 'event-source-polyfill';

  export default {
    data() {
      return {
        chats: [],
        eventSource: null,
      };
    },
    async created() {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/chat/my/rooms`);
      this.chats = response.data;
      this.connectSse();
    },
    beforeUnmount() {
      if (this.eventSource && this.eventSource.readyState !== 2) {
        this.eventSource.close();
      }
    },
    methods: {
      enterChatRoom(roomId) {
        this.$router.push(`/chat/${roomId}`);
      },
      async leaveChatRoom(roomId) {
        await axios.delete(`${process.env.VUE_APP_API_BASE_URL}/api/chat/group/room/${roomId}/leave`);
        this.chats = this.chats.filter((chat) => chat.roomId !== roomId);
      },
      connectSse() {
        const accessToken = localStorage.getItem('accessToken');
        const sseURL = `${process.env.VUE_APP_API_BASE_URL}/api/notification/subscribe`;

        this.eventSource = new EventSourcePolyfill(sseURL, {
          headers: { Authorization: `Bearer ${accessToken}` },
        });

        this.eventSource.addEventListener('connect', (event) => {
          console.info('SSE 연결 성공:', event.data);
        });

        this.eventSource.addEventListener('unread-message', (event) => {
          const updates = JSON.parse(event.data);
          updates.forEach((update) => {
            const chat = this.chats.find((c) => c.roomId === update.roomId);
            if (chat) {
              chat.unReadCount = update.count;
            }
          });
        });

        this.eventSource.onerror = (e) => {
          console.error('SSE 연결 오류:', e);
          this.eventSource.close();
        };
      },
    },
  };
</script>
