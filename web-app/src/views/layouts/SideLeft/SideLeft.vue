<script setup>
import { ref, getCurrentInstance, watch, onMounted, inject } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useBaseStore } from "@/store";
import setting from "@layouts/SideLeft/UserSettings/Setting.vue";
import notify from "@layouts/SideLeft/Notify/Notify.vue";

const { proxy } = getCurrentInstance();
const swal = inject("$swal");
const store = useBaseStore();
const router = useRouter();
const route = useRoute();

const conversationId = ref(route.params.conversationId);
const myUserId = ref("");

const props = defineProps({
  messageSent: {
    type: Object,
    default: null,
  },
  updateLastMessage: {
    type: Object,
    default: null,
  },
});

watch(
  () => props.messageSent,
  (message) => {
    updateLastConversation(message);
  }
);

watch(
  () => props.updateLastMessage,
  (message) => {
    if (Object.keys(message).length !== 0) {
      updateLastConversation(message);
    }
  }
);

function updateLastConversation(message) {
  let index = myConversations.value.findIndex(
    (conversation) => conversation.id === message.conversationId
  );
  myConversations.value[index].lastMessage = message;
  myConversations.value[index].lastMessageId = message.id;
  let t = myConversations.value[0];
  myConversations.value[0] = myConversations.value[index];
  myConversations.value[index] = t;
}

onMounted(async () => {
  await store.getMyUserId().then((res) => {
    myUserId.value = res;
  });
  await loadData();
});

// load lại data trong component khi path thay đổi
watch(
  () => route.params.conversationId,
  async (newVal) => {
    conversationId.value = newVal;
    await reloadData();
  }
);

//pagination
const totalElements = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);

async function reloadData() {
  totalElements.value = 0;
  pageSize.value = 10;
  pageNumber.value = 1;
  await loadData();
}

async function loadData() {
  await handleLoadMyConversation();
}

const myConversations = ref([]);
async function handleLoadMyConversation() {
  await proxy.$api
    .get(
      "/chat/conversations?" +
        "pageNumber=" +
        (pageNumber.value - 1) +
        "&pageSize=" +
        pageSize.value
    )
    .then((res) => {
      totalElements.value = res.totalElements;
      myConversations.value = res.content;

      // lay ra last message
      myConversations.value.forEach(async (value) => {
        if (!value.lastMessage) {
          await loadLastMessage(value.lastMessageId).then((res) => {
            value.lastMessage = res;
          });
        }

        if (value.groupMembers.length === 2) {
          value.groupMembers.forEach(async (member) => {
            if (member.userId !== myUserId.value) {
              value.receivers = await store.getUserById(member.userId);
            }
          });
        }
      });
    })
    .catch((error) => console.log(error));
}

//load last message
async function loadLastMessage(messageId) {
  let message = {};
  await proxy.$api
    .get("/chat/messages/" + messageId)
    .then((res) => {
      message = res.result;
    })
    .catch((error) => console.log(error));
  return message;
}

//#region
//handle search
const searchText = ref("");
const showBackChat = ref(false);
const searchResults = ref([]);
const isUserExisted = ref(true);

//handle khi search text thay doi
watch(searchText, (newVal) => {
  totalSearchResults.value = 0;
  pageSizeSR.value = 1;
  pageNumberSR.value = 1;
  if (newVal !== "") {
    showBackChat.value = true;
    loadSearchResults(newVal);
  } else {
    showBackChat.value = false;
  }
});

// load ket qua tim kiem
const totalSearchResults = ref(0);
const pageSizeSR = ref(10);
const pageNumberSR = ref(10);
async function loadSearchResults(username) {
  await proxy.$api
    .get(
      "/identity/users/username?username=" +
        username +
        "&pageNumber=" +
        (pageNumberSR.value - 1) +
        "&pageSize=" +
        pageSizeSR.value
    )
    .then((res) => {
      isUserExisted.value = true;
      searchResults.value = res.content;
      totalSearchResults.value = res.totalElements;
    })
    .catch((error) => {
      let data = error.response.data;
      if (data.code === 1005) {
        isUserExisted.value = false;
      }
    });
}

async function clickMoreSearchResults() {
  if (searchResults.value.length >= totalSearchResults.value) return;
  pageSizeSR.value += 10 * pageSizeSR.value;
  await loadSearchResults(searchText.value);
}

//handle button back when search
function backChat() {
  showBackChat.value = false;
  searchText.value = "";
  searchResults.value.length = 0;
}

async function chatWith(userId) {
  let type = 1;
  let membersId = Array.of(userId);

  await proxy.$api
    .post("/chat/conversations", {
      type: type,
      membersId: membersId,
    })
    .then((res) => {
      const conversation = res.result;
      router.push("/messages/" + conversation.id);
    })
    .catch((error) => {});
}

//#endregion

//xoa cuoc tro chuyen
async function deleteConversation(conversationIdSelected) {
  swal
    .fire({
      title: "Bạn có muốn cuộc trò chuyện này?",
      showCancelButton: true,
      confirmButtonText: "Có",
      showCancelButton: true,
      cancelButtonText: "Không",
    })
    .then(async (result) => {
      if (result.isConfirmed) {
        await proxy.$api.delete("/chat/conversations/" + conversationIdSelected).then(() => {
          myConversations.value = myConversations.value.filter(
            (value) => value.id !== conversationIdSelected
          );
          if(myConversations.value.length > 0){
            conversationId.value = myConversations.value[0].id;
            router.push("/messages/" + conversationId.value);
          }else{
            router.push("/messages/");
          }
        });
      }
    });
}

//xu ly hien thi cac option cho nguoi dung
//#region
const showOptions = ref(false);
//#endregion

//xu ly hien thi cac thong bao
//#region
const showNotify = ref(false);
//#endregion


</script>

<template>
  <div class="d-flex flex-column max-h-100vh h-100 px-2 border-e-sm">
    <div>
      <div class="header py-3 px-1 d-flex align-center justify-space-between">
        <h2 class="font-weight-bold py-3 px-1">Đoạn chat</h2>
        <div class="d-flex">
          <div class="mr-3 cursor-pointer" :class="{'text-purple-accent-4': showOptions}" @click="showOptions = !showOptions; showNotify= false">
            <font-awesome-icon class="setting-icon" :icon="['fas', 'gear']" />
            <v-tooltip activator="parent" location="bottom">
              <span class="text-12">Tùy chọn</span>
            </v-tooltip>
          </div>
          <div class="mr-3 cursor-pointer" :class="{'text-purple-accent-4': showNotify}" @click="showNotify = !showNotify; showOptions = false">
            <font-awesome-icon class="notify-icon" :icon="['fas', 'bell']" />
            <v-tooltip activator="parent" location="bottom">
              <span class="text-12">Thông báo</span>
            </v-tooltip>
          </div>
        </div>
      </div>
      <div class="d-flex align-center search-box">
        <div
          class="back-chat-icon d-flex align-center pa-2 mr-1 text-grey-darken-1 cursor-pointer rounded-circle"
          v-if="showBackChat"
          @click="backChat()"
        >
          <font-awesome-icon
            class="h-20px w-20px"
            :icon="['fas', 'arrow-left']"
          />
        </div>
        <div class="d-flex flex-grow-1 px-2 py-2 bg-grey-lighten-4 rounded-xl">
          <label for="input-search" class="mr-2">
            <font-awesome-icon :icon="['fas', 'magnifying-glass']" />
          </label>
          <div class="w-100 user-select-none">
            <input
              v-model.trim="searchText"
              id="input-search"
              class="outline-none w-100"
              type="text"
              placeholder="Tìm kiếm theo tên đăng nhập"
            />
          </div>
        </div>
      </div>
    </div>

    <transition v-if="myConversations.length > 0 || showOptions || showBackChat || showNotify" name="slide-fade" mode="out-in">
      
      <!-- SETTING -->
      <setting v-if="showOptions"></setting>
      
      <!-- NOTIFY -->
      <notify v-else-if="showNotify"></notify>

      <div class="mt-2 h-100" v-else>
        <!-- SEARCH RESULTS -->
        <div class="mt-1 pt-2" v-if="showBackChat">
          <div v-if="isUserExisted">
            <template v-for="user in searchResults" :key="user.id">
              <div
                class="search-results d-flex align-center px-1 py-2 cursor-pointer rounded user-select-none position-relative"
                @click="chatWith(user.id)"
              >
                <div class="d-flex align-center">
                  <img
                    class="width-avatar height-avatar rounded-circle object-cover object-center"
                    :src="user.avatarUrl"
                    alt=""
                  />
                </div>
                <div class="d-flex flex-column ml-2 justify-center">
                  <div class="text-14">{{ user.fullName }}</div>
                  <div class="text-12 text-grey-darken-1">
                    <span>{{ user.username }}</span>
                  </div>
                </div>
              </div>
            </template>
            <div
              class="text-center"
              v-if="totalSearchResults > searchResults.length"
            >
              <span
                class="cursor-pointer text-decoration-underline text-light-blue-accent-3 text-14"
                @click="clickMoreSearchResults()"
                >Xem thêm</span
              >
            </div>
          </div>
          <div class="text-center" v-else>
            Không tìm thấy kết quả cho {{ searchText }}
          </div>
        </div>
        <!-- CHAT LIST -->
        <div class="mt-2 h-100 overflow-y-auto" v-if="!showBackChat">
          <template
            v-for="conversation in myConversations"
            :key="conversation.id"
          >
            <div
              class="d-flex align-center px-1 py-2 cursor-pointer chat-box rounded user-select-none position-relative"
              :class="conversation?.id === conversationId ? 'active' : ''"
              v-if="conversation.lastMessageId !== null"
            >
              <div
                class="d-flex align-center w-100"
                @click="
                  chatWith(
                    conversation.type === 1 ? conversation?.receivers?.id : ''
                  )
                "
              >
                <div class="d-flex align-center">
                  <img
                    class="width-avatar height-avatar rounded-circle object-cover object-center"
                    :src="
                      conversation.type === 1
                        ? conversation?.receivers?.avatarUrl
                        : ''
                    "
                    alt=""
                  />
                </div>
                <div class="d-flex flex-column ml-2 justify-center">
                  <div class="text-14 font-weight-bold">
                    {{
                      conversation.type === 1
                        ? conversation?.receivers?.fullName
                        : ""
                    }}
                  </div>
                  <div class="text-12 text-grey-darken-1">
                    <span>{{
                      `${
                        conversation?.lastMessage?.senderId === myUserId
                          ? "Bạn: "
                          : ""
                      }${conversation.lastMessage?.content}`
                    }}</span>
                    <span class="ml-2">{{
                      conversation.lastMessage?.timeSent
                    }}</span>
                  </div>
                </div>
              </div>
              <div
                class="position-absolute rounded-circle bg-white chat-box__options d-none align-center justify-center"
                @click="deleteConversation(conversation.id)"
              >
                <font-awesome-icon :icon="['fas', 'trash']" />
              </div>
            </div>
          </template>
        </div>
      </div>
    </transition>
    <h1 v-else class="text-grey-lighten-1 text-center mt-16">
      Hãy tạo cuộc trò chuyện mới
    </h1>
  </div>
</template>

<style lang="scss" scoped>
.header {
  .setting-icon,.notify-icon {
    transition: all 0.4 linear;
    &:hover {
      transform: rotate(-20deg);
    }
  }
}
.search-box {
  .back-chat-icon:hover {
    background-color: #eeeeee;
  }
}

.search-results {
  &:hover {
    background-color: #e0e0e0;
  }
}

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
    &:hover {
      background-color: #bdbdbd !important;
    }
  }
}

.slide-fade-enter-active {
  transition: all 0.1s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.1s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(-20px);
  opacity: 0;
}
</style>
