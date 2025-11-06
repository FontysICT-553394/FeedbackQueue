import {ref} from "vue";

export function GetMyAccount() {
    const error = ref<string | null>(null)
    const success = ref(false)
    const username = ref<string | null>(null)
    const role = ref<string | null>(null)

    async function handleSubmit(e: Event) {
        e.preventDefault()

        error.value = null
        const { $api } = useNuxtApp()

        try {
            const data: any = await $api("users/getYourself")

            if(data?.status == 200) {
                success.value = true
                username.value = data.data.data.name
                role.value = data.data.data.role
            } else {
                error.value = data?.error || 'Failed to get info'
            }
        } catch (fetchError) {
            error.value = 'Get account failed'
            console.error(fetchError)
        }
    }

    return {
        error,
        success,
        username,
        role,
        handleSubmit,
    }
}
