export const useQueueEvents = (queueId: Ref<string | null>) => {
    const eventSource = ref<EventSource | null>(null);
    const isConnected = ref(false);
    const apiUrl = useRuntimeConfig().public.baseURL


    const connectQueueEvent = () => {
        if (!queueId.value) return;

        eventSource.value = new EventSource(`${apiUrl}queue/events/${queueId.value}`);

        eventSource.value.onopen = () => {
            isConnected.value = true;
            console.log('SSE connection established');
        };

        eventSource.value.onmessage = (event) => {
            try {
                const data = JSON.parse(event.data);
                handleQueueEvent(data);
            } catch (error) {
                console.error('Error parsing SSE event:', error);
            }
        };

        eventSource.value.onerror = () => {
            isConnected.value = false;
            console.error('SSE connection error');
        };
    };

    const handleQueueEvent = (data: { type: string; data: any }) => {
        if (data.type === 'user_joined_queue') {
            console.log('User joined:', data.data);
            window.location.reload();
        } else if (data.type === 'user_left_queue') {
            console.log('User left:', data.data);
            window.location.reload();
        }
    };

    const disconnectQueueEvent = () => {
        if (eventSource.value) {
            eventSource.value.close();
            eventSource.value = null;
            isConnected.value = false;
        }
    };

    onUnmounted(() => {
        disconnectQueueEvent();
    });

    return {
        connectQueueEvent,
        disconnectQueueEvent,
        isConnected
    };
};