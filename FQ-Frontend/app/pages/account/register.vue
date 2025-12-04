<script setup lang="ts">
import {Register} from "~/scripts/account/register";

const { username, email, password, error, success, handleSubmit } = Register()
</script>
<style src="~/css/account/register.css" scoped></style>

<template>
  <div class="register-container">
    <div class="register-card">
      <h1 class="register-title">Create Account</h1>
      <p class="register-subtitle">Sign up to get started</p>

      <form @submit="handleSubmit" class="register-form">
        <div class="form-group">
          <label for="username">Username</label>
          <input
              id="username"
              v-model="username"
              type="text"
              placeholder="Enter your username"
              required
          />
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input
              id="email"
              v-model="email"
              type="email"
              placeholder="Enter your email"
              required
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
              id="password"
              v-model="password"
              type="password"
              placeholder="Enter your password"
              required
          />
        </div>

        <button type="submit" class="register-button">Sign Up</button>
      </form>

      <p v-if="error" class="message error">{{ error }}</p>
      <p v-if="success" class="message success">Account created successfully!</p>

      <div class="register-footer">
        <NuxtLink to="/account/login" class="link">Already have an account? Sign in</NuxtLink>
      </div>
    </div>
  </div>
</template>
