<script setup lang="ts">
import {IsLoggedIn} from "~/scripts/account/IsLoggedIn";
import {Logout} from "~/scripts/account/logout";
import {navigateTo} from "#app";

const { isLoggedIn } = IsLoggedIn()

const handleLogout = async () => {
  await Logout()
  await navigateTo('/')
}
</script>
