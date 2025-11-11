<template>
  <Header />

  <section class="message-view">
    <h1>Create New Message</h1>

    <form @submit.prevent="createMessage">
      <label for="messageText">Content</label>
      <textarea
        id="messageText"
        v-model="message.text"
        rows="5"
        placeholder="Write your message..."
        required
      ></textarea>

      <label for="messageType">Type</label>
      <input
        type="text"
        id="messageType"
        v-model="message.type"
        placeholder="e.g. historia de usuario"
        required
      />

      <label for="participantSearch">Add Participants by Name</label>
      <div class="search-container">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Enter name to fetch ID"
        />
        <button type="button" @click="addParticipantByName">Add</button>
      </div>

      <div v-if="message.participant.length" class="selected-participants">
        <h4>Participants:</h4>
        <ul>
          <li v-for="p in message.participant" :key="p.id">
            {{ p.username || 'User' }} (ID: {{ p.id }})
            <button type="button" @click="removeParticipant(p)">❌</button>
          </li>
        </ul>
      </div>

      <button type="submit">Send Message</button>
    </form>

    <p v-if="status.message" :style="{ color: status.success ? 'green' : 'red' }">
      {{ status.message }}
    </p>
  </section>

  <Footer />
</template>
<script setup>
import { reactive, ref } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { useUserStore } from '~/stores/user'

const userStore = useUserStore()
const authToken = '1757002588886:7:2.16.27.0.17.62'

const message = reactive({
  text: '',
  type: '',
  participant: []
})

const searchQuery = ref('')
const status = reactive({ message: '', success: false })

// ✅ Fetch user by name and add to participants
async function addParticipantByName() {
  if (!searchQuery.value.trim()) return

  try {
    const res = await fetch(`http://localhost:8080/users/by-name/${searchQuery.value}`, {
      headers: { Authorization: authToken }
    })

    if (!res.ok) throw new Error(`Server returned ${res.status}`)

    const data = await res.json()
    const users = Array.isArray(data) ? data : [data]

    if (users.length === 0) {
      status.message = `No users found with name "${searchQuery.value}"`
      status.success = false
      return
    }

    users.forEach(user => {
      if (!message.participant.find(p => p.id === user.id)) {
        message.participant.push({ id: user.id, username: user.username })
      }
    })

    searchQuery.value = ''
    status.message = ''
  } catch (err) {
    console.error('Error fetching user:', err)
    status.message = `Error fetching user: ${err.message}`
    status.success = false
  }
}

// ✅ Remove participant
function removeParticipant(user) {
  message.participant = message.participant.filter(p => p.id !== user.id)
}

// ✅ Send message to backend
async function createMessage() {
  if (!message.text || !message.type) {
    status.message = 'Message content and type are required.'
    status.success = false
    return
  }

  try {
    const userId = userStore.userId || localStorage.getItem('userId')
    if (!userId) throw new Error('User not logged in')

    const res = await postMessage(userId)

    if (!res.ok) throw new Error(`HTTP ${res.status} - Failed to create message`)

    status.message = '✅ Message sent successfully!'
    status.success = true

    // reset form
    message.text = ''
    message.type = ''
    message.participant = []
  } catch (err) {
    console.error('Error creating message:', err)
    status.message = `Failed to send message: ${err.message}`
    status.success = false
  }
}

// ✅ Handles the actual API call
async function postMessage(userId) {
  const myHeaders = new Headers()
  myHeaders.append('Content-Type', 'application/json')
  myHeaders.append('Auth', authToken)

  // 🔥 EXACT JSON format as your working backend expects
  const raw = JSON.stringify({
    text: message.text,
    type: message.type,
    userId: userId.toString(),
    projectId: "2",
    participant: message.participant.map(p => ({ id: p.id }))
  })

  console.log("📦 Sending body:", raw)

  const requestOptions = {
    method: 'POST',
    headers: myHeaders,
    body: raw,
    redirect: 'follow'
  }

  const response = await fetch('http://localhost:8080/messages/', requestOptions)
  return response
}
</script>

<style scoped>
.message-view {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  font-family: 'Inter', system-ui, sans-serif;
}

h1 {
  color: #ca59c5;
  text-align: center;
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-top: 1rem;
  font-weight: bold;
}

input[type="text"], textarea {
  width: 100%;
  padding: 0.5rem;
  margin-top: 0.25rem;
  border: 1px solid #ccc;
  border-radius: 8px;
  box-sizing: border-box;
}

button {
  margin-top: 1rem;
  background-color: #ca59c5;
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

button:hover {
  background-color: #b44fb0;
}

.search-container {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.25rem;
}

.search-container input {
  flex: 1;
}

.selected-participants ul {
  list-style: none;
  padding: 0;
}

.selected-participants li {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.25rem 0;
}

.selected-participants button {
  background: none;
  color: red;
  font-size: 1rem;
  border: none;
  cursor: pointer;
}
</style>
