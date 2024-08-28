<script setup>
import { onMounted, getCurrentInstance } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const { proxy } = getCurrentInstance();
onMounted(async () => {
  await proxy.$api
    .get("/chat/conversations/latest")
    .then((res) => router.push("/messages/" + res.result.id));
});
</script>

<template>
  <div></div>
</template>

<style lang="scss" scoped></style>
