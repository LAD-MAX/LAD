<template>
  <div class="fixed inset-0 bg-black text-white overflow-hidden" @mousemove="onMouseMove">
    <!-- 背景 -->
    <div class="absolute inset-0 opacity-30">
      <video autoplay muted loop class="w-full h-full object-cover">
        <source src="https://lf3-h1cdn-cn.dailygn.com/obj/g-marketing-act-assets/2026_01_14_11_54_22/hgame_common_bg.mp4" type="video/mp4">
      </video>
    </div>
    <div class="absolute inset-0 bg-black/60"></div>

    <div v-if="loading" class="relative z-10 flex items-center justify-center h-full text-white/50 text-lg">加载中...</div>

    <div v-else-if="character" class="relative z-10 h-full flex">
      <!-- 左侧：信息区 -->
      <div class="w-[40%] flex flex-col justify-center px-10 md:px-16 space-y-6">
        <router-link to="/characters/list" class="inline-flex items-center gap-2 text-white/50 hover:text-white text-sm transition">
          <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7"/></svg>
          返回角色列表
        </router-link>

        <div class="text-5xl md:text-7xl font-extrabold leading-tight">
          {{ character.name }}
          <i class="block w-12 h-1 bg-red-600 mt-2"></i>
        </div>

        <div class="flex items-center gap-2">
          <span class="px-3 py-1 text-xs font-bold rounded-full" :class="rarityColors[character.rarity] || 'bg-gray-500'">
            {{ rarityLabels[character.rarity] || character.rarity }}
          </span>
        </div>

        <p class="text-white/50 text-sm leading-relaxed max-w-md">{{ character.description || '暂无介绍' }}</p>

        <!-- 技能列表 -->
        <div v-if="character.skills?.length" class="space-y-2">
          <p class="text-xs text-white/30 uppercase tracking-wider">｜点击技能图标，查看技能演示</p>
          <div class="flex gap-3 flex-wrap">
            <div v-for="(skill, idx) in character.skills" :key="idx"
              class="w-14 h-14 rounded-xl overflow-hidden cursor-pointer hover:ring-2 ring-red-500 transition bg-white/5"
              @click="openVideo(skill.videoUrl)" :title="skill.name + '：' + skill.desc">
              <img v-if="skill.icon" :src="skill.icon" :alt="skill.name" class="w-full h-full object-cover" />
              <div v-else class="w-full h-full flex items-center justify-center text-white/30 text-xs">{{ idx + 1 }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间：立绘 + 背后放大虚影 -->
      <div class="flex-1 flex items-center justify-center relative">
        <!-- 虚影 -->
        <img
          :src="character.poster || character.avatar || 'https://picsum.photos/600/900'"
          class="absolute h-[85vh] md:h-[100vh] object-contain opacity-10 blur-xl scale-110 pointer-events-none"
          :style="{ transform: `translate(${offsetX * 0.5}px, ${offsetY * 0.5}px)` }"
        />
        <!-- 主立绘 -->
        <img
          :src="character.poster || character.avatar || 'https://picsum.photos/600/900'"
          :style="{ transform: `translate(${offsetX}px, ${offsetY}px)` }"
          class="relative z-10 h-[60vh] md:h-[85vh] object-contain drop-shadow-2xl transition-transform duration-300 ease-out"
        />
      </div>
    </div>

    <!-- 视频弹窗 -->
    <Teleport to="body">
      <div v-if="videoUrl" class="fixed inset-0 z-[60] flex items-center justify-center bg-black/90 p-4" @click.self="videoUrl = null">
        <button @click="videoUrl = null" class="absolute top-4 right-4 w-10 h-10 rounded-full bg-white/20 flex items-center justify-center hover:bg-red-500 transition z-10">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path d="M6 18L18 6M6 6l12 12"/></svg>
        </button>
        <video :src="videoUrl" controls autoplay class="max-w-full max-h-[80vh] rounded-xl" />
      </div>
    </Teleport>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const id = route.params.id as string;

interface Skill { name: string; desc: string; icon?: string; videoUrl?: string; }
interface Character {
  id: number; name: string; rarity: string; avatar?: string; poster?: string;
  description?: string; skills?: Skill[];
}

const character = ref<Character | null>(null);
const loading = ref(true);
const videoUrl = ref<string | null>(null);
const offsetX = ref(0);
const offsetY = ref(0);

const rarityColors: Record<string, string> = {
  NORMAL: 'bg-green-600', EXCELLENT: 'bg-blue-600', SUPERIOR: 'bg-purple-600',
  LEGENDARY: 'bg-orange-600', MYTHIC: 'bg-yellow-600', COLLECTOR: 'bg-cyan-600',
};
const rarityLabels: Record<string, string> = {
  NORMAL: '普通', EXCELLENT: '优异', SUPERIOR: '卓越', LEGENDARY: '超凡', MYTHIC: '传奇', COLLECTOR: '典藏',
};

const openVideo = (url?: string) => { if (url) videoUrl.value = url; };
const onMouseMove = (e: MouseEvent) => {
  offsetX.value = (e.clientX / window.innerWidth - 0.5) * 30;
  offsetY.value = (e.clientY / window.innerHeight - 0.5) * 30;
};

const fetchCharacter = async (characterId: number) => {
  loading.value = true;
  try {
    const res = await axios.get(`/api/characters/${characterId}`);
    if (res.data.success) {
      const data = res.data.data;
      if (typeof data.skills === 'string') data.skills = JSON.parse(data.skills);
      character.value = data;
    }
  } catch (e) { console.error(e); }
  finally { loading.value = false; }
};

watch(() => route.params.id, (newId) => { if (newId) fetchCharacter(Number(newId)); });
onMounted(() => { if (id) fetchCharacter(Number(id)); });
</script>