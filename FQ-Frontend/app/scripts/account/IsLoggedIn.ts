export function IsLoggedIn() {
    if (typeof window === 'undefined') {
        return { isLoggedIn: false };
    }

    const accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');

    return { isLoggedIn: !!(accessToken && refreshToken) };
}
