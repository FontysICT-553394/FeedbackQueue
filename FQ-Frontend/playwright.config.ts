import { defineConfig, devices } from '@playwright/test';

export default defineConfig({
    testDir: './tests/',
    timeout: 15_000,
    expect: {
        timeout: 5_000,
    },

    /* Run tests in parallel */
    fullyParallel: true,

    /* Fail the build on CI if test.only is left in the code */
    forbidOnly: !!process.env.CI,

    /* Retry on CI only */
    retries: process.env.CI ? 2 : 0,

    /* Reporter to use */
    reporter: 'html',

    /* Shared settings for all projects */
    use: {
        baseURL: 'http://localhost:3000',
        headless: true,
        trace: 'on-first-retry',
        screenshot: 'only-on-failure',
        video: 'retain-on-failure',
    },

    /* Test against different browsers */
    projects: [
        // { Can't test because of an Linux Arch installation (Playwright does not support certain libraries)
        //     name: 'Chromium',
        //     use: { ...devices['Desktop Chrome'] },
        // },
        {
            name: 'Firefox',
            use: { ...devices['Desktop Firefox'] },
        },
        // { Can't test because of an Linux Arch installation (Playwright does not support)
        //     name: 'WebKit',
        //     use: { ...devices['Desktop Safari'] },
        // },

        // ---- Setup projects ----
        {
            name: 'setup-student',
            testMatch: /auth\.student\.setup\.ts/,
        },
    ],
});
