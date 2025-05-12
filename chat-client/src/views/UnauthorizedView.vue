<template>
  <v-container class="d-flex flex-column justify-center align-center text-center" style="height: 80vh">
    <h2>⚠️ 인증이 필요한 페이지입니다.</h2>
    <v-btn color="primary" class="mt-4" @click="$router.push('/login')"> 로그인 페이지로 이동 </v-btn>
  </v-container>
</template>

<script>
  export default {
    name: 'UnauthorizedView',
  };
</script>
