<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { inject } from "vue";

const emit = defineEmits(['hide-option']);

const { proxy } = getCurrentInstance();
const swal = inject("$swal");
const errorMsg = ref("");
const password = ref("");
const passwordConfirm = ref("");


const user = ref({
  password: "",
  email: "",
  phone: "",
  fullName: "",
  avatarUrl: "",
  roles: [],
});

onMounted(() => {
  proxy.$api
    .get("/identity/users/myInfo")
    .then((res) => {
      Object.assign(user.value, res.result);
      delete user.user_roles;
      user.value.roles = res.result.user_roles.map((role) => role.role);
    })
    .catch((error) => console.log(error));
});

async function changePassword() {
  errorMsg.value = "";
  if (password.value !== passwordConfirm.value) {
    errorMsg.value = "Mật khẩu không khớp, vui lòng nhập lại !";
    return;
  }
  if (!/^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).{8,}$/.test(password.value.trim())) {
    errorMsg.value =
      "Mật khẩu phải có ít nhất 8 ký tự chứa ít nhất một ký tự viết hoa, viết thường và chữ số";
    return;
  }

  user.value.password = password.value;

  await proxy.$api
    .put("/identity/users/" + user.value.id, user.value)
    .then((res) => {
      if (res.message) {
        errorMsg.value = res.message;
      } else {
        swal.fire({
          title: "Cập Nhật Mật Khẩu Thành Công!",
          icon: "success",
        });
        backSetting();
      }
    })
    .catch((error) => console.log(error));
}

function backSetting(){
  emit("hide-option",false);
}
</script>

<template>
  <div>
    <div class="d-flex align-center">
      <div
        class="back-chat-icon d-flex align-center pa-2 mr-1 text-grey-darken-1 cursor-pointer rounded-circle"
        @click="backSetting()"
      >
        <font-awesome-icon
          class="h-20px w-20px"
          :icon="['fas', 'arrow-left']"
        />
      </div>
      <h4>Thay đổi mật khẩu</h4>
    </div>
    <form method="PUT" @submit.prevent="changePassword()">
      <div>
        <input
          v-model.trim="password"
          class="w-100 border-b-sm py-2 pl-1 border-0 form-input text-grey-darken-4 mt-6"
          type="password"
          placeholder="Mật khẩu mới"
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Mật khẩu mới</span>
        </v-tooltip>
      </div>
      <div>
        <input
          v-model.trim="passwordConfirm"
          class="w-100 border-b-sm py-2 pl-1 border-0 form-input text-grey-darken-4 mt-6"
          type="password"
          placeholder="Nhập lại mật khẩu mới"
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Nhập lại mật khẩu mới</span>
        </v-tooltip>
      </div>
      <div class="mt-2 text-red-darken-1 text-14">
        {{ errorMsg }}
      </div>
      <button
        class="bg-purple-accent-4 py-3 px-4 rounded-lg mt-10"
        type="submit"
      >
        Lưu
      </button>
    </form>
  </div>
</template>

<style lang="scss" scoped>
</style>