export function classes() {
    const { $api } = useNuxtApp()
    const className = ref<string | null | undefined>(undefined)

    async function joinClassById(classId: string | number) {
        try {
            const response: any = await $api.post(`classes/joinClass/${classId}`)

            if (response?.status === 200) {
                return { success: true }
            } else {
                return { success: false, error: response?.data?.error || 'Failed to join class' }
            }
        } catch (error) {
            console.error('Error joining class:', error)
            return { success: false, error: 'Failed to join class' }
        }
    }

    async function getYourClass(){
        try{
            const response: any = await $api.get(`classes/getYourClass`)

            if(response?.status === 200){
                return {
                    success: true,
                    className: response.data.data.name,
                }
            }else{
                return {
                    success: false,
                    error: response?.data?.error || 'Failed to get your class, are you enrolled?'
                }
            }
        }catch(error){
            console.error('Error getting your class:', error)
            return {
                success: false,
                error: 'Failed to get your class'
            }
        }
    }

    return {
        className,
        getYourClass,
        joinClassById
    }

}
