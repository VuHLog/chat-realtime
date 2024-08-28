<script setup>
import { ref,getCurrentInstance } from "vue";
import { useRouter } from "vue-router";
import{ useBaseStore} from "@/store/index.js"
import { jwtDecode } from "jwt-decode";
import TokenService from "@/service/TokenService.js";
import profile from "./Profile.vue";
import password from "./Password.vue";

const {proxy} = getCurrentInstance();
const router = useRouter();
const store = useBaseStore();

async function logOut() {
  const token = TokenService.getLocalAccessToken();
  if (token) {
    await proxy.$api
      .post("/identity/auth/logout", token)
      .then(() => {
        store.isLoggedIn = false;
        TokenService.removeLocalAccessToken();
        router.push("/auth/sign-in");
      })
      .catch();
  }
}

const showOption = ref(true);
const selectedOption = ref("");
function selectOption(option) {
  showOption.value = false;
  selectedOption.value = option;
}
</script>

<template>
  <div class="d-flex flex-column">
    <transition name="slide-fade" mode="out-in">
      <div class="mt-7 setting" v-if="showOption">
        <ul class="p-0 m-0 setting-list">
          <li class="setting-item" @click="selectOption('profile')">
            <div
              class="text-grey-darken-4 rounded-xl py-2 d-inline-block w-100 user-none cursor-pointer"
            >
              <font-awesome-icon class="mr-3 w-20px" :icon="['far', 'user']" />
              <span>Tài Khoản Của Tôi</span>
            </div>
          </li>
          <li class="setting-item" @click="selectOption('change-password')">
            <div
              class="text-grey-darken-4 rounded-xl py-2 d-inline-block w-100 user-none cursor-pointer"
            >
              <font-awesome-icon class="mr-3 w-20px" :icon="['fas', 'key']" />
              <span>Thay Đổi Mật Khẩu</span>
            </div>
          </li>
          <li class="setting-item" @click="logOut()">
            <div
              class="text-grey-darken-4 d-inline-block w-100 rounded-xl py-2 user-none cursor-pointer"
            >
              <font-awesome-icon
                class="mr-3 w-20px"
                :icon="['fas', 'right-from-bracket']"
              />
              <span>Đăng xuất</span>
            </div>
          </li>
        </ul>
      </div>
      <profile v-else-if="selectedOption === 'profile'" @hide-option="() =>{showOption = true; selectedOption = ''}"></profile>
      <password v-else-if="selectedOption === 'change-password'" @hide-option="() =>{showOption = true; selectedOption = ''}"></password>
    </transition>

  </div>
</template>

<style lang="scss" scoped>
.setting {
  .setting-list {
    .setting-item {
      &:hover {
        background-color: #e0e0e0;
      }
    }
  }
}

.slide-fade-enter-active {
  transition: all 0.1s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.1s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-20px);
  opacity: 0;
}
</style>
