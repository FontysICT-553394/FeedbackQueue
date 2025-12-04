export function useJoinQueue() {
    const { $api } = useNuxtApp()

    async function joinTeacher(teacherId: string | number) {
        try {
            const response: any = await $api.post(`queue/join/${teacherId}`)

            if (response?.status === 200) {
                return { success: true }
            } else {
                return { success: false, error: response?.data?.error || 'Failed to join queue' }
            }
        } catch (error) {
            console.error('Error joining queue:', error)
            return { success: false, error: 'Failed to join queue' }
        }
    }

    return {
        joinTeacher
    }
}
