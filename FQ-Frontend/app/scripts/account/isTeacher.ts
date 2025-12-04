export function IsTeacher() {
    const { $api } = useNuxtApp()
    const isTeacher = ref<boolean>(false)

    async function checkIfTeacher() {
        try {
            const response: any = await $api.get('users/getYourself')

            if (response?.status === 200) {
                const role = response.data?.data.role
                isTeacher.value = role?.toUpperCase() === 'TEACHER'
                return { success: true, isTeacher: isTeacher.value }
            } else {
                return { success: false, isTeacher: false }
            }
        } catch (error) {
            console.error('Error checking teacher status:', error)
            return { success: false, isTeacher: false }
        }
    }

    return {
        isTeacher,
        checkIfTeacher
    }
}
