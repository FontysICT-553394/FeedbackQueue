export function useLeaveQueue() {
    const { $api } = useNuxtApp()

    async function leaveQueue(queueId: string | number) {
        try {
            const response: any = await $api.post(`queue/leaveByQueueId/${queueId}`)

            if (response?.status === 200) {
                return { success: true }
            } else {
                return { success: false, error: response?.data?.error || 'Failed to leave queue' }
            }
        } catch (error) {
            console.error('Error leaving queue:', error)
            return { success: false, error: 'Failed to leave queue' }
        }
    }

    return {
        leaveQueue,
    }
}
