<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h1 class="text-2xl font-bold">📰 文章管理</h1>
      <button @click="showForm = true; editingArticle = null" class="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700">
        + 新建文章
      </button>
    </div>

    <div class="bg-white rounded-xl shadow overflow-hidden">
      <table class="w-full">
        <thead class="bg-gray-50">
        <tr>
          <th class="px-4 py-3 text-left">标题</th>
          <th class="px-4 py-3 text-left">类型</th>
          <th class="px-4 py-3 text-left">状态</th>
          <th class="px-4 py-3 text-left">发布时间</th>
          <th class="px-4 py-3 text-left">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="article in articles" :key="article.id" class="border-t hover:bg-gray-50">
          <td class="px-4 py-3">{{ article.title }}</td>
          <td class="px-4 py-3">{{ article.type === 'NEWS' ? '资讯' : '攻略' }}</td>
          <td class="px-4 py-3">
              <span :class="article.isPublished ? 'text-green-600' : 'text-gray-400'">
                {{ article.isPublished ? '已发布' : '草稿' }}
              </span>
          </td>
          <td class="px-4 py-3 text-sm">{{ formatDate(article.publishedAt) }}</td>
          <td class="px-4 py-3 space-x-2">
            <button @click="editArticle(article)" class="text-blue-600 hover:underline">编辑</button>
            <button @click="deleteArticle(article.id)" class="text-red-600 hover:underline">删除</button>
          </td>
        </tr>
        </tbody>
      </table>
    </div>

    <!-- 编辑弹窗（简化版，生产环境可用模态框） -->
    <div v-if="showForm" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
      <div class="bg-white rounded-xl p-6 w-full max-w-2xl max-h-[90vh] overflow-y-auto">
        <h2 class="text-xl font-bold mb-4">{{ editingArticle ? '编辑文章' : '新建文章' }}</h2>
        <form @submit.prevent="saveArticle">
          <input v-model="form.title" placeholder="标题" class="w-full border rounded px-3 py-2 mb-3" required />
          <input v-model="form.slug" placeholder="URL 别名 (slug)" class="w-full border rounded px-3 py-2 mb-3" required />
          <select v-model="form.type" class="w-full border rounded px-3 py-2 mb-3">
            <option value="NEWS">资讯</option>
            <option value="GUIDE">攻略</option>
          </select>
          <textarea v-model="form.summary" placeholder="摘要" rows="2" class="w-full border rounded px-3 py-2 mb-3"></textarea>
          <textarea v-model="form.content" placeholder="正文 (支持 HTML)" rows="6" class="w-full border rounded px-3 py-2 mb-3" required></textarea>
          <input v-model="form.coverImage" placeholder="封面图片 URL" class="w-full border rounded px-3 py-2 mb-3" />
          <label class="flex items-center mb-3">
            <input type="checkbox" v-model="form.isPublished" class="mr-2" /> 立即发布
          </label>
          <div class="flex justify-end gap-3">
            <button type="button" @click="showForm = false" class="px-4 py-2 border rounded">取消</button>
            <button type="submit" class="px-4 py-2 bg-blue-600 text-white rounded">保存</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';

const articles = ref<any[]>([]);
const showForm = ref(false);
const editingArticle = ref<any>(null);
const form = ref({
  title: '',
  slug: '',
  summary: '',
  content: '',
  coverImage: '',
  type: 'NEWS',
  isPublished: true,
});

const fetchArticles = async () => {
  // 需要后端返回所有文章（包含未发布），这里暂时用现有的 news 接口
  const res = await axios.get('/api/news');
  if (res.data.success) articles.value = res.data.data;
};

const editArticle = (article: any) => {
  editingArticle.value = article;
  form.value = {
    title: article.title,
    slug: article.slug,
    summary: article.summary || '',
    content: article.content,
    coverImage: article.coverImage || '',
    type: article.type,
    isPublished: article.isPublished,
  };
  showForm.value = true;
};

const saveArticle = async () => {
  // 需要后端支持文章创建和更新接口（见下文）
  if (editingArticle.value) {
    await axios.put(`/api/admin/articles/${editingArticle.value.id}`, form.value);
  } else {
    await axios.post('/api/admin/articles', form.value);
  }
  showForm.value = false;
  fetchArticles();
};

const deleteArticle = async (id: number) => {
  if (!confirm('确定删除？')) return;
  await axios.delete(`/api/admin/articles/${id}`);
  fetchArticles();
};

const formatDate = (dateStr: string) => {
  if (!dateStr) return '-';
  return new Date(dateStr).toLocaleDateString('zh-CN');
};

onMounted(fetchArticles);
</script>