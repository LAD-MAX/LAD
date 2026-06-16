<template>
  <div class="max-w-4xl mx-auto p-6">
    <h1 class="text-2xl font-bold mb-6">🔍 搜索结果：{{ keyword }}</h1>

    <div v-if="loading" class="text-center py-20">加载中...</div>
    <div v-else-if="results.length === 0" class="text-center py-20 text-gray-400">未找到相关文章</div>

    <div v-else class="space-y-4">
      <div
          v-for="article in results"
          :key="article.id"
          class="bg-white rounded-xl shadow p-4 hover:shadow-lg cursor-pointer"
          @click="$router.push(`/news/${article.slug}`)"
      >
        <h2 class="text-lg font-semibold">{{ article.title }}</h2>
        <p class="text-gray-500 text-sm mt-1">{{ article.summary }}</p>
        <div class="text-xs text-gray-400 mt-2">{{ formatDate(article.publishedAt) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const keyword = ref(route.query.keyword || '');
const results = ref<any[]>([]);
const loading = ref(true);

const fetchResults = async () => {
  try {
    const res = await axios.get('/api/news/search', {
      params: { keyword: keyword.value },
    });
    if (res.data.success) {
      results.value = res.data.data;
    }
  } catch (err) {
    console.error(err);
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN');
};

onMounted(fetchResults);
</script>