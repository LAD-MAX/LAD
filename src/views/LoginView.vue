<template>
  <div class="max-w-md mx-auto mt-20 p-6 bg-white rounded-xl shadow">
    <h2 class="text-2xl font-bold mb-6 text-center">🔐 登录</h2>
    <form @submit.prevent="handleLogin">
      <div class="mb-4">
        <input v-model="username" type="text" placeholder="用户名" class="w-full border rounded px-3 py-2" required />
      </div>
      <div class="mb-6">
        <input v-model="password" type="password" placeholder="密码" class="w-full border rounded px-3 py-2" required />
      </div>
      <button type="submit" class="w-full bg-red-600 text-white py-2 rounded hover:bg-red-700">登录</button>
    </form>
    <p class="mt-4 text-center text-sm text-gray-600">
      还没有账号？<router-link to="/register" class="text-blue-600">立即注册</router-link>
    </p>
    <p v-if="error" class="mt-2 text-red-500 text-center">{{ error }}</p>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '../stores/useUserStore';

const router = useRouter();
const userStore = useUserStore();
const username = ref('');
const password = ref('');
const error = ref('');

const handleLogin = async () => {
  error.value = '';
  const success = await userStore.login(username.value, password.value);
  if (success) {
    router.push('/'); // 登录成功跳转首页
  } else {
    error.value = '用户名或密码错误';
  }
};
</script>