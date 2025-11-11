<template>
  <Header />

  <section class="message-detail" v-if="message">
    <div class="header">
      <h1>Message #{{ message.id }}</h1>
      <router-link to="/messages/read" class="btn">← Back to Messages</router-link>
    </div>

    <div class="card">
      <p><strong>Type:</strong> {{ message.type || 'Unknown' }}</p>
      <p><strong>Content:</strong> {{ message.text || 'No content' }}</p>
      <p><strong>User ID:</strong> {{ message.userId || 'Unknown' }}</p>

      <div v-if="message.participant && message.participant.length">
        <h3>Participants</h3>
        <ul>
          <li v-for="user in message.participant" :key="user.id">
            ID: {{ user.id }}
            <span v-if="user.username"> — {{ user.username }}</span>
          </li>
        </ul>
      </div>
    </div>
  </section>

  <section v-else class="message-detail">
    <p>Loading message...</p>
  </section>

  <Footer />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const route = useRoute()
const message = ref(null)
onMounted(async () => {
  const id = route.params.id
  console.log("Fetching message ID:", id)

  try {
    message.value = await fetchMessage(id)
    console.log("Message before user fetch:", message.value)

    // ✅ Only fetch participants if they exist
    if (Array.isArray(message.value.participant) && message.value.participant.length) {
      // Fetch all participant user info in parallel
      const users = await Promise.all(
        message.value.participant.map(async (p) => {
          const userDetails = await fetchUser(p.id)
          return { ...p, ...userDetails } // merge old + new data
        })
      )

      // Replace participant array with full user data
      message.value.participant = users
    }

    console.log("Loaded message with participants:", message.value)
  } catch (err) {
    console.error("Error fetching message:", err)
  }
})

async function fetchUser(User){
    var myHeaders = new Headers();
    myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62");

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    let Us=await fetch("http://localhost:8080/users/"+User, requestOptions)
    return await Us.json()
}
async function fetchMessage(id) {
  try {
    const myHeaders = new Headers({
      'Authorization': '1757002588886:7:2.16.27.0.17.62',
      'Content-Type': 'application/json'
    })

    const response = await fetch(`http://localhost:8080/messages/${id}`, {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    })

    if (!response.ok) {
      throw new Error(`HTTP ${response.status} — failed to fetch message`)
    }

    return await response.json()
  } catch (err) {
    console.error('API error:', err)
    throw err
  }
}
</script>

<style scoped>
.message-detail {
  max-width: 800px;
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

.card {
  background: #f9f9f9;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.card p {
  margin-bottom: 0.75rem;
  color: #333;
}

h3 {
  margin-top: 1.5rem;
  color: #ca59c5;
}
</style>
