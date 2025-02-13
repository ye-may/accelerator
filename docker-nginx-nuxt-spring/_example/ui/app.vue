<script setup lang="ts">
let text = ref("Loading...")
async function getTestData() {
    const url = '/api/test'

    fetch(url)
      .then(response => response.text())
      .then((responseText) => {
          console.log(responseText)
          text.value = responseText
      })
      .catch(err => console.log(err))
}

onMounted(() => getTestData())
</script>

<template>
  <div>
    <div>
      Test data: {{ text }}
    </div>
    <NuxtRouteAnnouncer />
    <NuxtWelcome />
  </div>
</template>
