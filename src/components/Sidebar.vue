<template>
  <aside class="space-y-6">
    <!-- 搜索框 -->
    <div class="bg-white rounded-xl shadow p-4">
      <h3 class="font-bold mb-3">🔍 搜索文章</h3>
      <div class="flex">
        <input
            v-model="keyword"
            @keyup.enter="search"
            type="text"
            placeholder="输入关键词..."
            class="flex-1 border rounded-l px-3 py-2 text-sm"
        />
        <button @click="search" class="bg-red-600 text-white px-4 py-2 rounded-r text-sm hover:bg-red-700">
          搜索
        </button>
      </div>
    </div>

    <!-- 热门文章 -->
    <div class="bg-white rounded-xl shadow p-4">
      <h3 class="font-bold mb-3">🔥 热门文章</h3>
      <div v-if="hotArticles.length > 0" class="space-y-3">
        <div
            v-for="(article, index) in hotArticles"
            :key="article.id"
            class="flex items-start gap-3 cursor-pointer hover:bg-gray-50 p-2 rounded"
            @click="$router.push(`/news/${article.slug}`)"
        >
          <span class="text-red-500 font-bold text-lg">{{ index + 1 }}</span>
          <div>
            <p class="text-sm font-medium line-clamp-2">{{ article.title }}</p>
            <p class="text-xs text-gray-400 mt-1">👁 {{ article.viewCount }} 次阅读</p>
          </div>
        </div>
      </div>
      <p v-else class="text-sm text-gray-400">暂无数据</p>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const router = useRouter();
const keyword = ref('');
const hotArticles = ref<any[]>([]);

const fetchHotArticles = async () => {
  const res = await axios.get('/api/news/hot');
  if (res.data.success) {
    hotArticles.value = res.data.data;
  }
};

const search = () => {
  if (keyword.value.trim()) {
    router.push(`/search?keyword=${encodeURIComponent(keyword.value)}`);
  }
};

onMounted(() => {
  fetchHotArticles();
});
</script>