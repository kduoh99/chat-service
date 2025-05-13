import { createRouter, createWebHistory } from 'vue-router';
import SignupView from '@/views/SignupView.vue';
import LoginView from '@/views/LoginView.vue';
import SocialCallbackView from '@/views/SocialCallbackView.vue';
import MembersView from '@/views/MembersView.vue';
import WebSocketView from '@/views/WebSocketView.vue';
import StompChatView from '@/views/StompChatView.vue';
import GroupChatRoomsView from '@/views/GroupChatRoomsView.vue';
import MyChatRoomsView from '@/views/MyChatRoomsView.vue';
import UnauthorizedView from '@/views/UnauthorizedView.vue';
import HomeView from '@/views/HomeView.vue';

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
    path: '/oauth/callback/:provider',
    name: 'SocialCallback',
    component: SocialCallbackView,
  },
  {
    path: '/members',
    name: 'Members',
    component: MembersView,
    meta: { requiresAuth: true },
  },
  {
    path: '/websocket',
    name: 'WebSocket',
    component: WebSocketView,
    meta: { requiresAuth: true },
  },
  {
    path: '/chat/:roomId',
    name: 'StompChat',
    component: StompChatView,
    meta: { requiresAuth: true },
  },
  {
    path: '/group-chat-rooms',
    name: 'GroupChatRooms',
    component: GroupChatRoomsView,
    meta: { requiresAuth: true },
  },
  {
    path: '/my-chat-rooms',
    name: 'MyChatRooms',
    component: MyChatRoomsView,
    meta: { requiresAuth: true },
  },
  {
    path: '/unauthorized',
    name: 'Unauthorized',
    component: UnauthorizedView,
  },
  {
    path: '/',
    name: 'Home',
    component: HomeView,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const accessToken = localStorage.getItem('accessToken');
  if (to.meta.requiresAuth && !accessToken) {
    next({ name: 'Unauthorized' });
  } else {
    next();
  }
});

export default router;
