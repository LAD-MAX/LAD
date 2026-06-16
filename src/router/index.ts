import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import NewsDetailView from '../views/NewsDetailView.vue';
import CharacterShowcase from '../views/CharacterShowcase.vue';
import CharacterListView from '../views/CharacterListView.vue';
import CharacterDetailView from '../views/CharacterDetailView.vue';
import VideoListView from '../views/VideoListView.vue';
import VideoDetailView from '../views/VideoDetailView.vue';
import SearchResultView from '../views/SearchResultView.vue';
import LoginView from '../views/LoginView.vue';
import RegisterView from '../views/RegisterView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', component: HomeView },
    { path: '/news/:slug', component: NewsDetailView },
    { path: '/characters', component: CharacterShowcase },        // 官网风格轮播
    { path: '/characters/list', component: CharacterListView },   // 筛选列表
    { path: '/characters/:id', component: CharacterDetailView },  // 详情页
    { path: '/videos', component: VideoListView },
    { path: '/videos/:id', component: VideoDetailView },
    { path: '/search', component: SearchResultView },
    { path: '/login', component: LoginView, meta: { guest: true } },
    { path: '/register', component: RegisterView, meta: { guest: true } },
  ],
});

export default router;