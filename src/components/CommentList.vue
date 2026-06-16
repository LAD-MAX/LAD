<template>
  <div class="mt-10">
    <h3 class="text-xl font-bold mb-6">💬 评论 ({{ comments.length }})</h3>

    <!-- 发表评论框 -->
    <div v-if="userStore.isLoggedIn" class="mb-8 bg-gray-50 p-4 rounded-lg">
      <textarea
          v-model="newComment"
          placeholder="写下你的评论..."
          rows="3"
          class="w-full border rounded p-2 resize-none"
      ></textarea>
      <button
          @click="submitComment"
          :disabled="!newComment.trim()"
          class="mt-2 px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 disabled:opacity-50"
      >
        发表评论
      </button>
    </div>
    <div v-else class="mb-8 text-gray-500">
      <router-link to="/login" class="text-blue-600">登录</router-link> 后参与评论
    </div>

    <!-- 评论列表 -->
    <div v-if="comments.length > 0" class="space-y-6">
      <CommentItem
          v-for="comment in comments"
          :key="comment.id"
          :comment="comment"
          :article-id="articleId"
          @reply="handleReply"
          @delete="handleDelete"
      />
    </div>
    <div v-else class="text-gray-500 text-center py-10">暂无评论</div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import request from '../api/request';
import { useUserStore } from '../stores/useUserStore';
import CommentItem from './CommentItem.vue';

const props = defineProps<{ articleId: number }>();
const userStore = useUserStore();

interface Comment {
  id: number;
  content: string;
  createdAt: string;
  user: { id: number; username: string; avatar?: string };
  replies?: Comment[];
}

const comments = ref<Comment[]>([]);
const newComment = ref('');

const fetchComments = async () => {
  const res = await request.get(`/comments/article/${props.articleId}`);
  if (res.data.success) {
    comments.value = res.data.data;
  }
};

const submitComment = async () => {
  if (!newComment.value.trim()) return;
  try {
    const res = await request.post('/comments', {
      content: newComment.value,
      articleId: props.articleId,
      parentId: null,
    });
    if (res.data.success) {
      // 将新评论插入到列表顶部
      comments.value.unshift(res.data.data);
      newComment.value = '';
    }
  } catch (err: any) {
    alert(err.response?.data?.message || '发表失败');
  }
};

const handleReply = async (parentId: number, content: string) => {
  try {
    const res = await request.post('/comments', {
      content,
      articleId: props.articleId,
      parentId,
    });
    if (res.data.success) {
      // 刷新整个评论列表以获取最新树形结构
      await fetchComments();
    }
  } catch (err: any) {
    alert(err.response?.data?.message || '回复失败');
  }
};

const handleDelete = async (commentId: number) => {
  if (!confirm('确定删除该评论吗？')) return;
  const res = await request.delete(`/comments/${commentId}`);
  if (res.data.success) {
    comments.value = comments.value.filter(c => c.id !== commentId);
  }
};

onMounted(fetchComments);
</script>