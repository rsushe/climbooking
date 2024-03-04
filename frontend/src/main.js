import {createApp} from 'vue';
import {createRouter, createWebHistory} from 'vue-router';
import App from './App.vue';
import TrainingPlaces from './components/TrainingPlaces.vue';
import Tournaments from './components/Tournaments.vue';
import Climbers from './components/Climbers.vue';
import Bookings from '@/components/Bookings.vue';
import Notifications from '@/components/Notifications.vue';
import Routes from '@/components/Routes.vue';
import Achievement from '@/components/Achievement.vue';
import axios from 'axios';
import RegisterForm from "@/components/RegisterForm.vue";
import LoginForm from "@/components/LoginForm.vue";

const routes = [
    {
        path: '/',
        redirect: '/register'
    },
    {
        path: '/training-places',
        component: TrainingPlaces,
        meta: { requiresAuth: true }
    },
    {
        path: '/tournaments',
        component: Tournaments,
        meta: { requiresAuth: true }
    },
    {
        path: '/climbers',
        component: Climbers,
        meta: { requiresAuth: true }
    },
    {
        path: '/bookings',
        component: Bookings,
        meta: { requiresAuth: true }
    },
    {
        path: '/notifications',
        component: Notifications,
        meta: { requiresAuth: true }
    },
    {
        path: '/routes',
        component: Routes,
        meta: { requiresAuth: true }
    },
    {
        path: '/achievement',
        component: Achievement,
        meta: { requiresAuth: true }
    },
    {
        path: '/register',
        component: RegisterForm
    },
    {
        path: '/login',
        component: LoginForm
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const isAuthenticated = localStorage.getItem('authToken');

    if (to.matched.some(record => record.meta.requiresAuth) && !isAuthenticated) {
        next({
            path: '/login',
            query: { redirect: to.fullPath, authRequired: true }
        });
    } else {
        next();
    }

});

axios.defaults.baseURL='http://localhost:8080';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.use(router);
app.mount('#app');
