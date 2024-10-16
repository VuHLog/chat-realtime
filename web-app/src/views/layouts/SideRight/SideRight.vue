<script setup>
import {
  onMounted,
  onBeforeUnmount,
  ref,
  watch,
  inject,
  getCurrentInstance,
} from "vue";
import { Client } from "@stomp/stompjs";
import { useRoute } from "vue-router";
import { useBaseStore } from "@/store/index.js";
import data from "emoji-mart-vue-fast/data/all.json";
import { Picker, EmojiIndex } from "emoji-mart-vue-fast/src";
import "emoji-mart-vue-fast/css/emoji-mart.css";
import { v4 as uuidv4 } from "uuid";
import FriendRequestsStatus from "@/constants/FriendRequestsStatus.js";
import MessageContentType from "@/constants/MessageContentType.js";
import NotificationType from "@/constants/NotificationType.js";
import Header from "./Header.vue";

const store = useBaseStore();
const { proxy } = getCurrentInstance();
const route = useRoute();
const swal = inject("$swal");

const emit = defineEmits(["updateEmoji", "send-message", "delete-message"]);
const emojiPickerSelected = ref(false);
const emojiIndex = ref(new EmojiIndex(data));
function showEmoji(emoji) {
  messageText.value += emoji.native;
}

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

const errorConversationNotExisted = ref(false);
async function handleLoadConversation() {
  await proxy.$api
    .get("/chat/conversations/" + conversationId.value)
    .then((res) => {
      conversation.value = res.result;
      errorConversationNotExisted.value = false;
    })
    .catch((error) => {
      let data = error.response.data;
      if (data.code === 1015) {
        errorConversationNotExisted.value = true;
      }
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
    .catch((error) => console.log(error.response.data.message));
}

async function loadData() {
  await handleLoadConversation();
  await handleLoadMessages();
}

//xu ly real time gui tin nhan voi stomp client
//#region

const showSentIcon = ref(false);
watch(messageText, (newVal) => {
  if (newVal !== "") {
    showSentIcon.value = true;
  } else {
    showSentIcon.value = false;
  }
});

// xu ly nhan Enter trong textarea
function handlePressEnterTextArea(event) {
  if (event.key !== "Enter") return;
  else if (event.shiftKey) return;

  event.preventDefault();
  sendMessage(MessageContentType.TEXT, null);
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
    console.log("my info : " + myInfo.value.id);
    stompClient.subscribe(
      "/topic/notifications/receiver/" + myInfo.value.id,
      (response) => {
        let responseBody = JSON.parse(response.body);
        const Toast = swal.mixin({
          toast: true,
          position: "bottom-end",
          showConfirmButton: false,
          timer: 2000,
          timerProgressBar: false,
          didOpen: (toast) => {
            toast.onmouseenter = swal.stopTimer;
            toast.onmouseleave = swal.resumeTimer;
          },
        });
        if(NotificationType.NEW_MESSAGE === responseBody.notificationType){
          Toast.fire({
            title: `<a href='http://localhost:5173/${responseBody.href}'style='display: inline-block;text-decoration: none; color: #00B0FF; width: 100%; text-align: center;'>Bạn có tin nhắn mới</a>`,
          });
        }
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

onBeforeUnmount(() => {
  disconnect();
});

async function sendMessage(contentType, url) {
  let messageRequest = {
    id: uuidv4(),
    senderId: myInfo.value.id,
    content: messageText.value,
    contentType: contentType,
    timeSent: store.getCurrentDateTime(),
    status: "sent",
    conversationId: conversationId.value,
  };
  if (contentType === MessageContentType.FILE) {
    messageRequest.url = url;
    messageRequest.content = file.value.name;
  }

  // send notification to receiver
  stompClient.publish({
    destination: "/app/sendMessage",
    body: JSON.stringify(messageRequest),
  });

  conversation.value.groupMembers
      .filter((member) => member.userId != myInfo.value.id)
      .map((member) => member.userId)
      .forEach(receiverId => {
        sendNotification(receiverId, messageText.value, NotificationType.NEW_MESSAGE, "/messages/"+ conversationId.value)
      });

  emit("send-message", messageRequest);
  messageText.value = "";
  emojiPickerSelected.value = false;
}

function sendNotification(receiverId, content, notificationType, href){
  let notificationRequest = {
    senderId: myInfo.value.id,
    receiverId: receiverId,
    content: content,
    notificationType: notificationType,
    href: href,
  };
  stompClient.publish({
    destination: "/app/sendNotification",
    body: JSON.stringify(notificationRequest),
  });
}

async function sendLikeIcon() {
  messageText.value = "👍🏼";
  sendMessage(MessageContentType.TEXT, null);
}

//xu ly gui file
const file = ref(null);
async function handleFileUpload(event) {
  file.value = event.target.files[0];
  let url = await submitFile();
  await sendMessage(MessageContentType.FILE, url);
}

async function submitFile() {
  let formData = new FormData();
  let url = null;
  formData.append("file", file.value);
  await proxy.$api
    .postFile("/identity/cloudinary/upload/file", formData)
    .then((res) => {
      url = res.url;
      console.log(res.url);
    })
    .catch((error) => console.log(error));
  console.log(url);
  return url;
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
      console.log(error.response.data.message);
    });
}

async function acceptFriendRequest() {
  await proxy.$api
    .put("/friend/requests/accept", {
      id: friendRequests.value.id,
    })
    .then((res) => {
      friendRequests.value = res.result;
    })
    .catch((error) => {
      console.log(error.response.data.message);
    });
}

async function deleteFriendRequest() {
  await proxy.$api
    .delete("/friend/requests/" + friendRequests.value.id)
    .then((res) => {
      friendRequests.value = {};
      friendRequests.value.status =
        FriendRequestsStatus.FRIEND_REQUEST_NOT_EXISTED;
    })
    .catch((error) => {
      console.log(error.response.data.message);
    });
}

function unfriend(status) {
  friendRequests.value = {};
  friendRequests.value.status = status;
}

//#endregion

//xu ly message options
//#region
function deleteMessage(messageId) {
  swal
    .fire({
      title: "Xóa tin nhắn này?",
      showCancelButton: true,
      confirmButtonText: "Có",
      showCancelButton: true,
      cancelButtonText: "Không",
    })
    .then((result) => {
      if (result.isConfirmed) {
        proxy.$api.delete("/chat/messages/" + messageId).then(() => {
          messages.value = messages.value.filter(
            (value) => value.id !== messageId
          );
          let message = {};
          if (messages.value.length >= 1) {
            message = messages.value[0];
            message.conversationId = conversationId.value;
          }
          emit("delete-message", message);
        });
      }
    });
}
//#endregion
</script>

<template>
  <div class="h-100">
    <div
      class="h-100 position-relative d-flex flex-column"
      @scroll="handleScroll()"
      v-if="!errorConversationNotExisted"
    >
      <Header
        ref="headerRef"
        :receiver="receiver"
        :isFriend="friendRequests?.status === FriendRequestsStatus.ACCEPTED"
        class="position-sticky top-0 right-0 left-0 z-index-99 bg-white"
        @unfriend="
          (value) => {
            unfriend(value);
          }
        "
      ></Header>

      <div
        class="d-flex flex-column align-center mt-10 mb-5"
        v-if="
          messages.length === 0 ||
          friendRequests?.status !== FriendRequestsStatus.ACCEPTED
        "
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
            friendRequests?.status ===
            FriendRequestsStatus.FRIEND_REQUEST_NOT_EXISTED
          "
          class="d-flex bg-deep-purple-accent-3 py-1 px-2 mt-1 cursor-pointer rounded-xl"
          @click="addFriendRequest(receiver?.id)"
        >
          <div><font-awesome-icon :icon="['fas', 'user-plus']" /></div>
          <span class="ml-2">Thêm bạn bè</span>
        </div>
        <div
          v-if="
            friendRequests?.status === FriendRequestsStatus.PENDING &&
            myInfo.id === friendRequests?.senderId
          "
          class="d-flex bg-deep-purple-accent-3 py-1 px-2 mt-1 cursor-pointer rounded-xl"
          @click="deleteFriendRequest()"
        >
          <div><font-awesome-icon :icon="['fas', 'user-plus']" /></div>
          <span class="ml-2">Hủy lời mời</span>
        </div>
        <div
          v-else-if="
            friendRequests?.status === FriendRequestsStatus.PENDING &&
            myInfo.id === friendRequests?.receiverId
          "
          class="d-flex bg-grey-lighten-1 py-1 px-2 mt-1 cursor-pointer rounded"
        >
          <span
            class="bg-deep-purple-accent-3 py-1 px-2 rounded-lg text-12"
            @click="acceptFriendRequest()"
            >Chấp nhận lời mời</span
          >
          <span class="ml-2 py-1 px-2 text-12" @click="deleteFriendRequest"
            >Xóa lời mời</span
          >
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
          <div class="message">
            <div
              class="text-center text-grey-darken-1 text-12 font-weight-medium"
            >
              {{ message.timeSent }}
            </div>
            <div
              class="d-flex align-center"
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
                  <template
                    v-if="message.contentType === MessageContentType.TEXT"
                  >
                    {{ message.content }}
                  </template>
                  <a
                    class="text-white"
                    :href="message.url"
                    v-else-if="message.contentType === MessageContentType.FILE"
                    >{{ message.content }}</a
                  >
                </p>
              </div>
              <div
                class="position-relative message_options"
                :class="{ 'order-first': message.senderId === myInfo.id }"
                v-if="message.senderId === myInfo.id"
              >
                <div
                  class="w-28px h-28px rounded-circle d-none align-center justify-center message_options-icon cursor-pointer"
                  @click="deleteMessage(message.id)"
                >
                  <font-awesome-icon :icon="['fas', 'trash']" />
                  <v-tooltip activator="parent" location="bottom">
                    <span class="text-12">Gỡ</span>
                  </v-tooltip>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
      <!-- END BODY -->

      <footer
        ref="footerRef"
        class="footer d-flex py-3 align-center position-relative"
      >
        <div class="text-purple-accent-4 pa-2 ma-1 cursor-pointer">
          <label for="formFile" class="cursor-pointer">
            <font-awesome-icon for="formFile" :icon="['far', 'file']" />
          </label>
          <v-tooltip activator="parent" location="bottom">
            <span class="text-12">Đính kèm file</span>
          </v-tooltip>
          <input
            class="w-100 py-2 pl-1 mt-6 d-none"
            type="file"
            id="formFile"
            accept="application/msword, application/vnd.ms-excel, application/vnd.ms-powerpoint,
text/plain, application/pdf"
            @change="handleFileUpload($event)"
          />
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
            style="resize: none"
            :disabled="friendRequests.status !== FriendRequestsStatus.ACCEPTED"
            :class="{
              'cursor-not-allowed':
                friendRequests.status !== FriendRequestsStatus.ACCEPTED,
            }"
          ></textarea>
          <div
            class="text-purple-accent-4 pa-1 cursor-pointer position-relative"
            @click="emojiPickerSelected = !emojiPickerSelected"
          >
            <font-awesome-icon :icon="['fas', 'face-smile']" />
            <v-tooltip activator="parent" location="bottom">
              <span class="text-12">Chọn biểu tượng hoặc cảm xúc</span>
            </v-tooltip>
          </div>
        </div>
        <Picker
          v-if="emojiPickerSelected"
          class="emoji-picker"
          :data="emojiIndex"
          emoji="point_up"
          set="facebook"
          :showPreview="false"
          @select="showEmoji"
        />
        <div
          class="text-purple-accent-4 pa-2 ma-1 cursor-pointer"
          @click="sendLikeIcon()"
        >
          <div v-if="!showSentIcon">
            <font-awesome-icon :icon="['fas', 'thumbs-up']" />
            <v-tooltip activator="parent" location="bottom">
              <span class="text-12">Gửi lượt thích</span>
            </v-tooltip>
          </div>
          <div v-else @click="sendMessage(MessageContentType.TEXT, null)">
            <font-awesome-icon :icon="['fas', 'paper-plane']" />
            <v-tooltip activator="parent" location="bottom">
              <span class="text-12">Nhấn Enter để gửi</span>
            </v-tooltip>
          </div>
        </div>
      </footer>
    </div>
    <h1 v-else class="text-grey-lighten-1 text-center mt-16">
      Cuộc trò chuyện không tồn tại
    </h1>
  </div>
</template>

<style lang="scss" scoped>
.emoji-picker {
  position: absolute;
  bottom: 100%;
  right: 0;
  height: 300px;
  width: 340px;
  box-shadow: rgba(99, 99, 99, 0.2) 0px 2px 8px 0px;
  &::after {
    content: "";
    position: absolute;
    right: 50px;
    top: 100%;
    width: 0;
    height: 0;
    border-left: 10px solid transparent;
    border-right: 10px solid transparent;
    border-top: 10px solid #ffffff;
    clear: both;
  }
}

.message {
  &:hover .message_options-icon {
    display: flex !important;
  }
  .message_options {
    .message_options-icon {
      &:hover {
        background-color: #e0e0e0 !important;
      }
    }
  }
}
</style>
