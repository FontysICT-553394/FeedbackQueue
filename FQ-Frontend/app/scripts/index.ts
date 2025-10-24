import {ref} from "vue";

export function GetMyAccount() {
    const error = ref<string | null>(null)
    const success = ref(false)
    const username = ref<string | null>(null)
    const role = ref<string | null>(null)

    async function handleSubmit(e: Event) {
        e.preventDefault()

        error.value = null

        const config = useRuntimeConfig()
        const baseLink = config.public.baseURL ?? ''

        const {data, error: fetchError}
            = await useFetch(baseLink + "users/getYourself", {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('jwtToken')}`
            }
        })

        if (fetchError.value) {
            error.value = 'Get account failed'
            console.error(fetchError.value)
        } else {

            if(data.value?.status == 200) {
                success.value = true
                username.value = data.value.data.name
                role.value = data.value.data.role
            }else{
                error.value = data.value?.error || 'Failed to get info'
            }
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
