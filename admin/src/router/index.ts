import { createRouter, createWebHistory } from 'vue-router';
import ArticleManager from '../views/ArticleManager.vue';
import CharacterManager from '../views/CharacterManager.vue';
import VideoManager from '../views/VideoManager.vue';
import LoginView from '../views/LoginView.vue';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        { path: '/login', component: LoginView },
        { path: '/', component: ArticleManager, meta: { requiresAuth: true } },
        { path: '/characters', component: CharacterManager, meta: { requiresAuth: true } },
        { path: '/videos', component: VideoManager, meta: { requiresAuth: true } },
    ],
});

router.beforeEach((to, _from, next) => {
    const token = localStorage.getItem('token');
    if (to.meta.requiresAuth && !token) {
        next('/login');
    } else if (to.path === '/login' && token) {
        next('/');
    } else {
        next();
    }
});

export default router;