<script setup lang="ts">
  import {onMounted} from "vue";
  import {useClassList} from "~/scripts/queue/classList";

  const { classes, error, loading, fetchClasses } = useClassList();
  onMounted(() => {
    fetchClasses();
  });
</script>
<style src="~/css/queue/classList.css" scoped></style>

<template>
  <div class="class-container">
    <h1 class="class-title">Class List</h1>
    <div v-if="loading" class="loading">Loading classes...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="classes-grid" style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem;">
      <div v-for="c in classes" :key="c.id" class="class-card">
        <div class="class-name">{{ c.name }}</div>
        <nuxt-link :to="`/queue/joinClass?classId=${c.id}`" class="join-link">Enroll</nuxt-link>
      </div>
    </div>
  </div>
</template>
