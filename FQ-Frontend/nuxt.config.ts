// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  compatibilityDate: '2025-07-15',
  devtools: { enabled: true },

  runtimeConfig: {
    public: {
      baseURL: process.env.BASE_URL || 'http://localhost:8080/api/'
    }
  }
})
