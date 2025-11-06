import { ref } from 'vue'

export function Login() {
    const { $api } = useNuxtApp()
    const email = ref('')
    const password = ref('')

    const error = ref<string | null>(null)
    const success = ref(false)

    async function handleSubmit(e: Event) {
        e.preventDefault()
        error.value = null

        try {
            const response: any = await $api.post("users/logIn", {
                email: email.value,
                password: password.value
            })

            if (response?.status === 200) {
                success.value = true

                const tokens = response.data.data

                localStorage.setItem('accessToken', tokens.accessToken)
                localStorage.setItem('refreshToken', tokens.refreshToken)
            } else {
                error.value = response?.data?.error || 'Login failed'
            }
        } catch (fetchError) {
            error.value = 'Login failed'
            console.error(fetchError)
        }
    }

    return {
        email,
        password,
        error,
        success,
        handleSubmit
    }
}
