<template>
  <Header />

  <section class="message-page">
    <div class="message-card">
      <h1>Create Message</h1>

      <form @submit.prevent="sendMessage">
        <label for="recipient">Recipient</label>
        <select v-model="recipient" id="recipient" required>
          <option disabled value="">Select a user</option>
          <option v-for="user in users" :key="user.id" :value="user.name">
            {{ user.name }}
          </option>
        </select>

        <input
          v-model="title"
          type="text"
          placeholder="Message Title"
          required
        />

        <textarea
          v-model="body"
          rows="6"
          placeholder="Write your message..."
          required
        ></textarea>

        <button type="submit">Send Message</button>

        <p v-if="success" class="success-msg">✅ Message sent to {{ recipient }}!</p>
      </form>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import { ref } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

// Hardcoded list of users — could be fetched or replaced with a store
const users = [
  { id: 1, name: 'Alice Johnson' },
  { id: 2, name: 'Bob Smith' },
  { id: 3, name: 'Charlie Davis' }
]

const title = ref('')
const body = ref('')
const recipient = ref('')
const success = ref(false)

const sendMessage = () => {
  console.log('Message Sent:', {
    to: recipient.value,
    title: title.value,
    body: body.value
  })

  success.value = true

  setTimeout(() => {
    title.value = ''
    body.value = ''
    recipient.value = ''
    success.value = false
  }, 2000)
}
</script>

<style scoped>
.message-page {
  display: flex;
  justify-content: center;
  padding: 4rem 1rem;
}

.message-card {
  background: #f9f9f9;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  max-width: 600px;
  width: 100%;
}

.message-card h1 {
  color: #ca59c5;
  margin-bottom: 1.5rem;
  text-align: center;
}

.message-card form {
  display: flex;
  flex-direction: column;
}

.message-card select,
.message-card input,
.message-card textarea {
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.message-card button {
  background-color: #ca59c5;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.message-card button:hover {
  background-color: #a8459d;
}

.success-msg {
  margin-top: 1rem;
  color: green;
  font-weight: bold;
  text-align: center;
}
</style>
