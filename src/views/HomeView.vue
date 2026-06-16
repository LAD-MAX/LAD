<template>
  <div>
    <!-- 全宽 Banner -->
    <div class="relative w-full h-[50vh] md:h-[60vh] overflow-hidden">
      <img
        src="https://p1.dailygn.com/obj/g-marketing-act-assets/2026_02_04_16_31_47/1489060004823_s1607207.png"
        alt="航海王热血航线"
        class="w-full h-full object-cover"
      />
      <div class="absolute inset-0 bg-gradient-to-r from-black/60 via-black/30 to-transparent"></div>



      <!-- 左侧渐变遮罩 -->
        <div class="absolute inset-0 bg-gradient-to-r from-black/60 via-black/20 to-transparent"></div>

        <!-- 底部渐变：让 logo 边缘淡化融合 -->
        <div class="absolute bottom-0 left-0 w-1/2 h-32 bg-gradient-to-t from-black/80 to-transparent"></div>



        <!-- 左下方：logo 图片，四条边缘线淡化 -->
        <div class="absolute bottom-0 left-0 z-10">
          <img
            src="/images/characters/logol.png"
            alt="航海王热血航线"
            class="h-20 md:h-28"
            style="
              mask-image: linear-gradient(to right, transparent 0%, black 15%, black 85%, transparent 100%),
                           linear-gradient(to bottom, transparent 0%, black 15%, black 85%, transparent 100%);
              -webkit-mask-image: linear-gradient(to right, transparent 0%, black 15%, black 85%, transparent 100%),
                                  linear-gradient(to bottom, transparent 0%, black 15%, black 85%, transparent 100%);
              mask-composite: intersect;
              -webkit-mask-composite: source-in;
            "
          />
        </div>

      <!-- 右侧：搜索框 + 热门文章 -->
      <div class="absolute top-1/2 right-6 md:right-12 -translate-y-1/2 w-64 md:w-72 space-y-4">
        <div class="bg-black/50 backdrop-blur rounded-xl p-3">
          <div class="flex">
            <input
                v-model="keyword"
                @keyup.enter="search"
                type="text"
                placeholder="搜索文章..."
                class="flex-1 bg-black/30 text-white placeholder-white/50 rounded-l px-3 py-2 text-sm outline-none border border-white/20"
            />
            <button @click="search" class="bg-red-600 text-white px-4 py-2 rounded-r text-sm hover:bg-red-700">
              搜索
            </button>
          </div>
        </div>

        <div class="bg-black/50 backdrop-blur rounded-xl p-3">
          <h3 class="text-white font-bold text-sm mb-3">🔥 热门文章</h3>
          <div v-if="hotArticles.length > 0" class="space-y-2">
            <div
                v-for="(article, index) in hotArticles"
                :key="article.id"
                class="flex items-start gap-2 cursor-pointer hover:bg-white/10 p-1.5 rounded transition"
                @click="$router.push(`/news/${article.slug}`)"
            >
              <span class="text-red-400 font-bold text-xs">{{ index + 1 }}</span>
              <p class="text-white/80 text-xs line-clamp-2">{{ article.title }}</p>
            </div>
          </div>
          <p v-else class="text-white/50 text-xs">暂无数据</p>
        </div>
      </div>
    </div>

    <!-- 资讯列表：全宽、无最大宽度限制 -->
    <div class="w-full bg-white">
      <div v-if="loading" class="text-center py-20 text-gray-400">加载中...</div>
      <div v-else-if="error" class="text-center py-20 text-red-500">{{ error }}</div>

      <div v-else>
        <div class="divide-y divide-gray-200">
          <div
              v-for="article in articles"
              :key="article.id"
              class="py-5 px-6 md:px-12 cursor-pointer hover:bg-gray-50 transition"
              @click="$router.push(`/news/${article.slug}`)"
          >
            <div class="flex items-center gap-3 mb-1">
              <span
                  class="text-xs px-2 py-0.5 rounded whitespace-nowrap"
                  :class="article.type === 'NEWS' ? 'bg-blue-100 text-blue-600' : 'bg-green-100 text-green-600'"
              >
                {{ article.type === 'NEWS' ? '资讯' : '攻略' }}
              </span>
              <span class="text-sm text-gray-400">{{ formatDate(article.publishedAt) }}</span>
            </div>
            <h2 class="text-lg font-semibold hover:text-red-600 transition leading-snug">{{ article.title }}</h2>
          </div>
        </div>

        <!-- 加载更多 -->
        <div v-if="hasMore" class="flex justify-center py-8">
          <button
              @click="loadMore"
              :disabled="loadingMore"
              class="px-8 py-3 bg-red-600 text-white rounded-full hover:bg-red-700 disabled:opacity-50 transition shadow-lg"
          >
            {{ loadingMore ? '加载中...' : '加载更多' }}
          </button>
        </div>
        <div v-else-if="articles.length > 0" class="text-center text-gray-400 py-8">
          已加载全部 {{ articles.length }} 条资讯
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

interface Article {
  id: number;
  title: string;
  slug: string;
  type: string;
  publishedAt: string;
}

const router = useRouter();
const keyword = ref('');
const articles = ref<Article[]>([]);
const hotArticles = ref<Article[]>([]);
const loading = ref(true);
const error = ref('');
const loadingMore = ref(false);
const page = ref(1);
const hasMore = ref(true);

const fetchNews = async (pageNum = 1) => {
  try {
    const res = await axios.get('/api/news', {
      params: { page: pageNum, limit: 20 }
    });
    if (res.data.success) {
      const newArticles = res.data.data || [];
      if (pageNum === 1) {
        articles.value = newArticles;
      } else {
        articles.value = [...articles.value, ...newArticles];
      }
      hasMore.value = newArticles.length === 20;
    } else {
      error.value = '获取资讯失败';
    }
  } catch (err: any) {
    error.value = '无法连接服务器';
  } finally {
    loading.value = false;
    loadingMore.value = false;
  }
};

const fetchHotArticles = async () => {
  const res = await axios.get('/api/news/hot');
  if (res.data.success) {
    hotArticles.value = res.data.data;
  }
};

const loadMore = async () => {
  loadingMore.value = true;
  page.value++;
  await fetchNews(page.value);
};

const search = () => {
  if (keyword.value.trim()) {
    router.push(`/search?keyword=${encodeURIComponent(keyword.value)}`);
  }
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN', { year: 'numeric', month: 'long', day: 'numeric' });
};

onMounted(() => {
  fetchNews();
  fetchHotArticles();
});
</script>