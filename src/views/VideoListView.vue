<template>
  <div class="max-w-7xl mx-auto px-4 py-8">
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-3xl font-bold text-gray-900">🎬 视频专区</h1>
        <p class="text-gray-500 mt-1">探索最新游戏视频</p>
      </div>
      <div class="flex items-center gap-2">
        <input v-model="keyword" @keyup.enter="search" type="text" placeholder="搜索..."
               class="border rounded-lg px-4 py-2 text-sm w-40 md:w-56 outline-none focus:border-red-400" />
        <button @click="search" class="bg-red-600 text-white px-4 py-2 rounded-lg text-sm hover:bg-red-700">搜索</button>
      </div>
    </div>

    <div class="flex gap-3 mb-6">
      <button v-for="tab in tabs" :key="tab.value" @click="activeTab = tab.value; currentPage = 1"
        :class="['px-5 py-2 rounded-full text-sm font-medium transition',
          activeTab === tab.value ? 'bg-red-600 text-white' : 'bg-white text-gray-600 border hover:bg-gray-100']">
        {{ tab.label }}
        <span class="ml-1 text-xs opacity-70">({{ tab.count }})</span>
      </button>
    </div>

    <div v-if="loading" class="flex justify-center py-40">
      <div class="w-10 h-10 border-4 border-red-200 border-t-red-600 rounded-full animate-spin"></div>
    </div>

    <div v-else-if="paginatedVideos.length > 0" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="video in paginatedVideos" :key="video.id"
           class="group bg-white rounded-2xl overflow-hidden shadow-sm hover:shadow-xl cursor-pointer transform hover:-translate-y-1 transition"
           @click="$router.push(`/videos/${video.id}`)">
        <div class="relative overflow-hidden">
          <img :src="video.thumbnail || 'https://picsum.photos/seed/video/640/360'" :alt="video.title"
               class="w-full h-44 object-cover group-hover:scale-105 transition-transform duration-500" />
          <div class="absolute inset-0 bg-black/0 group-hover:bg-black/20 flex items-center justify-center">
            <div class="w-14 h-14 rounded-full bg-white/90 flex items-center justify-center opacity-0 group-hover:opacity-100 transition">
              <svg class="w-6 h-6 text-red-600 ml-1" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
            </div>
          </div>
        </div>
        <div class="p-4">
          <h3 class="font-semibold text-gray-800 line-clamp-2 group-hover:text-red-600 transition">{{ video.title }}</h3>
        </div>
      </div>
    </div>

    <div v-else class="text-center py-40 text-gray-400">暂无视频</div>

    <div v-if="totalPages > 1" class="flex justify-center mt-10 gap-2">
      <button @click="currentPage--" :disabled="currentPage===1" class="px-4 py-2 rounded-full text-sm border disabled:opacity-40">←</button>
      <button v-for="p in displayPages" :key="p" @click="currentPage = p"
              :class="p===currentPage?'bg-red-600 text-white':'bg-white border'" class="w-10 h-10 rounded-full text-sm font-medium">{{ p }}</button>
      <button @click="currentPage++" :disabled="currentPage===totalPages" class="px-4 py-2 rounded-full text-sm border disabled:opacity-40">→</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

interface Video { id: number; title: string; thumbnail?: string; category: string; }

const allVideos = ref<Video[]>([]);
const loading = ref(true);
const keyword = ref('');
const activeTab = ref('all');
const currentPage = ref(1);
const pageSize = 12;

const tabs = [
  { label: '全部', value: 'all', count: 0 },
  { label: '官方视频', value: 'official', count: 0 },
  { label: '游戏攻略', value: 'guide', count: 0 },
  { label: '高光集锦', value: 'highlight', count: 0 },
];

const filteredVideos = computed(() => {
  let list = allVideos.value;
  if (activeTab.value !== 'all') {
    list = list.filter(v => v.category === activeTab.value);
  }
  if (keyword.value) {
    list = list.filter(v => v.title.includes(keyword.value));
  }
  return list;
});

const paginatedVideos = computed(() => {
  const start = (currentPage.value - 1) * pageSize;
  return filteredVideos.value.slice(start, start + pageSize);
});

const totalPages = computed(() => Math.ceil(filteredVideos.value.length / pageSize));

const displayPages = computed(() => {
  const pages: number[] = [];
  let start = Math.max(1, currentPage.value - 2);
  let end = Math.min(totalPages.value, start + 4);
  if (end - start < 4) start = Math.max(1, end - 4);
  for (let i = start; i <= end; i++) pages.push(i);
  return pages;
});

const fetchVideos = async () => {
  loading.value = true;
  try {
    const res = await axios.get('/api/videos', { params: { limit: 200 } });
    if (res.data.success) {
      allVideos.value = res.data.videos || [];
      tabs[0].count = allVideos.value.length;
      tabs[1].count = allVideos.value.filter(v => v.category === 'official').length;
      tabs[2].count = allVideos.value.filter(v => v.category === 'guide').length;
      tabs[3].count = allVideos.value.filter(v => v.category === 'highlight').length;
    }
  } catch (e: any) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const search = () => {
  currentPage.value = 1;
  fetchVideos();
};

onMounted(() => fetchVideos());
</script>