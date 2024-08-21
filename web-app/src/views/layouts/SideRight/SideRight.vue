<script setup>
import { onMounted, ref, nextTick } from "vue";
import Header from "./Header.vue";
import Footer from "./Footer.vue";

// Các tham chiếu đến các phần tử Header và Footer và body
const headerRef = ref(null);
const footerRef = ref(null);
const bodyRef = ref(null);

onMounted(async () => {
  await nextTick();
  bodyRef.value.style.height = `calc(100vh - ${
    headerRef.value.$el.offsetHeight + footerRef.value.$el.offsetHeight
  }px)`;
  console.log("bodyRef: ", bodyRef.value.offsetHeight);
});
</script>

<template>
  <div class="h-100 position-relative d-flex flex-column">
    <Header
      ref="headerRef"
      class="position-sticky top-0 right-0 left-0 z-index-99 bg-white"
    ></Header>
    <div
      ref="bodyRef"
      class="flex-grow-1 d-flex flex-column-reverse overflow-y-auto"
    >
      <div>
        <div class="text-center text-grey-darken-1 text-12 font-weight-medium">
          01:16
        </div>
        <div class="d-flex">
          <div class="d-flex max-w-40p px-4">
            <div class="d-flex align-end">
              <img
                class="width-avatar-chat height-avatar-chat rounded-circle object-cover object-center"
                src="https://res.cloudinary.com/cloud1412/image/upload/v1724172738/avatar_cv_fj5vuf.jpg"
                alt=""
              />
            </div>
            <p
              class="text-wrap w-100 bg-purple-accent-4 rounded-xl text-14 px-3 py-2 ml-2"
            >
              Cho bao nhiêu yêu thương nay bay xa Hoen đôi mi khi thu đưa em qua
              Đã từng ngọt ngào giờ nhận đắng cay vì anh Huh oh Thu vờn tóc em
              mơn man bên làn gió Vương trên mi ai lá rụng con phố nhỏ (Vương
              đâu đây chút hương hoa sữa) Nhẹ nhàng đâu đây mùi hương hoa sữa đó
              hoh yeah Nghe xung quanh âm thanh vang lên sao thân quen Trong bao
              nhiêu ngu ngơ vu vơ mùa lá vắng Thật nhẹ nhàng dù mùa thu không
              còn yêu anh nữa yeah
            </p>
          </div>
        </div>
      </div>

      <div>
        <div class="text-center text-grey-darken-1 text-12 font-weight-medium">
          01:11
        </div>
        <div class="d-flex justify-end">
          <div class="d-flex max-w-40p px-4">
            <p
              class="text-wrap w-100 bg-purple-accent-4 rounded-xl text-14 px-3 py-2 ml-2"
            >
              Cho bao nhiêu yêu thương nay bay xa Hoen đôi mi khi thu đưa em qua
              Đã từng ngọt ngào giờ nhận đắng cay vì anh Huh oh Thu vờn tóc em
              mơn man bên làn gió Vương trên mi ai lá rụng con phố nhỏ (Vương
              đâu đây chút hương hoa sữa) Nhẹ nhàng đâu đây mùi hương hoa sữa đó
              hoh yeah Nghe xung quanh âm thanh vang lên sao thân quen Trong bao
              nhiêu ngu ngơ vu vơ mùa lá vắng Thật nhẹ nhàng dù mùa thu không
              còn yêu anh nữa yeah
            </p>
          </div>
        </div>
      </div>
    </div>
    <Footer ref="footerRef" class="footer"></Footer>
  </div>
</template>

<style lang="scss" scoped></style>
