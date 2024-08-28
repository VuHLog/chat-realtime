<script setup>
import { ref, getCurrentInstance, onMounted } from "vue";
import { inject } from "vue";

const emit = defineEmits(['hide-option']);

const { proxy } = getCurrentInstance();
const swal = inject("$swal");
const file = ref(null);
const errorMsg = ref("");

const user = ref({
  password: "",
  email: "",
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

async function handleFileUpload(event) {
  file.value = event.target.files[0];
  if (file.value !== null) {
    await submitFile();
  }
}

async function submitFile() {
  let formData = new FormData();

  formData.append("image", file.value);
  await proxy.$api
    .postFile("/identity/cloudinary/upload", formData)
    .then((res) => {
      user.value.avatarUrl = res.url;
      console.log(res.url);
    })
    .catch((error) => console.log(error));
}

async function updateUser() {
  errorMsg.value = "";
  if (!/^.{8,}$/.test(user.value.fullName.trim())) {
    errorMsg.value = "Họ tên phải có ít nhất 8 ký tự";
    return;
  }

  await proxy.$api
    .put("/identity/users/" + user.value.id, user.value)
    .then((res) => {
      if (res.message) {
        errorMsg.value = res.message;
      } else {
        swal.fire({
          title: "Cập Nhật Thông Tin Thành Công!",
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
      <h4>Thông tin tài khoản</h4>
    </div>
    <form method="POST" @submit.prevent="updateUser()">
      <div>
        <input
          v-model.trim="user.username"
          class="w-100 border-b-sm py-2 pl-1 border-0 form-input text-grey-darken-4 mt-6"
          type="text"
          placeholder="Tên đăng nhập"
          disabled
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Tên đăng nhập</span>
        </v-tooltip>
      </div>
      <div>
        <input
          v-model.trim="user.fullName"
          class="w-100 border-b-sm py-2 pl-1 border-0 form-input text-grey-darken-4 mt-6"
          type="text"
          placeholder="Họ tên"
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Họ tên</span>
        </v-tooltip>
      </div>
      <div>
        <input
          v-model.trim="user.email"
          class="w-100 border-b-sm py-2 pl-1 border-0 form-input text-grey-darken-4 mt-6"
          type="text"
          placeholder="Email"
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Email</span>
        </v-tooltip>
      </div>
      <div class="d-flex align-center flex-column">
        <div class="py-4">
          <img
            class="height-avatar-thumbnail width-avatar-thumbnail rounded-circle object-cover object-center"
            :src="user.avatarUrl"
            alt="avatar"
          />
        </div>
        <input
          id="input-image"
          class="d-none"
          type="file"
          accept=".jpg,.jpeg,.png"
          @change="handleFileUpload($event)"
        />
        <label
          for="input-image"
          class="pa-2 cursor-pointer rounded-sm bg-white border-sm border-solid text-grey-darken-4 box-shadow-none"
        >
          Chọn Ảnh
        </label>
        <div class="text-12 text-grey-lighten-1 mt-3">
          <p class="m-0">Dụng lượng file tối đa 1 MB</p>
          <p class="m-0">Định dạng:.JPEG, .PNG, .JPG</p>
        </div>
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

<style lang="scss" scoped></style>
