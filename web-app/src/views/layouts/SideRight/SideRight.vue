<script setup>
import { onMounted, onBeforeUnmount, ref, watch } from "vue";

import { Client } from "@stomp/stompjs";

import Header from "./Header.vue";
import { useBaseStore } from "@/store/index.js";

const store = useBaseStore();

// Các tham chiếu đến các phần tử Header và Footer và body
const headerRef = ref(null);
const footerRef = ref(null);
const bodyRef = ref(null);

onMounted(async () => {
  bodyRef.value.style.height = `calc(100vh - ${
    headerRef.value.offsetHeight + footerRef.value.offsetHeight
  }px)`;
  console.log("bodyRef: ", bodyRef.value.offsetHeight);

  await connect();
});

onBeforeUnmount(()=>{
  disconnect();
})

const messageText = ref("");
const messages = ref([]);
const showSentIcon = ref(false);
watch(messageText, (newVal) => {
  if (newVal !== "") {
    showSentIcon.value = true;
  }
});

// xu ly nhan Enter trong textarea
function handlePressEnterTextArea(event) {
  if (event.key !== "Enter") return;
  else if (event.shiftKey) return;

  event.preventDefault();
  sendMessage();
}

const stompClient = new Client({
  brokerURL: "ws://localhost:8081/chat/ws",
  onConnect: (frame) => {
    console.log("Connected: " + frame);
    stompClient.subscribe("/topic/conversations/1", (response) => {
      messages.value.unshift(JSON.parse(response.body));
    });
  },
  onWebSocketError: (error) => {
    console.error("Error with websocket", error);
  },
  onStompError: (frame) => {
    console.error("Broker reported error: " + frame.headers["message"]);
    console.error("Additional details: " + frame.body);
  },
});

async function connect() {
  stompClient.activate();
}

function disconnect() {
  stompClient.deactivate();
  console.log("Disconnected");
}

async function sendMessage() {
  stompClient.publish({
    destination: "/app/sendMessage",
    body: JSON.stringify({
      content: messageText.value,
      contentType: "text",
      conversationId: 1,
    }),
  });
  messageText.value = "";
}

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
      <template v-for="message in messages" :key="message.id">
        <div>
          <div
            class="text-center text-grey-darken-1 text-12 font-weight-medium"
          >
            {{ message.timeSent }}
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
                {{ message.content }}
              </p>
            </div>
          </div>
        </div>
      </template>
    </div>
    <footer ref="footerRef" class="footer d-flex py-3 align-center">
      <div class="text-purple-accent-4 pa-2 ma-1 cursor-pointer">
        <font-awesome-icon :icon="['far', 'file']" />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">Đính kèm file</span>
        </v-tooltip>
      </div>
      <div
        class="d-flex align-center px-2 bg-grey-lighten-4 rounded-xl flex-grow-1"
      >
        <textarea
          name=""
          id=""
          v-model.trim="messageText"
          @keydown="handlePressEnterTextArea"
          rows="2"
          class="d-flex align-center outline-none border-0 flex-grow-1 ml-2"
          placeholder="Aa"
          style="resize: none"
        ></textarea>
        <div class="text-purple-accent-4 pa-1 cursor-pointer">
          <font-awesome-icon :icon="['fas', 'face-smile']" />
          <v-tooltip activator="parent" location="bottom">
            <span class="text-12">Chọn biểu tượng hoặc cảm xúc</span>
          </v-tooltip>
        </div>
      </div>
      <div class="text-purple-accent-4 pa-2 ma-1 cursor-pointer">
        <div v-if="!showSentIcon">
          <font-awesome-icon :icon="['fas', 'thumbs-up']" />
          <v-tooltip activator="parent" location="bottom">
            <span class="text-12">Gửi lượt thích</span>
          </v-tooltip>
        </div>
        <div v-else @click="sendMessage()">
          <font-awesome-icon :icon="['fas', 'paper-plane']" />
          <v-tooltip activator="parent" location="bottom">
            <span class="text-12">Nhấn Enter để gửi</span>
          </v-tooltip>
        </div>
      </div>
    </footer>
  </div>
</template>

<style lang="scss" scoped></style>
