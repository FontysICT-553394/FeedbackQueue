<script setup lang="ts">
import {GetMyAccount} from "~/scripts/index";
import {IsLoggedIn} from "~/scripts/account/IsLoggedIn";
import { IsTeacher } from "~/scripts/account/isTeacher";
import {classes} from "~/scripts/queue/classes";

const { username, role, success, handleSubmit } = GetMyAccount()
const { isLoggedIn } = IsLoggedIn()
const { isTeacher, checkIfTeacher } = IsTeacher()
const { className, getYourClass } = classes()

onMounted(async() => {
  await checkIfTeacher()

  console.log(isTeacher)
  if(!isTeacher) {
    const result = await getYourClass()
    if (result.success) {
      className.value = result.className
    } else {
      className.value = null
    }
  }
})

</script>
<style src="~/css/index.css" scoped></style>

<template>
  <main class="main-content">
    <section class="hero-section">
      <h1 class="hero-title">Welcome to the Feedback Queue</h1>

      <p v-if="success === false" class="hero-subtitle">Enroll in a class and join your teachers queue and wait for feedback!</p>
      <p v-else class="hero-subtitle">
        You are enrolled in: {{ className }} <br>
        Join your teachers queue and wait for feedback!
      </p>


      <div v-if="!isLoggedIn" class="action-buttons">
        <NuxtLink to="/account/register" class="btn btn-primary">Create Account</NuxtLink>
        <NuxtLink to="/account/login" class="btn btn-secondary">Sign In</NuxtLink>
      </div>
      <div v-else class="action-buttons">
        <div v-if="!isTeacher">
          <NuxtLink to="/queue/teachersList" class="btn btn-primary">Teachers List</NuxtLink>
          <NuxtLink to="/queue/queueList" class="btn btn-primary">Joined Queues</NuxtLink>
          <NuxtLink to="/queue/classList" class="btn btn-secondary">Class List</NuxtLink>
        </div>
        <div v-else>
          <NuxtLink to="/queue/queueList" class="btn btn-primary">Your Queues</NuxtLink>
          <NuxtLink to="/queue/classList" class="btn btn-secondary">Your Classes</NuxtLink>
        </div>
      </div>

    </section>

    <section class="account-section">
      <h2 class="section-title">My Account</h2>
      <form @submit="handleSubmit" class="account-form">
        <button type="submit" class="btn btn-primary">View Account Details</button>
      </form>

      <div v-if="success" class="account-info">
        <div class="info-item">
          <span class="info-label">Username:</span>
          <span>{{ username }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">Role:</span>
          <span>{{ role }}</span>
        </div>
      </div>
    </section>
  </main>
</template>
