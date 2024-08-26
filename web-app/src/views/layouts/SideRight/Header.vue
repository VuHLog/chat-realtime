<script setup>
const props = defineProps({
  receiver:{
    type: Object,
    required: true,
  }
})
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
    </div>
    <div class="d-flex align-center text-purple-accent-4">
        <div class=" cursor-pointer">
            <font-awesome-icon :icon="['fas', 'phone']" />
        </div>
        <div class="ml-5 cursor-pointer">
            <font-awesome-icon :icon="['fas', 'video']" />
        </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.header{
    box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
}
</style>
