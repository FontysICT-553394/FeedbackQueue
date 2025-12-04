interface Class {
    id: string | number
    name: string
}

export function useClassList() {
    const { $api } = useNuxtApp()
    const classes = ref<Array<Class>>([])
    const error = ref<string | null>(null)
    const loading = ref(false)

    async function fetchClasses() {
        loading.value = true
        error.value = null

        try {
            const response: any = await $api.get("classes/getAll")

            console.log(response)

            if (response?.status === 200) {
                const teachersData = response.data?.data || []
                classes.value = teachersData.map((c: any) => ({
                    id: c.id,
                    name: c.name,
                }))
            } else {
                error.value = response?.data?.error || 'Failed to fetch class list'
            }
        } catch (fetchError) {
            error.value = 'Failed to fetch class list'
            console.error(fetchError)
        } finally {
            loading.value = false
        }

        return { classes, error, loading }
    }

    return {
        classes,
        error,
        loading,
        fetchClasses
    }
}
