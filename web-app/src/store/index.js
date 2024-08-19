import { defineStore } from "pinia";
import { jwtDecode } from "jwt-decode";
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
  },
});
