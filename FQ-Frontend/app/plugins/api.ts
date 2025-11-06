import axios from 'axios'

export default defineNuxtPlugin(() => {
    const url = useRuntimeConfig().public.baseURL
    const api = axios.create({
        baseURL: useRuntimeConfig().public.baseURL,
    })

    api.interceptors.request.use(config => {
        const token = localStorage.getItem('accessToken')
        if (token) config.headers.Authorization = `Bearer ${token}`
        return config
    })

    api.interceptors.response.use(
        res => res,
        async error => {
            const originalRequest = error.config

            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true
                const refreshToken = localStorage.getItem('refreshToken')
                const res = await axios.post(url +'/users/refresh', { refreshToken })

                localStorage.setItem('accessToken', res.data.data.accessToken)
                localStorage.setItem('refreshToken', res.data.data.refreshToken)
                console.log("refreshed tokens")
                originalRequest.headers.Authorization = `Bearer ${res.data.accessToken}`
                console.log("retried original request")
                return api(originalRequest)
            }

            return Promise.reject(error)
        }
    )

    return { provide: { api } }
})
