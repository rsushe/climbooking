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

const routes = [
    {
        path: '/',
        redirect: '/training-places'
    },
    {
        path: '/training-places',
        component: TrainingPlaces
    },
    {
        path: '/tournaments',
        component: Tournaments
    },
    {
        path: '/climbers',
        component: Climbers
    },
    {
        path: '/bookings',
        component: Bookings
    },
    {
        path: '/notifications',
        component: Notifications
    },
    {
        path: '/routes',
        component: Routes
    },
    {
        path: '/achievement',
        component: Achievement
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

axios.defaults.baseURL='https://158.160.140.66:8080';

const app = createApp(App);
app.config.globalProperties.$axios = axios;
app.use(router);
app.mount('#app');
