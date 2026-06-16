<template>
  <div class="max-w-md mx-auto mt-20 p-6 bg-white rounded-xl shadow">
    <h2 class="text-2xl font-bold mb-6 text-center">📝 注册</h2>
    <form @submit.prevent="handleRegister">
      <div class="mb-4">
        <input v-model="username" type="text" placeholder="用户名" class="w-full border rounded px-3 py-2" required />
      </div>
      <div class="mb-4">
        <input v-model="email" type="email" placeholder="邮箱" class="w-full border rounded px-3 py-2" required />
      </div>
      <div class="mb-6">
        <input v-model="password" type="password" placeholder="密码（至少6位）" class="w-full border rounded px-3 py-2" required />
      </div>
      <button type="submit" class="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700">注册</button>
    </form>
    <p class="mt-4 text-center text-sm text-gray-600">
      已有账号？<router-link to="/login" class="text-blue-600">去登录</router-link>
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
const email = ref('');
const password = ref('');
const error = ref('');

const handleRegister = async () => {
  error.value = '';
  const success = await userStore.register(username.value, email.value, password.value);
  if (success) {
    router.push('/');
  } else {
    error.value = '注册失败，可能是用户名或邮箱已存在';
  }
};
</script>