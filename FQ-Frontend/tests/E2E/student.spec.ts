// tests/E2E/student.spec.ts
import { test, expect } from '@playwright/test';

test.describe('Student tests', () => {

    // Runs before each test in this describe block
    test.beforeEach(async ({ page }) => {
        await page.goto('/account/login');

        await page.fill('input[type="email"]', 'test@example.com');
        await page.fill('input[type="password"]', 'testpassword');
        await page.click('button[type="submit"]');

        await page.waitForURL('/');

        const jwtToken = await page.evaluate(() => localStorage.getItem('accessToken'));
        const refreshToken = await page.evaluate(() => localStorage.getItem('refreshToken'));
        expect(jwtToken).toBeTruthy();
        expect(refreshToken).toBeTruthy();
    });

    test('Can Login', async ({ page }) => {
        // Already logged in, visit protected page
        await page.goto('/');
        await expect(page.locator('text=Welcome')).toBeVisible();
    });

    test('Student can enroll in a class', async ({ page }) => {
        await page.click('a.btn:has-text("Class List")');
        await page.click('a.join-link:has-text("Enroll")');

        await page.waitForURL('/');
    });

    test('Student can join a queue', async ({ page }) => {
        await page.click('a.btn:has-text("Class List")');
        await page.click('a.join-link:has-text("Enroll")');
        await page.waitForURL('/');

        await page.click('a.btn:has-text("Teachers List")');
        await page.click('a.join-link:has-text("Join Queue")');
        await page.waitForURL('/');

        await page.click('a.btn:has-text("Joined Queues")');
        await page.click('a.join-link:has-text("Go to queue")');
        await page.waitForURL(/\/queue\/queue\?queueId=\d+/);

        await expect(page.locator('.users-list')).toBeVisible();

        await expect(page.locator('.user-card').first().locator('.user-position')).toHaveText('1');
        await expect(page.locator('.user-card').first().locator('.user-name')).toBeVisible();
    });

    test('Student leave join a queue', async ({ page }) => {
        await page.click('a.btn:has-text("Class List")');
        await page.click('a.join-link:has-text("Enroll")');
        await page.waitForURL('/');

        await page.click('a.btn:has-text("Teachers List")');
        await page.click('a.join-link:has-text("Join Queue")');
        await page.waitForURL('/');

        await page.click('a.btn:has-text("Joined Queues")');
        await page.click('a.join-link:has-text("Go to queue")');
        await page.waitForURL(/\/queue\/queue\?queueId=\d+/);

        await page.click('a.leave-button:has-text("Leave Queue")');
        await page.waitForURL('/');

        await page.click('a.btn:has-text("Joined Queues")');
        await expect(page.locator('text=You are not part of any queues.')).toBeVisible();
    });

});
