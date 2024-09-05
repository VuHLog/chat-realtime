import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";
import {base} from "@/apis/ApiService.js"
import TokenService from "@/service/TokenService.js"

const token = TokenService.getLocalAccessToken();
const tokenDecoded = token? jwtDecode(token): {};


export const useBaseStore = defineStore("base", {
  state: () => {
    return {
      isLoggedIn: tokenDecoded?true:false,
      roles: tokenDecoded?.scope || "",
      username: tokenDecoded?.sub || "",
      fullName: tokenDecoded?.name || "",
      avatarUserDefault:
        "https://res.cloudinary.com/iflixlong/image/upload/v1717530924/icon-256x256_judaje.png",
    };
  },
  getters: {
  },
  actions: {
    refreshToken(accessToken) {
      this.isLoggedIn = true;
      localStorage.setItem("token",accessToken);
    },
    async getMyInfo(){
      let response = null;
      await base.get("/identity/users/myInfo").then((res) => {
        response = res.result;
      })
      .catch((error) => console.log(error)
      )
      return response;
    },
    async getMyUserId(){
      let response = null;
      await this.getMyInfo().then((res) => {
        response = res;
      });
      return response.id;
    },
    async getUserById(userId){
      let response = null;
      await base.get("/identity/users/"+ userId).then((res) => {
        response = res.result;
      })
      .catch((error) => console.log(error)
      )
      return response;
    },
    getCurrentDateTime() {
      const now = new Date();
    
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');  // Tháng bắt đầu từ 0, nên cần +1
      const day = String(now.getDate()).padStart(2, '0');
      
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      const seconds = String(now.getSeconds()).padStart(2, '0');
    
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    }
  },
});
