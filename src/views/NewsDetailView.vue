
<template>
  <div class="max-w-4xl mx-auto p-6">
    <!-- 返回按钮 -->
    <button
        @click="$router.push('/')"
        class="mb-6 flex items-center text-blue-600 hover:text-blue-800 transition"
    >
      <svg class="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
      </svg>
      返回首页
    </button>

    <!-- 加载状态 -->
    <div v-if="loading" class="text-center py-20 text-gray-400">加载中...</div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="text-center py-20 text-red-500">{{ error }}</div>

    <!-- 文章内容（TypeScript 在此分支内确定 article 非空） -->
    <article v-else-if="article">
      <!-- 封面图 -->
      <img
          v-if="article.coverImage"
          :src="article.coverImage"
          :alt="article.title"
          class="w-full h-64 md:h-96 object-cover rounded-xl mb-8 shadow"
      />

      <!-- 标题 -->
      <h1 class="text-3xl md:text-4xl font-bold mb-4">{{ article.title }}</h1>

      <!-- 元信息：作者、日期、分类 -->
      <div class="flex flex-wrap items-center gap-4 text-sm text-gray-500 mb-6">
        <div class="flex items-center">
          <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
          </svg>
          {{ article.author?.username || '未知' }}
        </div>
        <div class="flex items-center">
          <svg class="w-4 h-4 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
          </svg>
          {{ formatDate(article.publishedAt) }}
        </div>
        <span class="bg-blue-100 text-blue-600 px-2 py-1 rounded text-xs">{{ article.category?.name }}</span>
      </div>

      <!-- 标签 -->
      <div v-if="article.tags?.length" class="flex flex-wrap gap-2 mb-6">
        <span
            v-for="t in article.tags"
            :key="t.tag.id"
            class="bg-gray-100 text-gray-600 px-3 py-1 rounded-full text-xs"
        >
          #{{ t.tag.name }}
        </span>
      </div>

      <!-- 正文（富文本） -->
      <div
          class="prose max-w-none text-gray-800 leading-relaxed"
          v-html="article.content"
      ></div>

      <!-- 评论区域 -->
      <CommentList v-if="article.id" :article-id="article.id" />


    </article>

    <!-- 兜底情况：数据异常 -->
    <div v-else class="text-center py-20 text-gray-400">文章数据异常</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';
import CommentList from '../components/CommentList.vue';

const route = useRoute();
const slug = route.params.slug as string;

interface Tag {
  tag: {
    id: number;
    name: string;
  };
}

interface Article {
  id: number;
  title: string;
  slug: string;
  summary?: string;
  content: string;
  coverImage?: string;
  author?: {
    username: string;
    avatar?: string;
  };
  category?: {
    name: string;
    slug: string;
  };
  tags?: Tag[];
  publishedAt: string;
  viewCount?: number;
}

const article = ref<Article | null>(null);
const loading = ref(true);
const error = ref('');

const fetchArticle = async () => {
  try {
    const res = await axios.get(`/api/news/${slug}`);
    if (res.data.success) {
      article.value = res.data.data;
    } else {
      error.value = '文章不存在';
    }
  } catch (err: any) {
    if (err.response?.status === 404) {
      error.value = '文章不存在';
    } else {
      error.value = '加载失败，请检查网络';
    }
  } finally {
    loading.value = false;
  }
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  });
};

onMounted(fetchArticle);
</script>

<style scoped>
.prose {
  font-size: 1.1rem;
  line-height: 1.8;
}
.prose :deep(h3) {
  font-size: 1.4rem;
  margin: 1.5em 0 0.8em;
}
.prose :deep(p) {
  margin-bottom: 1.2em;
}
.prose :deep(ul) {
  list-style: disc;
  padding-left: 1.5em;
  margin-bottom: 1.2em;
}

.prose {
  background: #fafafa;
  padding: 1rem;
  border-radius: 0.5rem;
}
</style>