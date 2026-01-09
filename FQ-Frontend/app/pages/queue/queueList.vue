<script setup lang="ts">
import {onMounted} from "vue";
import {useQueueList} from "~/scripts/queue/queueList";

const { queues, error, loading, fetchQueues } = useQueueList();
onMounted(() => {
  fetchQueues();
});
</script>
<style src="~/css/queue/queueList.css" scoped></style>

<template>
  <div class="queue-container">
    <h1 class="queue-title">Queue Overview</h1>
    <div v-if="loading" class="loading">Loading your queues...</div>
    <div v-else-if="error" class="error">{{ error }}</div>
    <div v-else class="queue-grid" style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 1rem;">

      <div v-if="queues.length == 0">
        <p class="no-queues">You are not part of any queues.</p>
      </div>

      <div v-for="queue in queues" :key="queue.id" class="queue-card">
        <div class="queue-name">{{ queue.name }}</div>
        <nuxt-link :to="`/queue/queue?queueId=${queue.id}`" class="join-link">Go to queue</nuxt-link>
      </div>
    </div>
  </div>
</template>
