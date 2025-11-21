<script setup lang="ts">
import {IsLoggedIn} from "~/scripts/account/IsLoggedIn";
const { isLoggedIn } = IsLoggedIn()

</script>
<style src="~/css/app.css" scoped></style>

<template>
  <header class="header">
    <div class="header-content">
      <nuxt-link to="/" class="logo">Feedback Queue</nuxt-link>

      <nav v-if="!isLoggedIn" class="nav-links">
        <NuxtLink to="/account/login" class="nav-link">Login</NuxtLink>
      </nav>
      <nav v-else class="nav-links">
        <NuxtLink @click="handleLogout" class="nav-link">Logout</NuxtLink>
      </nav>

    </div>
  </header>

  <nuxt-page/>
</template>

<style>
  * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
  }

  body, html {
    margin: 0;
    padding: 0;
    width: 100%;
    height: 100%;
  }
</style>
