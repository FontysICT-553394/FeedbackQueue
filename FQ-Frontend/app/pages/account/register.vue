<script setup lang="ts">
import {Register} from "~/scripts/account/register";

const { username, email, password, error, success, handleSubmit } = Register()
</script>

<template>
  <h1>Sign Up</h1>

  <form @submit="handleSubmit">
    <input v-model="username" type="text" placeholder="Username" />
    <input v-model="email" type="email" placeholder="Email" />
    <input v-model="password" type="password" placeholder="Password" />

    <button type="submit">Sign Up</button>
  </form>

  <p v-if="error" class="error">{{ error }}</p>
  <p v-if="success" class="success">Account created successfully!</p>
</template>
