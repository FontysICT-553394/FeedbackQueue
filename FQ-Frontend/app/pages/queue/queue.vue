<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {useQueueList} from "~/scripts/queue/queueList";
import {useRoute} from "vue-router";

const route = useRoute();
const { queueDetails, getQueueInformation, error, loading } = useQueueList();

onMounted(async () => {
  const queueId = route.query.queueId as string;
  if (queueId) {
    const data = await getQueueInformation(queueId);
    if (data) {
      queueDetails.value = data;
    }
  }
});
</script>
<style src="~/css/queue/queue.css" scoped></style>

<template>
  <div class="queue-detail-container">
    <div v-if="loading" class="loading">Loading queue information...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else-if="queueDetails" class="queue-content">
      <div class="queue-header">
        <h1 class="queue-title">{{ queueDetails.queueName }}</h1>
        <div class="teacher-info">
          <span class="teacher-label">Owner:</span>
          <span class="teacher-name">{{ queueDetails.teacher.name }}</span>
          <span class="teacher-role">({{ queueDetails.teacher.role }})</span>
        </div>
        <div>
          <br>
          <nuxt-link :to="`/queue/leaveQueue?queueId=${route.query.queueId}`" class="leave-button">Leave Queue</nuxt-link>
        </div>
      </div>

      <div class="queue-list-section">
        <h2 class="section-title">Students in Queue</h2>
        <div v-if="queueDetails.users.length === 0" class="empty-queue">
          No students in queue
        </div>
        <div v-else class="users-list">
          <div v-for="(user, index) in queueDetails.users" :key="user.id" class="user-card">
            <div class="user-position">{{ index + 1 }}</div>
            <div class="user-info">
              <div class="user-name">{{ user.userName }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
