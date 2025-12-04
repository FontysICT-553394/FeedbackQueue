<script setup lang="ts">
import {onMounted} from "vue";
import {useTeachersList} from "~/scripts/queue/teachersList";

const { teachers, error, loading, fetchTeachers } = useTeachersList();
onMounted(() => {
  fetchTeachers();
});
</script>
<style src="~/css/queue/teachersList.css" scoped></style>

<template>
  <div class="teachers-container">
    <h1 class="teachers-title">Teachers Overview</h1>
    <div v-if="loading" class="loading">Loading teachers...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="teachers-grid" style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem;">
      <div v-for="teacher in teachers" :key="teacher.id" class="teacher-card">
        <div class="teacher-name">{{ teacher.name }}</div>
        <nuxt-link :to="`/queue/joinTeacher?teacherId=${teacher.id}`" class="join-link">Join Queue</nuxt-link>
      </div>
    </div>
  </div>
</template>
