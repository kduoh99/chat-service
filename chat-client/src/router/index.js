import { createRouter, createWebHashHistory } from 'vue-router';
import SignupView from '@/views/SignupView.vue';
import LoginView from '@/views/LoginView.vue';
import MembersView from '@/views/MembersView.vue';
import WebSocketView from '@/views/WebSocketView.vue';
import StompView from '@/views/StompView.vue';

const routes = [
  {
    path: '/signup',
    name: 'Signup',
    component: SignupView,
  },
  {
    path: '/login',
    name: 'Login',
    component: LoginView,
  },
  {
    path: '/members',
    name: 'Members',
    component: MembersView,
  },
  {
    path: '/websocket',
    name: 'WebSocket',
    component: WebSocketView,
  },
  {
    path: '/stomp',
    name: 'Stomp',
    component: StompView,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
