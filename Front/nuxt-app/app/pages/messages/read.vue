<template>
  <Header />

  <section v-if="logged" class="messages-page">
    <div class="header">
      <h1>All Messages</h1>
      <router-link to="/messages/create" class="btn">+ New Message</router-link>
    </div>

    <div v-if="messages.length">
      <div v-for="msg in messages" :key="msg.id" class="message-card">
        <router-link :to="`/messages/${msg.id}`" class="title">
          <h2>{{ 'Message #' + msg.id }}</h2>
        </router-link>

        <p class="from"><strong>Type:</strong> {{ msg.type || 'Unknown' }}</p>
        <p class="from"><strong>Content:</strong> {{ msg.text || 'No content' }}</p>

        <!-- ✅ Safe and semantic participants list -->
        <div v-if="msg.participants && msg.participants.length">
          <h4>Participants:</h4>
          <ul>
            <li v-for="user in msg.participant" :key="user.id">
              User: {{ user.name || user.id }}
            </li>
          </ul>
        </div>

        <router-link :to="`/messages/update/${msg.id}`" class="edit">✏️ Edit</router-link>
      </div>
    </div>

    <p v-else>Loading messages...</p>
  </section>

  <section v-else class="messages-page">
    <div class="header">
      <h1>Please log in to view your messages.</h1>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useUserStore } from '~/stores/user'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const userStore = useUserStore()
const logged = ref(false)
const messages = ref([])

onMounted(async () => {
  try {
    // ✅ Ensure we load user data from store
    await userStore.loadUserId?.()

    // Try getting the ID from Pinia or localStorage fallback
    const id = userStore.userId || localStorage.getItem('userId')
    console.log("User ID:", id)

    // ✅ Safer condition — both must be valid
    if (id && id !== '0') {
      logged.value = true
    } else {
      throw new Error('User ID not found')
    }

    // ✅ Fetch messages
    const data = await fetchMessages(id)
    console.log("Messages response:", data)

    // ✅ Normalize data format (array or object)
    if (Array.isArray(data)) {
      messages.value = data
    } else if (data?.messages && Array.isArray(data.messages)) {
      messages.value = data.messages
    } else {
      console.warn('Unexpected API format, response:', data)
      messages.value = []
    }
  } catch (error) {
    console.error('Error fetching messages:', error)
    logged.value = false
  }
})

async function fetchMessages(id) {
  try {
    const myHeaders = new Headers({
      'Authorization': '1757002588886:7:2.16.27.0.17.62',
      'Content-Type': 'application/json'
    })

    const response = await fetch(`http://localhost:8080/messages/user/${id}`, {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status} - Failed to fetch messages`)
    }

    return await response.json()
  } catch (err) {
    console.error('API error:', err)
    throw err
  }
}
</script>

<style scoped>
.messages-page {
  max-width: 900px;
  margin: auto;
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.btn {
  background-color: #ca59c5;
  color: white;
  padding: 0.5rem 1rem;
  text-decoration: none;
  border-radius: 6px;
  font-weight: bold;
}

.message-card {
  background: #f9f9f9;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.message-card h2 {
  margin-bottom: 0.5rem;
  color: #ca59c5;
}

.message-card .from {
  font-size: 0.9rem;
  color: #777;
}

.message-card .preview {
  margin-top: 0.5rem;
  color: #333;
}

.edit {
  display: inline-block;
  margin-top: 0.5rem;
  color: #ca59c5;
  text-decoration: none;
  font-weight: bold;
}
</style>
