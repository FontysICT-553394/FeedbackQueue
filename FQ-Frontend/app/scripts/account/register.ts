import { ref } from 'vue'

export function Register() {
    const username = ref('')
    const email = ref('')
    const password = ref('')

    const error = ref<string | null>(null)
    const success = ref(false)

    async function handleSubmit(e: Event) {
        e.preventDefault()
        error.value = null

        const config = useRuntimeConfig()
        const baseLink = config.public.baseURL ?? ''

        const { data, error: fetchError }
            = await useFetch(baseLink + "users/register", {
            method: 'POST',
            body: {
                name: username.value,
                email: email.value,
                password: password.value,
                role: 'STUDENT'
            }
        })

        if (fetchError.value) {
            error.value = 'Registration failed'
            console.error(fetchError.value)
        } else {
            if(data.value?.status == 200) {
                success.value = true
                console.log('Success:', data.value)
            }else{
                error.value = data.value?.error || 'Registration failed'
            }
        }
    }

    return {
        username,
        email,
        password,
        error,
        success,
        handleSubmit
    }
}
