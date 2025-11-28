interface Queue {
    id: string | number
    name: string
}

interface QueueDetails {
    queueId: number
    queueName: string
    teacher: { role: string, id: number, name: string }
    users: Array<{ joinedAt: Record<string, any>, id: number, userName: string, userId: number }>
}

export function useQueueList() {
    const { $api } = useNuxtApp()
    const queues = ref<Array<Queue>>([])
    const queueDetails = ref<QueueDetails | null>(null)
    const error = ref<string | null>(null)
    const loading = ref(false)

    async function fetchQueues() {
        loading.value = true
        error.value = null

        try {
            const response: any = await $api.get("queue/getYourQueues")

            console.log(response)

            if (response?.status === 200) {
                const queuesData = response.data?.data || []
                queues.value = queuesData.map((queue: any) => ({
                    id: queue.id,
                    name: queue.name,
                }))
            } else {
                error.value = response?.data?.error || 'Failed to fetch queues list'
            }
            console.log(response)

        } catch (fetchError) {
            error.value = 'Failed to fetch queues list'
            console.error(fetchError)
        } finally {
            loading.value = false
        }


        return { queues, error, loading, fetchQueues }
    }

    async function getQueueInformation(queueId: string) {
        loading.value = true
        error.value = null

        try {
            const response: any = await $api.get(`queue/getQueueById/${queueId}`)
            console.log(response)

            if (response?.status === 200) {
                return response.data?.data || null
            } else {
                error.value = response?.data?.error || 'Failed to fetch queue information'
                return null
            }
        } catch (fetchError) {
            error.value = 'Failed to fetch queue information'
            console.error(fetchError)
            return null
        } finally {
            loading.value = false
        }

    }

    return { queues, queueDetails, error, loading, fetchQueues, getQueueInformation }
}
