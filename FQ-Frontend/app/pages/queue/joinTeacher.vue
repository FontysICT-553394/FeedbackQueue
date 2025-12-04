<script setup lang="ts">
  import {useJoinQueue} from "~/scripts/queue/joinTeacher";
  const route = useRoute()

  const {joinTeacher} = useJoinQueue()

  onMounted(() => {
    const teacherId = route.query.teacherId
    if (teacherId) {
      joinTeacher(teacherId)
    }
    navigateTo('/')
  })
</script>

<template>

</template>
