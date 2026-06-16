<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-100">
    <div class="bg-white p-8 rounded-xl shadow w-96">
      <h1 class="text-2xl font-bold mb-6 text-center">🔐 管理员登录</h1>
      <form @submit.prevent="handleLogin">
        <input v-model="username" type="text" placeholder="用户名" class="w-full border rounded px-3 py-2 mb-4" required />
        <input v-model="password" type="password" placeholder="密码" class="w-full border rounded px-3 py-2 mb-6" required />
        <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">登录</button>
      </form>
      <p v-if="error" class="mt-4 text-red-500 text-center">{{ error }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const username = ref('');
const password = ref('');
const error = ref('');

const handleLogin = async () => {
  try {
    const res = await axios.post('/api/auth/login', {
      username: username.value,
      password: password.value,
    });
    if (res.data.success) {
      localStorage.setItem('token', res.data.data.token);
      router.push('/');
    }
  } catch (err: any) {
    error.value = err.response?.data?.message || '登录失败';
  }
};
</script>