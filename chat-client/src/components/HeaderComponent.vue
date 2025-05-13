<template>
  <v-app-bar app dark>
    <v-container>
      <v-row align="center" class="flex-wrap">
        <!-- 왼쪽 버튼 그룹 -->
        <v-col cols="12" sm="6" md="4" class="d-flex justify-start">
          <v-btn class="mx-1" :to="{ path: '/members' }">회원목록</v-btn>
          <v-btn class="mx-1" :to="{ path: '/group-chat-rooms' }">채팅방목록</v-btn>
        </v-col>

        <!-- 중앙 로고 -->
        <v-col cols="12" sm="12" md="4" class="d-flex justify-center text-center">
          <v-btn class="mx-1" :to="{ path: '/' }">chat서비스</v-btn>
        </v-col>

        <!-- 오른쪽 버튼 그룹 -->
        <v-col cols="12" sm="6" md="4" class="d-flex justify-end flex-wrap">
          <v-btn class="mx-1" v-if="isLogin" :to="{ path: '/my-chat-rooms' }">MyChatPage</v-btn>
          <v-btn class="mx-1" v-if="!isLogin" :to="{ path: '/signup' }">회원가입</v-btn>
          <v-btn class="mx-1" v-if="!isLogin" :to="{ path: '/login' }">로그인</v-btn>
          <v-btn class="mx-1" v-if="isLogin" @click="logout">로그아웃</v-btn>
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
        window.location.href = '/';
      },
    },
  };
</script>
