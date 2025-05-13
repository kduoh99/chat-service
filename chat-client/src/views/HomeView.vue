<template>
  <v-container class="mt-5">
    <v-row justify="center" align="start">
      <v-col cols="12" sm="10" md="8" lg="6">
        <v-card>
          <v-card-title class="text-h5">💬 실시간 채팅 서비스</v-card-title>
          <v-card-text>
            <p class="mb-4">
              {{ isLogin ? '채팅방에 입장하여 대화를 시작해보세요.' : '로그인 후 서비스를 이용할 수 있습니다.' }}
            </p>
            <div class="d-flex justify-end">
              <v-btn color="primary" :to="{ path: isLogin ? '/my-chat-rooms' : '/login' }">
                {{ isLogin ? '내 채팅방 보기' : '로그인하러 가기' }}
              </v-btn>
            </div>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'HomeView',
    computed: {
      isLogin() {
        return !!localStorage.getItem('accessToken');
      },
    },
  };
</script>
