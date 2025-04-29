<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="4" md="6">
        <v-card>
          <v-card-title class="text-h5 text-center">회원가입</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="signup">
              <v-text-field label="이름" v-model="name" required></v-text-field>
              <v-text-field label="이메일" v-model="email" type="email" required></v-text-field>
              <v-text-field label="비밀번호" v-model="password" type="password" required></v-text-field>
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
