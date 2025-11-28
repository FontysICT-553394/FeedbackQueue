<script setup lang="ts">
import {useLeaveQueue} from "~/scripts/queue/leaveQueue";
const route = useRoute()

const {leaveQueue} = useLeaveQueue()

onMounted(() => {
  const queueId = route.query.queueId as string
  if (queueId) {
    leaveQueue(queueId)
  }
  navigateTo('/')
})
</script>
