import { ref } from 'vue'

export function Login() {
    const email = ref('')
    const password = ref('')

    const error = ref<string | null>(null)
    const success = ref(false)

    async function handleSubmit(e: Event) {
        e.preventDefault()
        error.value = null

        const config = useRuntimeConfig()
        const baseLink = config.public.baseURL ?? ''

        const {data, error: fetchError}
            = await useFetch(baseLink + "users/login", {
            method: 'POST',
            body: {
                email: email.value,
                password: password.value
            }
        })

        if (fetchError.value) {
            error.value = 'Login failed'
            console.error(fetchError.value)
        } else {
            if(data.value?.status == 200) {
                success.value = true
                localStorage.setItem('jwtToken', data.value.data)
            }else{
                error.value = data.value?.error || 'Login failed'
            }
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
