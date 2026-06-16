<template>
  <div class="relative w-full h-64 md:h-80 rounded-xl overflow-hidden shadow mb-10">
    <div
        v-for="(article, index) in articles"
        :key="article.id"
        class="absolute inset-0 transition-opacity duration-700"
        :class="index === current ? 'opacity-100' : 'opacity-0'"
        @click="$router.push(`/news/${article.slug}`)"
        style="cursor: pointer;"
    >
      <img
          :src="article.coverImage || 'https://picsum.photos/800/400'"
          :alt="article.title"
          class="w-full h-full object-cover"
      />
      <div class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/70 to-transparent p-6">
        <h2 class="text-white text-xl md:text-2xl font-bold">{{ article.title }}</h2>
        <p class="text-white/80 text-sm mt-1">{{ article.summary }}</p>
      </div>
    </div>

    <!-- 轮播控制点 -->
    <div class="absolute bottom-4 right-4 flex gap-2">
      <span
          v-for="(_, i) in articles"
          :key="i"
          @click.stop="current = i"
          class="w-3 h-3 rounded-full cursor-pointer"
          :class="i === current ? 'bg-white' : 'bg-white/50'"
      ></span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps<{
  articles: any[];
}>();

const current = ref(0);
let timer: any = null;

const startAutoPlay = () => {
  timer = setInterval(() => {
    current.value = (current.value + 1) % props.articles.length;
  }, 4000);
};

onMounted(() => {
  if (props.articles.length > 1) {
    startAutoPlay();
  }
});

onUnmounted(() => {
  clearInterval(timer);
});
</script>