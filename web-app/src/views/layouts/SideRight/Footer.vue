<script setup></script>

<template>
  <div class="footer d-flex py-3 align-end">
    <div class="text-purple-accent-4 pa-2 ma-1 cursor-pointer">
      <font-awesome-icon :icon="['far', 'file']" />
      <v-tooltip activator="parent" location="bottom">
        <span class="text-12">Đính kèm file</span>
      </v-tooltip>
    </div>
    <div
      class="d-flex align-end justify-space-between px-2 py-2 bg-grey-lighten-4 rounded-xl flex-grow-1 max-w-100p"
    >
       
      <div class="text-purple-accent-4 pa-1 cursor-pointer">
        <font-awesome-icon :icon="['fas', 'face-smile']" />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Chọn biểu tượng hoặc cảm xúc</span>
        </v-tooltip>
      </div>
    </div>
    <div class="text-purple-accent-4 pa-2 ma-1 cursor-pointer">
      <font-awesome-icon :icon="['fas', 'thumbs-up']" />
      <v-tooltip activator="parent" location="bottom">
        <span class="text-12">Gửi lượt thích</span>
      </v-tooltip>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.footer {
  .chat-input {
    max-height: 100px;
    width: 90%;
    max-width: 90%;
    border: none;
    &[placeholder]:empty:before {
      content: attr(placeholder);
      color: #aaa;
    }
  }
}
</style>
