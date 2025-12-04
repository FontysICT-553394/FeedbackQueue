import { ref } from 'vue'

export function Register() {
    const { $api } = useNuxtApp()
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

        const data: any = await $api("users/register", {
            method: 'POST',
            data: {
                name: username.value,
                email: email.value,
                password: password.value,
                role: 'STUDENT'
            }
        })

        if(data.data?.status == 201) {
            success.value = true
            navigateTo('/account/login')
            console.log('Success:', data.data)
        }else{
            error.value = data.data?.error || 'Registration failed'
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
