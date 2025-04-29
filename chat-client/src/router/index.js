import { createRouter, createWebHashHistory } from 'vue-router';
import MemberCreate from '@/views/MemberCreate.vue';
import LoginPage from '@/views/LoginPage.vue';

const routes = [
  {
    path: '/member/create',
    name: 'MemberCreate',
    component: MemberCreate,
  },
  {
    path: '/login',
    name: 'LoginPage',
    component: LoginPage,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
