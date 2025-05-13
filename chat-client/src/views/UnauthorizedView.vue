<template>
  <v-container class="mt-5">
    <v-row justify="center" align="start">
      <v-col cols="12" sm="10" md="8" lg="6">
        <v-card>
          <v-card-title class="text-h5 text-center"> ⚠️ 인증이 필요한 페이지입니다. </v-card-title>
          <v-card-actions class="justify-center my-4">
            <v-btn color="primary" @click="$router.push('/login')"> 로그인 페이지로 이동 </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  export default {
    name: 'UnauthorizedView',
  };
</script>
