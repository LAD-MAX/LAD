<template>
  <div class="fixed inset-0 bg-black text-white overflow-hidden" @mousemove="onMouseMove">
    <div class="absolute inset-0 opacity-30">
      <video autoplay muted loop class="w-full h-full object-cover">
        <source src="https://lf3-h1cdn-cn.dailygn.com/obj/g-marketing-act-assets/2026_01_14_11_54_22/hgame_common_bg.mp4" type="video/mp4">
      </video>
    </div>
    <div class="absolute inset-0 bg-black/60"></div>

    <div v-if="loading" class="relative z-10 flex items-center justify-center h-full text-white/50 text-lg">加载中...</div>

    <div v-else-if="characters.length > 0" class="relative z-10 h-full flex">
      <div class="w-[40%] flex flex-col justify-center px-10 md:px-16 space-y-6">
        <div class="text-5xl md:text-7xl font-extrabold leading-tight">
          {{ currentChar.name }}
          <i class="block w-12 h-1 bg-red-600 mt-2"></i>
        </div>
        <p class="text-white/50 text-sm leading-relaxed max-w-md">{{ currentChar.description || '暂无介绍' }}</p>

        <div v-if="currentChar.skills?.[0]?.videoUrl" class="relative w-48 h-28 rounded-xl overflow-hidden cursor-pointer group" @click="playVideo(currentChar.skills[0].videoUrl)">
          <img :src="currentChar.skills[0].poster || currentChar.skills[0].icon || currentChar.avatar" class="w-full h-full object-cover" />
          <div class="absolute inset-0 bg-black/30 group-hover:bg-black/10 transition flex items-center justify-center">
            <div class="w-12 h-12 rounded-full bg-white/20 flex items-center justify-center group-hover:scale-110 transition">
              <svg class="w-6 h-6 text-white" fill="currentColor" viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
            </div>
          </div>
        </div>

        <div v-if="currentChar.skills?.length" class="space-y-2">
          <p class="text-xs text-white/30 uppercase tracking-wider">｜点击技能图标，查看技能演示</p>
          <div class="flex gap-3 flex-wrap">
            <div v-for="(skill, idx) in currentChar.skills" :key="idx"
              class="w-14 h-14 rounded-xl overflow-hidden cursor-pointer hover:ring-2 ring-red-500 transition bg-white/5"
              @click="playVideo(skill.videoUrl)" :title="skill.name + '：' + skill.desc">
              <img v-if="skill.icon" :src="skill.icon" :alt="skill.name" class="w-full h-full object-cover" />
              <div v-else class="w-full h-full flex items-center justify-center text-white/30 text-xs">{{ idx + 1 }}</div>
            </div>
          </div>
        </div>

        <router-link to="/characters/list"
          class="inline-flex items-center gap-2 px-6 py-3 bg-white/10 hover:bg-white/20 text-white rounded-full transition border border-white/20 w-fit text-sm">
          全部角色 →
        </router-link>
      </div>

      <div class="flex-1 flex items-center justify-center">
        <img
          :src="currentChar.poster || currentChar.avatar || 'https://picsum.photos/600/900'"
          :alt="currentChar.name"
          :style="{ transform: `translate(${offsetX}px, ${offsetY}px)` }"
          class="h-[60vh] md:h-[85vh] object-contain drop-shadow-2xl transition-transform duration-300 ease-out"
        />
      </div>

      <div class="w-[15%] flex flex-col items-center justify-center gap-6">
        <button @click="prev" class="w-12 h-12 rounded-full bg-white/10 hover:bg-white/20 flex items-center justify-center transition">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 15l7-7 7 7"/></svg>
        </button>
        <div class="text-sm font-mono text-white/40">{{ currentIndex + 1 }} / {{ characters.length }}</div>
        <button @click="next" class="w-12 h-12 rounded-full bg-white/10 hover:bg-white/20 flex items-center justify-center transition">
          <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"/></svg>
        </button>
      </div>
    </div>

    <div v-else class="relative z-10 flex items-center justify-center h-full text-white/50 text-lg">暂无角色</div>

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
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

interface Skill { name: string; desc: string; icon?: string; videoUrl?: string; poster?: string; }
interface Character {
  id: number; name: string; alias?: string; rarity: string; type: string;
  avatar?: string; poster?: string; description?: string; skills?: Skill[]; tierRank?: string;
}

const characters = ref<Character[]>([]);
const loading = ref(true);
const currentIndex = ref(0);
const videoUrl = ref<string | null>(null);
const offsetX = ref(0);
const offsetY = ref(0);

const currentChar = computed(() => characters.value[currentIndex.value] || characters.value[0]);

const prev = () => { currentIndex.value = currentIndex.value > 0 ? currentIndex.value - 1 : characters.value.length - 1; };
const next = () => { currentIndex.value = currentIndex.value < characters.value.length - 1 ? currentIndex.value + 1 : 0; };
const playVideo = (url?: string) => { if (url) videoUrl.value = url; };

const onMouseMove = (e: MouseEvent) => {
  offsetX.value = (e.clientX / window.innerWidth - 0.5) * 30;
  offsetY.value = (e.clientY / window.innerHeight - 0.5) * 30;
};

onMounted(async () => {
  try {
    const res = await axios.get('/api/characters', { params: { limit: 200 } });
    if (res.data.success) {
      const all = res.data.data.map((c: any) => ({
        ...c,
        skills: typeof c.skills === 'string' ? JSON.parse(c.skills) : c.skills,
      }));
      characters.value = all.filter((c: Character) => c.skills?.[0]?.videoUrl && c.skills?.[0]?.poster);
    }
  } catch (e) { console.error(e); }
  finally { loading.value = false; }
});
</script>