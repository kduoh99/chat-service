<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="4" md="6">
        <v-card>
          <v-card-title class="text-h5 text-center">로그인</v-card-title>
          <v-card-text>
            <v-form @submit.prevent="login">
              <v-text-field label="이메일" v-model="email" type="email" required></v-text-field>
              <v-text-field label="비밀번호" v-model="password" type="password" required></v-text-field>
              <v-btn type="submit" color="primary" block>로그인</v-btn>
            </v-form>
            <br />
            <v-row>
              <v-col cols="6" class="d-flex justify-center">
                <img
                  src="@/assets/google_login.png"
                  style="max-height: 40px; width: 100%; cursor: pointer"
                  @click="googleLogin()"
                />
              </v-col>
              <v-col cols="6" class="d-flex justify-center">
                <img
                  src="@/assets/kakao_login.png"
                  style="max-height: 40px; width: 100%; cursor: pointer"
                  @click="kakaoLogin()"
                />
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import axios from 'axios';
  import { jwtDecode } from 'jwt-decode';

  export default {
    data() {
      return {
        email: '',
        password: '',
        googleUrl: 'https://accounts.google.com/o/oauth2/auth',
        googleClientId: process.env.VUE_APP_GOOGLE_CLIENT_ID,
        googleRedirectUrl: process.env.VUE_APP_GOOGLE_REDIRECT_URL,
        googleScope: 'openid email profile',
        kakaoUrl: 'https://kauth.kakao.com/oauth/authorize',
        kakaoClientId: process.env.VUE_APP_KAKAO_CLIENT_ID,
        kakaoRedirectUrl: process.env.VUE_APP_KAKAO_REDIRECT_URL,
      };
    },
    methods: {
      async login() {
        const data = {
          email: this.email,
          password: this.password,
        };
        const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/api/auth/login`, data);
        console.log(response);
        const accessToken = response.data.accessToken;
        const decoded = jwtDecode(accessToken);
        const email = decoded.sub;
        const role = decoded.role;

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('email', email);
        localStorage.setItem('role', role);
        window.location.href = '/';
      },
      googleLogin() {
        const auth_uri = `${this.googleUrl}?client_id=${this.googleClientId}&redirect_uri=${this.googleRedirectUrl}&response_type=code&scope=${this.googleScope}`;
        window.location.href = auth_uri;
      },
      kakaoLogin() {
        const auth_uri = `${this.kakaoUrl}?client_id=${this.kakaoClientId}&redirect_uri=${this.kakaoRedirectUrl}&response_type=code`;
        window.location.href = auth_uri;
      },
    },
  };
</script>
