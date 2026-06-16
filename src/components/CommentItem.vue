<template>
  <div class="border-l-2 border-gray-200 pl-4">
    <div class="flex items-start gap-3">
      <div class="font-semibold text-sm">{{ comment.user.username }}</div>
      <div class="text-xs text-gray-400">{{ formatDate(comment.createdAt) }}</div>
    </div>
    <p class="mt-1 text-gray-700">{{ comment.content }}</p>

    <!-- 操作按钮 -->
    <div class="flex gap-3 mt-2 text-xs">
      <button
          v-if="userStore.isLoggedIn"
          @click="showReply = !showReply"
          class="text-gray-500 hover:text-blue-600"
      >
        {{ showReply ? '取消回复' : '回复' }}
      </button>
      <button
          v-if="canDelete"
          @click="$emit('delete', comment.id)"
          class="text-gray-500 hover:text-red-600"
      >
        删除
      </button>
    </div>

    <!-- 回复框 -->
    <div v-if="showReply" class="mt-3">
      <textarea
          v-model="replyContent"
          placeholder="写下回复..."
          rows="2"
          class="w-full border rounded p-2 text-sm resize-none"
      ></textarea>
      <button
          @click="submitReply"
          :disabled="!replyContent.trim()"
          class="mt-1 px-3 py-1 bg-blue-600 text-white text-xs rounded hover:bg-blue-700"
      >
        回复
      </button>
    </div>

    <!-- 子回复 -->
    <div v-if="comment.replies && comment.replies.length > 0" class="mt-4 space-y-4">
      <CommentItem
          v-for="reply in comment.replies"
          :key="reply.id"
          :comment="reply"
          :article-id="articleId"
          @delete="(id: number) => $emit('delete', id)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useUserStore } from '../stores/useUserStore';

const props = defineProps<{
  comment: any;
  articleId: number;
}>();

const emit = defineEmits<{
  (e: 'reply', parentId: number, content: string): void;
  (e: 'delete', commentId: number): void;
}>();

const userStore = useUserStore();
const showReply = ref(false);
const replyContent = ref('');

const canDelete = computed(() => {
  if (!userStore.user) return false;
  return userStore.user.id === props.comment.user.id || userStore.user.role === 'ADMIN';
});

const submitReply = () => {
  if (!replyContent.value.trim()) return;
  emit('reply', props.comment.id, replyContent.value);
  replyContent.value = '';
  showReply.value = false;
};

const formatDate = (dateStr: string) => {
  return new Date(dateStr).toLocaleDateString('zh-CN');
};
</script>