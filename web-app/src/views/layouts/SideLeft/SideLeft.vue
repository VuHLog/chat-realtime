<script setup>
import { ref, getCurrentInstance, watch, onMounted } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useBaseStore } from "@/store";

const { proxy } = getCurrentInstance();
const store = useBaseStore();
const router = useRouter();
const route = useRoute();

const conversationId = ref(route.params.conversationId);
const myUserId = ref("");

onMounted(async () => {
  await store.getMyUserId().then((res) => {
    myUserId.value = res;
  });
  await handleLoadMyConversation();
});

// load lại data trong component khi path thay đổi
watch(
  () => route.params.conversationId,
  (newVal) => {
    conversationId.value = newVal;
    totalElements.value = 0;
    pageSize.value = 10;
    pageNumber.value = 1;
  }
);

//pagination
const totalElements = ref(0);
const pageSize = ref(10);
const pageNumber = ref(1);

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
  if (newVal !== "") {
    showBackChat.value = true;
    loadSearchResults(newVal);
  } else {
    showBackChat.value = false;
  }
});

// load ket qua tim kiem
async function loadSearchResults(username) {
  await proxy.$api
    .get("/identity/users/username?username=" + username)
    .then((res) => {
      isUserExisted.value = true;
      searchResults.value = res.result;
    })
    .catch((error) => {
      let data = error.response.data;
      if (data.code === 1005) {
        isUserExisted.value = false;
      }
    });
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
  console.log(membersId);
  
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
</script>

<template>
  <div class="d-flex flex-column max-h-100vh h-100 px-2 border-e-sm">
    <div class="search-box">
      <h2 class="font-weight-bold py-3 px-1">Đoạn chat</h2>
      <div class="d-flex align-center">
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

    <div class="mt-2 h-100 overflow-y-auto">
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
            @click="chatWith(conversation.type===1?conversation?.receivers?.id:'')"
          >
            <div class="d-flex align-center">
              <img
                class="width-avatar height-avatar rounded-circle object-cover object-center"
                :src="conversation.type===1?conversation?.receivers?.avatarUrl:''"
                alt=""
              />
            </div>
            <div class="d-flex flex-column ml-2 justify-center">
              <div class="text-14 font-weight-bold">{{ conversation.type===1?conversation?.receivers?.fullName:'' }}</div>
              <div class="text-12 text-grey-darken-1">
                <span>{{
                  `${
                    conversation?.lastMessage?.senderId === myUserId
                      ? "Bạn: "
                      : ""
                  }${conversation.lastMessage?.content}`
                }}</span>
                <span class="ml-2">3 giờ</span>
              </div>
            </div>
            <div
              class="position-absolute rounded-circle bg-white chat-box__options d-none align-center justify-center"
            >
              <font-awesome-icon :icon="['fas', 'ellipsis']" />
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>

<style lang="scss" scoped>
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
  }
}
</style>
