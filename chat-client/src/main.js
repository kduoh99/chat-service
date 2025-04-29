import { createApp } from 'vue';
import App from './App.vue';
import vutify from './plugins/vuetify';
import router from '@/router/index.js';
import axios from 'axios';

const app = createApp(App);

axios.interceptors.request.use(
  (config) => {
    const accessToken = localStorage.getItem('accessToken');
    if (accessToken) {
      config.headers['Authorization'] = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

app.use(vutify);
app.use(router);
app.mount('#app');
