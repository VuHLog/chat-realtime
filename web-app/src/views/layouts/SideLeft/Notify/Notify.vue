<script setup>
import {
  ref,
  onMounted,
  onBeforeUnmount,
  getCurrentInstance,
  inject,
} from "vue";
import { useRouter } from "vue-router";
import { useBaseStore } from "@/store/index.js";
import { Client } from "@stomp/stompjs";
import NotificationStatus from "@/constants/NotificationStatus.js";
import NotificationType from "@/constants/NotificationType.js";

const { proxy } = getCurrentInstance();
const swal = inject("$swal");
const store = useBaseStore();

// load ket qua thong bao
const notifications = ref([]);
const totalNotifyResults = ref(0);
const pageSize = ref(5);
const pageNumber = ref(1);
const myInfo = ref({});

onMounted(async () => {
  myInfo.value = await store.getMyInfo();
  await loadNotifications();
  await connect();
});

async function loadNotifications() {
  await proxy.$api
    .get(
      "/notifications/my-notifications?" +
        "pageNumber=" +
        (pageNumber.value - 1) +
        "&pageSize=" +
        pageSize.value
    )
    .then((res) => {
      notifications.value = res.content;
      //get receiver name
      notifications.value.forEach(async (notification) => {
        notification.fullName = await getReceiverName(notification.receiverId);
      });

      totalNotifyResults.value = res.totalElements;
    })
    .catch((error) => console.log(error));
}

//click xem them
async function clickMoreResults() {
  if (notifications.value.length >= totalNotifyResults.value) return;
  pageSize.value += 5 * pageSize.value;
  await loadNotifications();
}

//get receiver name
async function getReceiverName(userId) {
  let name = "";
  await proxy.$api
    .get("/identity/users/" + userId)
    .then((res) => {
      name = res.result.fullName;
    })
    .catch((error) => console.log(error));
  return name;
}

//update notification status -> read
async function updateNotificationStatus(notificationId) {
  await proxy.$api
    .put("/notifications/" + notificationId + "/read", {})
    .then((res) => {
      let index = notifications.value.findIndex(
        (notification) => notification.id === notificationId
      );
      notifications.value[index].status = NotificationStatus.READ;
    })
    .catch((error) => console.log(error));
}

//xoa thong bao
async function deleteNotify(notificationId) {
  swal
    .fire({
      title: "Xóa thông báo này?",
      showCancelButton: true,
      confirmButtonText: "Có",
      showCancelButton: true,
      cancelButtonText: "Không",
    })
    .then(async (result) => {
      if (result.isConfirmed) {
        await proxy.$api
          .delete("/notifications/" + notificationId)
          .then((res) => {
            console.log(res.result);
            totalNotifyResults.value--;
            notifications.value = notifications.value.filter(
              (notification) => notification.id !== notificationId
            );
          });
      }
    });
}

//xu ly notifications realtime - bị lỗi
//#region
// const token = localStorage.getItem("token");
// const stompClient = new Client({
//   brokerURL: "http://localhost:8084/notifications/ws",
//   debug: (str) => {
//     console.log(str);
//   },
//   onConnect: (frame) => {
//     console.log("Connected: " + frame);
//     stompClient.subscribe("/topic/notifications/receiver/" + myInfo.value.id, (response) => {
//       console.log(JSON.parse(response.body));
//     });
//   },
//   onWebSocketError: (error) => {
//     console.error("Error with websocket", error);
//   },
//   onStompError: (frame) => {
//     console.error("Broker reported error: " + frame.headers["message"]);
//     console.error("Additional details: " + frame.body);
//   },
// });

// async function connect() {
//   stompClient.activate();
// }

// function disconnect() {
//   stompClient.deactivate();
//   console.log("Disconnected");
// }

// onBeforeUnmount(() => {
//   disconnect();
// });
//#endregion
</script>

<template>
  <div class="d-flex flex-column">
    <transition name="slide-fade" mode="out-in">
      <div class="mt-7 d-flex flex-column">
        <template v-for="notification in notifications" :key="notification.id">
          <router-link
            :to="notification.href"
            class="text-decoration-none text-grey-darken-4"
          >
            <div
              class="d-flex align-center w-100 px-1 py-2 cursor-pointer notify-box rounded user-select-none position-relative"
              @click="updateNotificationStatus(notification.id)"
            >
              <div class="d-flex align-center w-100">
                <div class="d-flex align-center">
                  <img
                    class="width-avatar height-avatar rounded-circle object-cover object-center"
                    src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQMdrLWXLaNFf6VZxIb67nycfFzc9laPmhT8Q&s"
                    alt=""
                  />
                </div>
                <div
                  class="text-14 d-flex flex-grow-1 flex-column ml-2 justify-space-between"
                >
                  <div class="d-flex">
                    <p class="">
                      <strong class="d-inline-block">{{
                        notification.fullName
                      }}</strong>
                      <template v-if="notification.notificationType === NotificationType.NEW_MESSAGE">
                        đã gửi cho bạn một tin nhắn mới:
                      {{ notification.content }}
                      </template>
                      <template v-else-if="notification.notificationType === NotificationType.FRIEND_REQUEST">
                      {{ notification.content }}
                      </template>
                    </p>
                  </div>
                  <div class="text-12 text-grey-darken-1">
                    <span>{{ notification.timestamp }}</span>
                  </div>
                </div>
                <div
                  v-if="notification.status === NotificationStatus.UNREAD"
                  class="text-purple-accent-4"
                >
                  <font-awesome-icon :icon="['fas', 'circle']" />
                </div>
              </div>
              <div
                class="position-absolute rounded-circle bg-white notify-box__options d-none align-center justify-center"
                @click="deleteNotify(notification.id)"
              >
                <font-awesome-icon :icon="['fas', 'trash']" />
              </div>
            </div>
          </router-link>
        </template>

        <div class="text-center">
          <span
            class="cursor-pointer text-decoration-underline text-light-blue-accent-3 text-14"
            @click="clickMoreResults()"
            v-if="notifications.length < totalNotifyResults"
            >Xem thêm</span
          >
        </div>
      </div>
    </transition>
  </div>
</template>

<style lang="scss" scoped>
.notify-box {
  &:hover {
    background-color: #eeeeee;
  }

  &.active {
    background-color: #e1bee7;
  }

  &:hover .notify-box__options {
    display: flex !important;
  }

  .notify-box__options {
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
