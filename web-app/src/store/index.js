import { defineStore } from "pinia";
import Cookies from "js-cookie";
import { jwtDecode } from "jwt-decode";

export const useBaseStore = defineStore("base", {
  state: () => {
    return {
      cart: JSON.parse(Cookies.get("cart") || "[]"),
      showSidenav: true,
      showNavbar: true,
      showFooter: true,
      isLoggedIn: false,
      roles: "",
      username: "",
      fullName: "",
      tokenGHN: "399094ea-4b6f-11ef-b635-eeb7b370243f",
      avatarUserDefault:
        "https://res.cloudinary.com/iflixlong/image/upload/v1717530924/icon-256x256_judaje.png",
    };
  },
  getters: {
    totalPrice: (state) => {
      let total = state.cart.reduce((prev, next) => {
        return prev + next.variant.price * next.quantity;
      }, 0);
      return total;
    },
    totalQuantity: (state) => {
      let total = state.cart.reduce((prev, next) => {
        return prev + next.quantity;
      }, 0);
      return total;
    },
  },
  actions: {
    refreshToken(accessToken) {
      this.isLoggedIn = true;
      localStorage.setItem("token",accessToken);
    },
    addToCart(variant) {
      let index = this.cart.findIndex((item) => item.variant.id === variant.id);
      if (index >= 0) {
        this.cart[index].quantity++;
      } else {
        this.cart.push({ variant, quantity: 1 });
      }
      Cookies.set("cart", JSON.stringify(this.cart), { expires: 7 });
    },

    changeQuantity(variantId, quantity) {
      let index = this.cart.findIndex((item) => item.variant.id === variantId);
      if (index >= 0) {
        this.cart[index].quantity += quantity;
        Cookies.set("cart", JSON.stringify(this.cart), { expires: 7 });
      }
    },

    removeFromCart(variantId) {
      this.cart = this.cart.filter((item) => item.variant.id !== variantId);
      Cookies.set("cart", JSON.stringify(this.cart), { expires: 7 });
    },

    clearCart() {
      this.cart = [];
      Cookies.set("cart", JSON.stringify(this.cart), { expires: 7 });
    },
  },
});
