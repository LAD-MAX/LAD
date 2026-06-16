<template>
  <div class="max-w-6xl mx-auto p-6">
    <!-- 返回按钮 -->
    <router-link to="/characters" class="inline-flex items-center gap-1 text-blue-600 hover:text-blue-800 mb-4 text-sm">
      <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/></svg>
      返回角色展示
    </router-link>

    <h1 class="text-3xl font-bold text-red-600 mb-6">🦸 角色图鉴</h1>

    <!-- 筛选器 -->
    <div class="flex flex-wrap gap-4 mb-8">
      <select v-model="filterRarity" @change="fetchCharacters" class="border rounded px-3 py-2">
        <option value="">全部稀有度</option>
        <option value="NORMAL">普通</option>
        <option value="EXCELLENT">优异</option>
        <option value="SUPERIOR">卓越</option>
        <option value="LEGENDARY">超凡</option>
        <option value="MYTHIC">传奇</option>
        <option value="COLLECTOR">典藏</option>
      </select>
    </div>

    <!-- 加载/错误 -->
    <div v-if="loading" class="text-center py-20 text-gray-400">加载中...</div>
    <div v-else-if="error" class="text-center py-20 text-red-500">{{ error }}</div>

    <!-- 角色网格 -->
    <div v-else class="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-6">
      <div
        v-for="char in characters"
        :key="char.id"
        class="bg-white rounded-xl shadow hover:shadow-lg transition cursor-pointer overflow-hidden"
        @click="$router.push(`/characters/${char.id}`)"
      >
        <img
          v-if="char.avatar"
          :src="char.avatar"
          :alt="char.name"
          class="w-full h-40 object-cover"
          @error="($event.target as HTMLImageElement).src = 'https://picsum.photos/200/200'"
        />
        <div v-else class="w-full h-40 bg-gray-200 flex items-center justify-center text-gray-400 text-sm">暂无图片</div>
        <div class="p-3 text-center">
          <h2 class="font-semibold text-lg">{{ char.name }}</h2>
          <p class="text-sm text-gray-500">{{ char.alias || '' }}</p>
          <div class="mt-2 flex justify-center gap-2">
            <span class="px-2 py-0.5 rounded text-xs" :class="rarityColors[char.rarity] || 'bg-gray-100 text-gray-800'">
              {{ rarityLabels[char.rarity] || char.rarity }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

interface Character {
  id: number;
  name: string;
  alias?: string;
  rarity: string;
  avatar?: string;
}

const characters = ref<Character[]>([]);
const loading = ref(true);
const error = ref('');
const filterRarity = ref('');

const rarityColors: Record<string, string> = {
  NORMAL: 'bg-green-100 text-green-800',
  EXCELLENT: 'bg-blue-100 text-blue-800',
  SUPERIOR: 'bg-purple-100 text-purple-800',
  LEGENDARY: 'bg-orange-100 text-orange-800',
  MYTHIC: 'bg-yellow-100 text-yellow-800',
  COLLECTOR: 'bg-cyan-100 text-cyan-800',
};

const rarityLabels: Record<string, string> = {
  NORMAL: '普通', EXCELLENT: '优异', SUPERIOR: '卓越',
  LEGENDARY: '超凡', MYTHIC: '传奇', COLLECTOR: '典藏',
};

const fetchCharacters = async () => {
  loading.value = true;
  error.value = '';
  try {
    const params: any = {};
    if (filterRarity.value) params.rarity = filterRarity.value;
    const res = await axios.get('/api/characters', { params });
    if (res.data.success) {
      characters.value = res.data.data;
    } else {
      error.value = '获取角色失败';
    }
  } catch (err: any) {
    error.value = '无法连接服务器';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchCharacters);
</script>