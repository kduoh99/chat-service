import { createRouter, createWebHashHistory } from 'vue-router';
import MemberSignup from '@/views/MemberSignup.vue';
import MemberLogin from '@/views/MemberLogin.vue';
import MemberList from '@/views/MemberList.vue';

const routes = [
  {
    path: '/signup',
    name: 'MemberSignup',
    component: MemberSignup,
  },
  {
    path: '/login',
    name: 'MemberLogin',
    component: MemberLogin,
  },
  {
    path: '/members',
    name: 'MemberList',
    component: MemberList,
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes,
});

export default router;
