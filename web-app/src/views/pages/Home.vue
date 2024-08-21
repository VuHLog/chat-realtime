<script setup>
import SideLeft from "@layouts/SideLeft/SideLeft.vue";
import SideRight from "@layouts/SideRight/SideRight.vue";
</script>

<template>
    <div class="d-flex flex-wrap max-h-100vh h-100vh w-100 bg-white text-grey-darken-4">
        <v-row no-gutters>
            <v-col cols="3">
            <side-left></side-left>
        </v-col>
        <v-col cols="9">
            <side-right></side-right>
        </v-col>
        </v-row>
    </div>
</template>

<style lang="scss" scoped>

</style>