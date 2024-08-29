<script setup>
const props = defineProps({
  receiver: {
    type: Object,
    required: true,
  },
  isFriend: {
    type: Boolean,
    required: true,
  },
});

import { ref, getCurrentInstance, inject } from "vue";
import FriendRequestsStatus from "@/constants/FriendRequestsStatus.js";

const emit = defineEmits(["unfriend"])
const swal = inject("$swal");
const {proxy} = getCurrentInstance();

function unFriend() {
  swal
    .fire({
      title: "Bạn có muốn hủy kết bạn?",
      showCancelButton: true,
      confirmButtonText: "Có",
      showCancelButton: true,
      cancelButtonText: "Không"
    })
    .then((result) => {
      /* Read more about isConfirmed, isDenied below */
      if (result.isConfirmed) {
        proxy.$api.delete("/friend/"+props.receiver.id).then(()=>{
          emit("unfriend",FriendRequestsStatus.FRIEND_REQUEST_NOT_EXISTED )
        })
      }
    });
}
</script>

<template>
  <div class="header px-4 py-3 d-flex justify-space-between border-b-sm">
    <div class="d-flex align-center">
      <div class="d-flex align-center">
        <img
          class="height-avatar-thumbnail width-avatar-thumbnail rounded-circle object-cover object-center"
          :src="props.receiver?.avatarUrl"
          alt=""
        />
      </div>
      <div class="ml-2 font-weight-bold">{{ props.receiver?.fullName }}</div>
      <div
        v-if="props.isFriend"
        class="text-purple-accent-4 ml-2 cursor-pointer"
        @click="unFriend()"
      >
        <font-awesome-icon :icon="['fas', 'user-xmark']" />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Hủy kết bạn</span>
        </v-tooltip>
      </div>
    </div>
    <div class="d-flex align-center text-purple-accent-4">
      <div class="cursor-pointer">
        <font-awesome-icon :icon="['fas', 'phone']" />
      </div>
      <div class="ml-5 cursor-pointer">
        <font-awesome-icon :icon="['fas', 'video']" />
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.header {
  box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
}
</style>
