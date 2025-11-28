<script setup lang="ts">
import {IsLoggedIn} from "~/scripts/account/IsLoggedIn";
import {logout} from "~/scripts/account/logout";
import {navigateTo} from "#app";

const { isLoggedIn } = IsLoggedIn()

const handleLogout = async () => {
  await logout()
  await navigateTo('/')
}
</script>
