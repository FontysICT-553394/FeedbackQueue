<script setup lang="ts">
  import {classes} from "~/scripts/queue/classes";
  const { joinClassById } = classes()
  const route = useRoute()

  onMounted(() => {
    const classId = route.query.classId
    if (classId) {
      joinClassById(classId)
    }
    navigateTo('/')
  })
</script>

<template>

</template>
