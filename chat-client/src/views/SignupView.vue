<template>
  <v-container class="mt-5">
    <v-row justify="center" align="start">
      <v-col cols="12" sm="8" md="6" lg="4">
        <v-card>
          <v-card-title class="text-h5 text-center">회원가입</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="signup">
              <v-text-field label="이름" v-model="name" required outlined class="mb-3" />
              <v-text-field label="이메일" v-model="email" type="email" required outlined class="mb-3" />
              <v-text-field label="비밀번호" v-model="password" type="password" required outlined class="mb-4" />
              <v-btn type="submit" color="primary" block>회원가입</v-btn>
            </v-form>
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
        name: '',
        email: '',
        password: '',
      };
    },
    methods: {
      async signup() {
        const data = {
          name: this.name,
          email: this.email,
          password: this.password,
        };
        await axios.post(`${process.env.VUE_APP_API_BASE_URL}/api/member/save`, data);
        this.$router.push('/');
      },
    },
  };
</script>
