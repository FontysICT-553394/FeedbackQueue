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

        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')

        try {
            const response: any = await $api.post("users/logIn", {
                email: email.value,
                password: password.value
            })

            if (response?.status === 200) {
                const tokens = response.data.data

                if (tokens?.accessToken && tokens?.refreshToken) {
                    localStorage.setItem('accessToken', tokens.accessToken)
                    localStorage.setItem('refreshToken', tokens.refreshToken)

                    success.value = true
                    await navigateTo('/')
                } else {
                    error.value = response?.data?.error || 'Login failed: Missing tokens'
                    success.value = false
                }
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
