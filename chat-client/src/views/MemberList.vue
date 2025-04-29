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
                <tr v-for="member in memberList" :key="member.memberId">
                  <td>{{ member.memberId }}</td>
                  <td>{{ member.name }}</td>
                  <td>{{ member.email }}</td>
                  <td>
                    <v-btn>채팅하기</v-btn>
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
        memberList: [],
      };
    },
    async created() {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/api/member/list`);
      this.memberList = response.data;
    },
  };
</script>
