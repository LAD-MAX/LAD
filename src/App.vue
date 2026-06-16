<template>
  <div class="min-h-screen bg-gray-100 flex flex-col">
    <!-- 顶部导航 -->
    <header class="bg-white shadow sticky top-0 z-50">
      <div class="max-w-6xl mx-auto px-4 py-3 flex items-center justify-between">
        <router-link to="/" class="flex items-center gap-2">
          <img src="/images/characters/logo.png" alt="航海王热血航线" class="h-8" />
        </router-link>

        <!-- 桌面端导航链接 -->
        <nav class="hidden md:flex gap-6 text-sm font-medium items-center">
          <router-link to="/" class="hover:text-red-600 transition" active-class="text-red-600 border-b-2 border-red-600 pb-1">首页</router-link>
          <router-link to="/characters" class="hover:text-red-600 transition" active-class="text-red-600 border-b-2 border-red-600 pb-1">角色图鉴</router-link>
          <router-link to="/videos" class="hover:text-red-600 transition" active-class="text-red-600 border-b-2 border-red-600 pb-1">视频专区</router-link>

          <template v-if="userStore.isLoggedIn">
            <span class="text-gray-700">{{ userStore.user?.username }}</span>
            <button @click="handleLogout" class="text-gray-500 hover:text-red-600">退出</button>
          </template>
          <template v-else>
            <router-link to="/login" class="text-gray-600 hover:text-red-600">登录</router-link>
            <router-link to="/register" class="text-gray-600 hover:text-red-600">注册</router-link>
          </template>
        </nav>

        <!-- 移动端汉堡按钮 -->
        <button @click="mobileOpen = !mobileOpen" class="md:hidden text-gray-600 focus:outline-none">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path v-if="!mobileOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- 移动端展开菜单 -->
      <div v-if="mobileOpen" class="md:hidden bg-white border-t px-4 pb-4 space-y-2">
        <router-link to="/" @click="mobileOpen = false" class="block py-2 hover:text-red-600">首页</router-link>
        <router-link to="/characters" @click="mobileOpen = false" class="block py-2 hover:text-red-600">角色图鉴</router-link>
        <router-link to="/videos" @click="mobileOpen = false" class="block py-2 hover:text-red-600">视频专区</router-link>
        <hr />
        <template v-if="userStore.isLoggedIn">
          <span class="block py-2 text-gray-700">{{ userStore.user?.username }}</span>
          <button @click="handleLogout" class="block py-2 text-red-500">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" @click="mobileOpen = false" class="block py-2">登录</router-link>
          <router-link to="/register" @click="mobileOpen = false" class="block py-2">注册</router-link>
        </template>
      </div>
    </header>

    <!-- 主体内容 -->
    <main class="flex-1">
      <router-view />
    </main>

    <!-- 页脚 -->
    <footer class="bg-white mt-12 py-6 text-center text-gray-500 text-sm">
      © 2025 热血航线资讯站 - 玩家共建
    </footer>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useUserStore } from './stores/useUserStore';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();
const mobileOpen = ref(false);

const handleLogout = () => {
  userStore.logout();
  mobileOpen.value = false;
  router.push('/');
};
</script>