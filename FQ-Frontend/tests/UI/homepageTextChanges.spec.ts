import { test, expect } from '@playwright/test';

test.describe('Homepage text changes based on authentication state', () => {
    test('Homepage has correct text from the start', async ({ page }) => {
        await page.goto('/');
        const loginButton = page.locator('a.btn-secondary:has-text("Sign In")');
        const signupButton = page.locator('a.btn-primary:has-text("Create Account")');
        const text = page.locator('p.hero-subtitle:has-text("Enroll in a class and join your teachers queue and wait for feedback!")')

        expect(loginButton)
        expect(signupButton)
        expect(text)
    });

    test('Homepage text changes after login', async ({ page }) => {
        await page.goto('/');

        await page.evaluate(() => {
            localStorage.setItem("accessToken", "mocked");
            localStorage.setItem("refreshToken", "mocked");
        });

        await page.reload();
        const classBtn = page.locator('a.btn-secondary:has-text("Class List")');
        const teacherBtn = page.locator('a.btn-primary:has-text("Teachers List")');
        const queueBtn = page.locator('a.btn-primary:has-text("Joined Queues")');
        const text = page.locator('p.hero-subtitle').filter({ hasText: /You are enrolled in: \d+\s+Join your teachers queue and wait for feedback!/ });

        expect(teacherBtn)
        expect(classBtn)
        expect(queueBtn)
        expect(text)
    })

    test('Homepage text changes after logout', async ({ page }) => {
        await page.goto('/');

        await page.evaluate(() => {
            localStorage.setItem("accessToken", "mocked");
            localStorage.setItem("refreshToken", "mocked");
        });

        await page.goto('/');
        const classBtn = page.locator('a.btn-secondary:has-text("Class List")');
        const teacherBtn = page.locator('a.btn-primary:has-text("Teachers List")');
        const queueBtn = page.locator('a.btn-primary:has-text("Joined Queues")');
        const text = page.locator('p.hero-subtitle').filter({ hasText: /You are enrolled in: \d+\s+Join your teachers queue and wait for feedback!/ });

        expect(teacherBtn)
        expect(classBtn)
        expect(queueBtn)
        expect(text)

        await page.evaluate(() => {
            localStorage.removeItem("accessToken");
            localStorage.removeItem("refreshToken");
        });

        await page.reload();

        const loginButton = page.locator('a.btn-secondary:has-text("Sign In")');
        const signupButton = page.locator('a.btn-primary:has-text("Create Account")');
        const text2 = page.locator('p.hero-subtitle:has-text("Enroll in a class and join your teachers queue and wait for feedback!")')

        expect(loginButton)
        expect(signupButton)
        expect(text2)
    })

});