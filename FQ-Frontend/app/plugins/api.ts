import axios from 'axios'

export default defineNuxtPlugin(() => {
    const url = useRuntimeConfig().public.baseURL
    const api = axios.create({
        baseURL: useRuntimeConfig().public.baseURL,
    })

    api.interceptors.request.use(config => {
        if (import.meta.client) {
            const token = localStorage.getItem('accessToken')
            if (token) {
                config.headers.Authorization = `Bearer ${token}`
            }
        }
        return config
    })

    api.interceptors.response.use(
        res => res,
        async error => {
            const originalRequest = error.config

            if (error.response?.status === 401 && !originalRequest._retry && import.meta.client) {
                originalRequest._retry = true
                const refreshToken = localStorage.getItem('refreshToken')

                if (!refreshToken) {
                    navigateTo('/account/login')
                    return Promise.reject(error)
                }

                try {
                    const res = await axios.post(url + '/users/refresh', { refreshToken })

                    if (res.data?.data?.accessToken && res.data?.data?.refreshToken) {
                        localStorage.setItem('accessToken', res.data.data.accessToken)
                        localStorage.setItem('refreshToken', res.data.data.refreshToken)
                        console.log("refreshed tokens")
                        originalRequest.headers.Authorization = `Bearer ${res.data.data.accessToken}`
                        console.log("retried original request")
                        return api(originalRequest)
                    } else {
                        throw new Error('Invalid token response')
                    }
                } catch (refreshError) {
                    localStorage.removeItem('accessToken')
                    localStorage.removeItem('refreshToken')
                    navigateTo('/account/login')
                    return Promise.reject(refreshError)
                }
            }

            return Promise.reject(error)
        }
    )

    return { provide: { api } }
})
