<template>
  <v-app-bar app dark>
    <v-container>
      <v-row align="center">
        <v-col class="d-flex justify-start">
          <v-btn :to="{ path: '/members' }">회원목록</v-btn>
          <v-btn :to="{ path: '/groupchatting/list' }">채팅방목록</v-btn>
        </v-col>
        <v-col class="text-center">
          <v-btn :to="{ path: '/' }">chat서비스</v-btn>
        </v-col>
        <v-col class="d-flex justify-end">
          <v-btn v-if="isLogin" :to="{ path: '/my/chat/page' }">MyChatPage</v-btn>
          <v-btn v-if="!isLogin" :to="{ path: '/signup' }">회원가입</v-btn>
          <v-btn v-if="!isLogin" :to="{ path: '/login' }">로그인</v-btn>
          <v-btn v-if="isLogin" @click="logout">로그아웃</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </v-app-bar>
</template>
<script>
  export default {
    data() {
      return {
        isLogin: false,
      };
    },
    created() {
      const accessToken = localStorage.getItem('accessToken');
      if (accessToken) {
        this.isLogin = true;
      }
    },
    methods: {
      logout() {
        localStorage.clear();
        window.location.reload();
      },
    },
  };
</script>
