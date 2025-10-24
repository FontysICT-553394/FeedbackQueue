<script setup lang="ts">
import {Login} from "~/scripts/account/login";

const { email, password, error, success, handleSubmit } = Login()
</script>

<template>
  <h1>Login</h1>

  <form @submit="handleSubmit">
    <input v-model="email" type="email" placeholder="Email" />
    <input v-model="password" type="password" placeholder="Password" />

    <button type="submit">Login</button>
  </form>

  <p v-if="error" class="error">{{ error }}</p>
  <p v-if="success" class="success">Logged in!</p>
</template>
