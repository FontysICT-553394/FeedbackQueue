import { test, expect } from '@playwright/test';

test.describe('Login button changes text correctly', () => {
    test('Login button has correct text from the start', async ({ page }) => {
        await page.goto('/');
        const loginButton = page.locator('a.nav-link:has-text("Login")');

        await expect(loginButton).toHaveText('Login');
    });

    test('Login button text changes after login', async ({ page }) => {
        await page.goto('/');

        await page.evaluate(() => {
            localStorage.setItem("accessToken", "mocked");
            localStorage.setItem("refreshToken", "mocked");
        });

        await page.reload();
        const loginButton = page.locator('a.nav-link').filter({ hasText: 'Logout' });

        await expect(loginButton).toHaveText('Logout');
    })

    test('Login button text changes after logout', async ({ page }) => {
        await page.goto('/');
        await page.evaluate(() => {
            localStorage.setItem("accessToken", "mocked");
            localStorage.setItem("refreshToken", "mocked");
        });

        await page.reload();
        const loginButton = page.locator('a.nav-link').filter({ hasText: 'Logout' });
        await expect(loginButton).toHaveText('Logout');

        await page.evaluate(() => {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        });

        await page.reload();

        const loginButtonAfterLogout = page.locator('a.nav-link:has-text("Login")');
        await expect(loginButtonAfterLogout).toHaveText('Login');
    })
})