<template>
  <v-container>
    <v-row>
      <v-col>
        <v-card>
          <v-card-title class="text-h5 text-center">회원목록</v-card-title>
          <v-card-text>
            <v-table>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>이름</th>
                  <th>이메일</th>
                  <th>채팅</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="member in members" :key="member.memberId">
                  <td>{{ member.memberId }}</td>
                  <td>{{ member.name }}</td>
                  <td>{{ member.email }}</td>
                  <td>
                    <v-btn color="primary" @click="startChat(member.memberId)">채팅하기</v-btn>
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
        members: [],
      };
    },
    async created() {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/member/list`);
      this.members = response.data;
    },
    methods: {
      async startChat(otherMemberId) {
        // 기존 채팅방이 존재하면 return받고, 없으면 새롭게 생성된 roomId return
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/api/chat/private/room?otherMemberId=${otherMemberId}`,
        );
        const roomId = response.data;
        this.$router.push(`/chat/${roomId}`);
      },
    },
  };
</script>
