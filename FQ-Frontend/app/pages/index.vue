<script setup lang="ts">
  import {GetMyAccount} from "~/scripts/index";
  const { username, role, success, handleSubmit } = GetMyAccount()

</script>

<template>
  <h1>Welcome</h1>
  <NuxtLink to="/account/register">Go to Register</NuxtLink> <br>
  <NuxtLink to="/account/login">Go to Login</NuxtLink>
  <br/>

  <form @submit="handleSubmit">
    <button type="submit">Get My Account</button>
  </form>

  <p v-if="success">Username: {{username}}</p>
  <p v-if="success">Role: {{role}}</p>

</template>
