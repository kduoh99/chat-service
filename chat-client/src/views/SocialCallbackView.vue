<template>
  <v-container class="mt-8">
    <v-row justify="center">
      <v-col cols="12" class="text-center">
        <div class="text-h6 font-weight-medium">{{ provider.toUpperCase() }} 로그인 진행중...</div>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
  import axios from 'axios';
  import { jwtDecode } from 'jwt-decode';

  export default {
    created() {
      const code = new URL(window.location.href).searchParams.get('code');
      const provider = this.$route.params.provider.toUpperCase();
      this.sendCode(provider, code);
    },
    methods: {
      async sendCode(provider, code) {
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/api/auth/callback/${provider}?code=${code}`,
        );
        const accessToken = response.data.accessToken;
        const decoded = jwtDecode(accessToken);
        const email = decoded.sub;
        const role = decoded.role;

        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('email', email);
        localStorage.setItem('role', role);
        window.location.href = '/';
      },
    },
    computed: {
      provider() {
        return this.$route.params.provider;
      },
    },
  };
</script>
