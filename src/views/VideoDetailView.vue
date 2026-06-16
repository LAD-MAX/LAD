<template>
  <div class="max-w-5xl mx-auto p-6">
    <button @click="$router.push('/videos')" class="mb-6 flex items-center text-blue-600 hover:text-blue-800">
      ← 返回视频列表
    </button>

    <div v-if="loading" class="text-center py-20">加载中...</div>
    <div v-else-if="error" class="text-center py-20 text-red-500">{{ error }}</div>

    <div v-else-if="video">
      <!-- 视频播放器 -->
      <div class="mb-6 bg-black rounded-xl overflow-hidden">
        <video
          v-if="video.url"
          :src="video.url"
          :poster="video.thumbnail"
          controls
          autoplay
          class="w-full aspect-video"
          style="max-height: 70vh;"
        >
          您的浏览器不支持视频播放
        </video>
      </div>

      <h1 class="text-2xl font-bold mt-4">{{ video.title }}</h1>
      <div class="flex items-center gap-4 mt-2 text-sm text-gray-500">
        <span>👁 {{ video.viewCount || 0 }} 次观看</span>
      </div>
      <p class="mt-4 text-gray-700 leading-relaxed">{{ video.description || '暂无简介' }}</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const id = route.params.id;

interface Video {
  id: number;
  title: string;
  description?: string;
  url: string;
  thumbnail?: string;
  source: string;
  viewCount?: number;
}

const video = ref<Video | null>(null);
const loading = ref(true);
const error = ref('');

const fetchVideo = async () => {
  try {
    const res = await axios.get(`/api/videos/${id}`);
    if (res.data.success) {
      video.value = res.data.data;
    } else {
      error.value = '视频不存在';
    }
  } catch (err: any) {
    error.value = '加载失败';
  } finally {
    loading.value = false;
  }
};

onMounted(fetchVideo);
</script>