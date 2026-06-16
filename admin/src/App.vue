<template>
  <div class="min-h-screen bg-gray-100 flex">

    <!-- 移动端顶部栏 -->
    <div class="md:hidden bg-gray-800 text-white p-4 flex justify-between items-center">
      <span class="font-bold text-lg">⚓ 后台管理</span>
      <button @click="sidebarOpen = !sidebarOpen" class="text-white focus:outline-none">
        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
          <path v-if="!sidebarOpen" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
          <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </button>
    </div>

    <!-- 侧边栏 -->
    <aside class="w-64 bg-gray-800 text-white flex-shrink-0 min-h-screen">
      <div class="p-4 text-xl font-bold border-b border-gray-700">⚓ 后台管理</div>
      <nav class="p-4 space-y-2">
        <router-link to="/" class="block py-2 px-3 rounded hover:bg-gray-700">📰 文章管理</router-link>
        <router-link to="/characters" class="block py-2 px-3 rounded hover:bg-gray-700">🦸 角色管理</router-link>
        <router-link to="/videos" class="block py-2 px-3 rounded hover:bg-gray-700">🎬 视频管理</router-link>
        <hr class="border-gray-700 my-3" />
        <button @click="logout" class="w-full text-left py-2 px-3 rounded hover:bg-red-700 text-red-300">🚪 退出登录</button>
      </nav>
    </aside>
    <!-- 主内容区 -->
    <main class="flex-1 p-4 md:p-8">
      <router-view />
    </main>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const sidebarOpen = ref(false);

const logout = () => {
  localStorage.removeItem('token');
  sidebarOpen.value = false;
  router.push('/login');
};
</script>