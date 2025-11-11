<template>
  <Header />

  <section class="edit-message">
    <h1>Edit Message #{{ message.id }}</h1>

    <form @submit.prevent="saveMessage">
      <label>Text:</label>
      <textarea v-model="message.text" rows="3" />

      <label>Type:</label>
      <input type="text" v-model="message.type" placeholder="e.g. historia de usuario" />

      <label>User ID:</label>
      <input type="number" v-model.number="message.userId" placeholder="User ID" />

      <label>Project ID:</label>
      <input type="number" v-model.number="message.projectId" placeholder="Project ID" />

      <h3>Participants (IDs)</h3>
      <div
        v-for="(p, index) in message.participant"
        :key="index"
        class="id-input"
      >
        <input type="number" v-model.number="p.id" placeholder="Participant ID" />
        <button type="button" @click="removeParticipant(index)">❌</button>
      </div>
      <button type="button" @click="addParticipant">+ Add Participant</button>

      <button type="submit" class="save-btn">💾 Save Message</button>
    </form>

    <p v-if="status" class="status">{{ status }}</p>

    <button type="button" class="delete-btn" @click="deleteMessage">
      🗑️ Delete Message
    </button>
  </section>

  <Footer />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const route = useRoute()
const authToken = '1757002588886:7:2.16.27.0.17.62'
const status = ref('')

const message = reactive({
  id: null,
  text: '',
  type: '',
  userId: null,
  projectId: null,
  participant: []
})

// ✅ Fetch single message
async function getMessage() {
  const id = route.params.id
  const myHeaders = new Headers()
  myHeaders.append('Authorization', authToken)

  const requestOptions = {
    method: 'GET',
    headers: myHeaders,
    redirect: 'follow'
  }

  const res = await fetch(`http://localhost:8080/messages/${id}`, requestOptions)
  if (!res.ok) throw new Error(await res.text())
  return res.json()
}

// ✅ Load message on mount
onMounted(async () => {
  try {
    const res = await getMessage()
    Object.assign(message, res)

    if (!Array.isArray(message.participant)) message.participant = []
    status.value = '✅ Message data loaded successfully.'
  } catch (err) {
    console.error('Failed to load message:', err)
    status.value = `❌ Failed to load message: ${err.message}`
  }
})

// ✅ Add/remove participants dynamically
function addParticipant() {
  message.participant.push({ id: null })
}
function removeParticipant(index) {
  message.participant.splice(index, 1)
}

// ✅ Save message (PUT)
async function saveMessage() {
  if (!message.text || !message.type) {
    status.value = '⚠️ Please fill out text and type before saving.'
    return
  }

  try {
    const myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')
    myHeaders.append('Authorization', authToken)

    const raw = JSON.stringify({
      text: message.text,
      type: message.type,
      userId: Number(message.userId),       // ✅ Corrected field
      projectId: Number(message.projectId), // ✅ Always number
      participant: message.participant
        .filter(p => p.id != null && p.id !== '')
        .map(p => ({ id: Number(p.id) }))   // ✅ Only IDs
    })

    console.log('📦 PUT body:', raw)

    const requestOptions = {
      method: 'PUT',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    }

    const res = await fetch(`http://localhost:8080/messages/${message.id}`, requestOptions)
    const text = await res.text()
    console.log('📥 Response:', text)

    if (!res.ok) throw new Error(text)
    status.value = '✅ Message successfully updated!'
  } catch (err) {
    console.error('Save failed:', err)
    status.value = `❌ Failed to update message: ${err.message}`
  }
}

// ✅ Delete message (DELETE)
async function deleteMessage() {
  if (!message.id) {
    status.value = '⚠️ Message ID is missing.'
    return
  }

  if (!confirm('Are you sure you want to delete this message?')) return

  try {
    const res = await fetch(`http://localhost:8080/messages/${message.id}`, {
      method: 'DELETE',
      headers: { Authorization: authToken }
    })

    if (!res.ok) throw new Error(await res.text())
    status.value = '✅ Message successfully deleted.'
  } catch (err) {
    console.error('Delete failed:', err)
    status.value = `❌ Failed to delete message: ${err.message}`
  }
}
</script>

<style scoped>
.edit-message {
  max-width: 800px;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  font-family: 'Inter', system-ui, sans-serif;
}

h1 {
  font-size: 1.8rem;
  color: #ca59c5;
  text-align: center;
  margin-bottom: 1.5rem;
}

form {
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
}

label {
  font-weight: 600;
  color: #333;
  margin-bottom: 0.25rem;
}

textarea, input[type='text'], input[type='number'] {
  width: 100%;
  border: 1.5px solid #ddd;
  border-radius: 10px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

textarea:focus, input:focus {
  outline: none;
  border-color: #ca59c5;
  box-shadow: 0 0 0 3px rgba(202, 89, 197, 0.15);
}

h3 {
  font-size: 1.2rem;
  color: #555;
  margin-top: 1.5rem;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 0.5rem;
}

.id-input {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #fafafa;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  border: 1px solid #eee;
}

.id-input input {
  flex: 1;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.id-input button {
  background: transparent;
  border: none;
  color: #d33;
  cursor: pointer;
  font-size: 1.2rem;
}

button[type='button'] {
  align-self: flex-start;
  background-color: #f3e0f2;
  color: #ca59c5;
  border: 1px solid #e3c6e2;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

button[type='button']:hover {
  background-color: #ca59c5;
  color: white;
}

.save-btn {
  align-self: center;
  background-color: #ca59c5;
  color: white;
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 10px;
  font-weight: bold;
  cursor: pointer;
  font-size: 1rem;
  margin-top: 2rem;
}

.save-btn:hover {
  background-color: #b44fb0;
}

.status {
  margin-top: 1.5rem;
  text-align: center;
  font-weight: 600;
  font-size: 1rem;
}

.delete-btn {
  display: block;
  margin: 2rem auto;
  background-color: #fdd;
  color: #d33;
  border: 1px solid #d33;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-weight: bold;
}

.delete-btn:hover {
  background-color: #d33;
  color: white;
}
</style>
