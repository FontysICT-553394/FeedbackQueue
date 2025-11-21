<script setup lang="ts">
import {Login} from "~/scripts/account/login";

const { email, password, error, success, handleSubmit } = Login()
</script>
<style src="~/css/account/login.css" scoped></style>

<template>
  <div class="login-container">
    <div class="login-card">
      <h1 class="login-title">Welcome Back</h1>
      <p class="login-subtitle">Sign in to your account</p>

      <form @submit="handleSubmit" class="login-form">
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

        <button type="submit" class="login-button">Sign In</button>
      </form>

      <p v-if="error" class="message error">{{ error }}</p>
      <p v-if="success" class="message success">Logged in successfully!</p>

      <div class="login-footer">
        <NuxtLink to="/account/register" class="link">Don't have an account? Register</NuxtLink>
        <NuxtLink to="/" class="link">Back to Home</NuxtLink>
      </div>
    </div>
  </div>
</template>
