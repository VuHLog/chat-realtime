<script setup></script>

<template>
  <div class="d-flex flex-column max-h-100vh h-100 px-2 border-e-sm">
    <div>
      <h2 class="font-weight-bold py-3 px-1">Đoạn chat</h2>
      <div class="d-flex px-2 py-2 bg-grey-lighten-4 rounded-xl">
        <label for="input-search" class="mr-2">
          <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
        </label>
        <div class="w-100 user-select-none">
          <input
            id="input-search"
            class="outline-none w-100"
            type="text"
            placeholder="Tìm kiếm"
          />
        </div>
      </div>
    </div>

    <div class="mt-2 h-100 overflow-y-auto">
      <div
        class="d-flex align-center px-1 py-2 cursor-pointer chat-box rounded user-select-none position-relative active"
      >
        <div class="d-flex align-center">
          <img
            class="width-avatar height-avatar rounded-circle object-cover object-center"
            src="https://res.cloudinary.com/cloud1412/image/upload/v1724172738/avatar_cv_fj5vuf.jpg"
            alt=""
          />
        </div>
        <div class="d-flex flex-column ml-2 justify-center">
          <div class="text-14 font-weight-bold">Long Hoàng</div>
          <div class="text-12 text-grey-darken-1">
            <span>Long đã gửi một nhãn dán</span>
            <span class="ml-2">3 giờ</span>
          </div>
        </div>
        <div
          class="position-absolute rounded-circle bg-white chat-box__options d-none align-center justify-center"
        >
          <font-awesome-icon :icon="['fas', 'ellipsis']" />
        </div>
      </div>

      <div
        class="d-flex align-center px-1 py-2 cursor-pointer chat-box rounded user-select-none position-relative"
      >
        <div class="d-flex align-center">
          <img
            class="width-avatar height-avatar rounded-circle object-cover object-center"
            src="https://res.cloudinary.com/cloud1412/image/upload/v1724172738/avatar_cv_fj5vuf.jpg"
            alt=""
          />
        </div>
        <div class="d-flex flex-column ml-2 justify-center">
          <div class="text-14 font-weight-bold">18 gà</div>
          <div class="text-12 text-grey-darken-1">
            <span>Chicken</span>
            <span class="ml-2">3 giờ</span>
          </div>
        </div>
        <div
          class="position-absolute rounded-circle bg-white chat-box__options d-none align-center justify-center"
        >
          <font-awesome-icon :icon="['fas', 'ellipsis']" />
        </div>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.chat-box {
  &:hover {
    background-color: #f5f5f5;
  }

  &.active {
    background-color: #e1bee7;
  }

  &:hover .chat-box__options {
    display: flex !important;
  }

  .chat-box__options {
    height: 36px;
    width: 36px;
    right: 10%;
  }
}
</style>
