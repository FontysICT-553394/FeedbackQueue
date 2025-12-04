interface Teacher {
    id: string | number
    name: string
    role: string
}

export function useTeachersList() {
    const { $api } = useNuxtApp()
    const teachers = ref<Array<Teacher>>([])
    const error = ref<string | null>(null)
    const loading = ref(false)

    async function fetchTeachers() {
        loading.value = true
        error.value = null

        try {
            const response: any = await $api.get("users/getAllYourTeachers")

            console.log(response)

            if (response?.status === 200) {
                const teachersData = response.data?.data || []
                teachers.value = teachersData.map((teacher: any) => ({
                    id: teacher.id,
                    name: teacher.name,
                    role: teacher.role
                }))
            } else {
                error.value = response?.data?.error || 'Failed to fetch teachers list'
            }
        } catch (fetchError) {
            error.value = 'Failed to fetch teachers list'
            console.error(fetchError)
        } finally {
            loading.value = false
        }

        return { teachers, error, loading }
    }

    return {
        teachers,
        error,
        loading,
        fetchTeachers
    }
}
