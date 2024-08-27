<script setup>
import {
  onMounted,
  onBeforeUnmount,
  ref,
  watch,
  getCurrentInstance,
  computed,
} from "vue";
import { Client } from "@stomp/stompjs";
import { useRoute } from "vue-router";
import { useBaseStore } from "@/store/index.js";
import FriendRequestsStatus from "@/constants/FriendRequestsStatus.js";
import Header from "./Header.vue";

const store = useBaseStore();
const { proxy } = getCurrentInstance();
const route = useRoute();

const conversationId = ref(route.params.conversationId);

// Các tham chiếu đến các phần tử Header và Footer và body
const headerRef = ref(null);
const footerRef = ref(null);
const bodyRef = ref(null);
const bodyHeight = ref(null);

//pagination
const totalElements = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);

const myInfo = ref();
const conversation = ref();
const receiver = ref({});
const messageText = ref("");
const messages = ref([]);

onMounted(async () => {
  await connect();

  calcBodyHeight();

  myInfo.value = await store.getMyInfo();
  await loadData();
});

// load lại data trong component khi path thay đổi
watch(
  () => route.params.conversationId,
  (newVal) => {
    conversationId.value = newVal;
    totalElements.value = 0;
    pageSize.value = 10;
    pageNumber.value = 1;
    calcBodyHeight();
    loadData();
  }
);

//tinh body height
function calcBodyHeight() {
  bodyHeight.value = `calc(100vh - ${
    headerRef.value.offsetHeight + footerRef.value.offsetHeight
  }px)`;
  bodyRef.value.style.height = bodyHeight.value;
}

//xu ly khi scroll
function handleScroll() {
  if (messages.value.length >= totalElements.value) return;

  console.log("scrollTop");
  if (bodyRef.value.scrollTop === 0) {
    // pageNumber.value++;
    // handleLoadMessages();
  }
}

async function handleLoadConversation() {
  await proxy.$api
    .get("/chat/conversations/" + conversationId.value)
    .then((res) => {
      conversation.value = res.result;
    })
    .catch((error) => {
      console.log(error);
    });
  let conversationVal = conversation.value;
  if (conversationVal.type === 1) {
    let receiverId = conversationVal.groupMembers.find((value) => {
      return value.userId !== myInfo.value.id;
    }).userId;
    receiver.value = await store.getUserById(receiverId);
    getRelationship();
  }
}

async function handleLoadMessages() {
  await proxy.$api
    .get(
      "/chat/messages/conversation?conversationId=" +
        conversationId.value +
        "&pageNumber=" +
        (pageNumber.value - 1) +
        "&pageSize=" +
        pageSize.value
    )
    .then((res) => {
      messages.value = res.content;
      totalElements.value = res.totalElements;
    })
    .catch((error) => console.log(error));
}

async function loadData() {
  await handleLoadConversation();
  await handleLoadMessages();
}

//xu ly real time voi stomp client
//#region
onBeforeUnmount(() => {
  disconnect();
});

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
    stompClient.subscribe(
      "/topic/conversations/" + conversationId.value,
      (response) => {
        messages.value.unshift(JSON.parse(response.body));
      }
    );
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
  let messageRequest = {
    content: messageText.value,
    contentType: "text",
    conversationId: conversationId.value,
  };
  let messageId = null;
  await proxy.$api
    .post("/chat/messages", messageRequest)
    .then((res) => {
      messageId = res.result.id;
    })
    .catch((error) => {
      console.log(error);
    });

  stompClient.publish({
    destination: "/app/sendMessage",
    body: JSON.stringify({
      conversationId: messageRequest.conversationId,
      messagesId: messageId,
    }),
  });
  messageText.value = "";
}
//#endregion

//xu ly friend
//#region
const friendRequests = ref({});
async function getRelationship() {
  await proxy.$api
    .get("/friend/requests/" + receiver.value.id)
    .then((res) => {
      friendRequests.value = res.result;
    })
    .catch((error) => {
      let response = error.response.data;
      if (response.code === 1013) {
        friendRequests.value.status = response.message;
      }
    });
}

async function addFriendRequest(receiverId) {
  await proxy.$api
    .post("/friend/requests", {
      receiverId,
    })
    .then((res) => {
      friendRequests.value = res.result;
    })
    .catch((error) => {
      console.log(error);
    });
}
async function cancelFriendRequest() {
  await proxy.$api
    .put("/friend/requests/cancel", {
      id: friendRequests.value.friendRequestId,
    })
    .then((res) => {
      friendRequests.value = res.result;
    })
    .catch((error) => {
      console.log(error);
    });
}

//#endregion
</script>

<template>
  <div
    class="h-100 position-relative d-flex flex-column"
    @scroll="handleScroll()"
  >
    <Header
      ref="headerRef"
      :receiver="receiver"
      class="position-sticky top-0 right-0 left-0 z-index-99 bg-white"
    ></Header>

    <div
      class="d-flex flex-column align-center mt-10"
      v-if="messages.length === 0"
    >
      <div>
        <img
          class="height-avatar-thumbnail width-avatar-thumbnail rounded-circle object-cover object-center"
          :src="receiver?.avatarUrl"
          alt=""
        />
        <v-tooltip activator="parent" location="bottom">
          <span class="text-12">{{ receiver?.username }}</span>
        </v-tooltip>
      </div>
      <div class="ml-2 font-weight-bold">{{ receiver?.fullName }}</div>
      <div
        v-if="
          friendRequests?.status === FriendRequestsStatus.FRIEND_REQUEST_NOT_EXISTED || friendRequests?.status === FriendRequestsStatus.CANCELLED
        "
        class="d-flex bg-deep-purple-accent-3 py-1 px-2 mt-1 cursor-pointer rounded-xl"
        @click="addFriendRequest(receiver?.id)"
      >
        <div><font-awesome-icon :icon="['fas', 'user-plus']" /></div>
        <span class="ml-2">Thêm bạn bè</span>
      </div>
      <div
        v-else-if="friendRequests?.status === FriendRequestsStatus.PENDING"
        class="d-flex bg-deep-purple-accent-3 py-1 px-2 mt-1 cursor-pointer rounded-xl"
        @click="cancelFriendRequest()"
      >
        <div><font-awesome-icon :icon="['fas', 'user-plus']" /></div>
        <span class="ml-2">Hủy lời mời</span>
      </div>
      <div v-if="friendRequests?.status === FriendRequestsStatus.ACCEPTED">
        <span class="text-12">Bạn bè</span>
      </div>
    </div>

    <!-- BODY -->
    <div
      ref="bodyRef"
      class="flex-grow-1 d-flex flex-column-reverse overflow-y-auto"
      :style="{ maxHeight: bodyHeight }"
    >
      <template v-for="message in messages" :key="message.id">
        <div>
          <div
            class="text-center text-grey-darken-1 text-12 font-weight-medium"
          >
            {{ message.timeSent }}
          </div>
          <div
            class="d-flex"
            :class="{ 'justify-end': message.senderId === myInfo.id }"
          >
            <div class="d-flex max-w-40p px-4">
              <div
                class="d-flex align-end"
                v-if="message.senderId !== myInfo.id"
              >
                <img
                  class="width-avatar-chat height-avatar-chat rounded-circle object-cover object-center"
                  :src="receiver?.avatarUrl"
                  alt=""
                />
              </div>
              <p
                class="text-wrap w-100 rounded-xl text-14 px-3 py-2 ml-2"
                :class="
                  message.senderId === myInfo.id
                    ? 'bg-purple-accent-4'
                    : 'bg-grey-lighten-3'
                "
              >
                {{ message.content }}
              </p>
            </div>
          </div>
        </div>
      </template>
    </div>
    <!-- END BODY -->

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
