<template>
  <Header />

  <section class="user-profile">
    <h1>User Profile</h1>

    <div v-if="loading" class="loading">Loading user data...</div>
    <div v-else-if="error" class="error">{{ error }}</div>

    <div v-else class="profile-card">
      <img
        class="avatar"
        src="/src/user.jpeg"
        alt="User Avatar"
      />

      <h2>{{ user.username || 'Unnamed User' }}</h2>
      <p><strong>ID:</strong> {{ user.id }}</p>
      <p><strong>Email:</strong> {{ user.email || 'Not provided' }}</p>
      <p><strong>Role:</strong> {{ user.role || 'Member' }}</p>

      <div v-if="user.createdAt">
        <p><strong>Joined:</strong> {{ formatDate(user.createdAt) }}</p>
      </div>

      <!-- ✅ Logout button -->
      <button class="logout-btn" @click="logout">🚪 Log Out</button>
      <router-link :to="`/user/update/${user.id}`" class="name"><h2>change password</h2></router-link>
        <!-- Confirmation message -->
      <p v-if="statusMessage" class="status">{{ statusMessage }}</p>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { useUserStore } from '~/stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const authToken = '1757002588886:7:2.16.27.0.17.62'
const user = ref({})
const loading = ref(true)
const error = ref(null)
const statusMessage = ref('')

// ✅ Fetch user info
onMounted(async () => {
  const id = route.params.id
  try {
    const res = await fetch(`http://localhost:8080/users/${id}`, {
      headers: { Authorization: authToken },
    })
    if (!res.ok) throw new Error(`Server returned ${res.status}`)
    const data = await res.json()
    user.value = data
  } catch (err) {
    console.error('Failed to load user:', err)
    error.value = `❌ Failed to load user: ${err.message}`
  } finally {
    loading.value = false
  }
})

// ✅ Logout (checkout)
function logout() {
  localStorage.removeItem('userId')
  userStore.$reset?.() // if Pinia store supports reset
  statusMessage.value = '👋 You have logged out successfully! Redirecting...'

  setTimeout(() => {
    router.push('/user/signin')
  }, 1500)
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleDateString()
}
</script>

<style scoped>
.user-profile {
  max-width: 600px;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  font-family: 'Inter', system-ui, sans-serif;
  text-align: center;
}

h1 {
  color: #ca59c5;
  margin-bottom: 1.5rem;
}

.loading,
.error {
  text-align: center;
  font-size: 1.1rem;
  color: #777;
}

.error {
  color: red;
}

.profile-card {
  padding: 1.5rem;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  margin-bottom: 1rem;
  object-fit: cover;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

p {
  margin: 0.5rem 0;
  color: #444;
  font-size: 1rem;
}

/* ✅ Logout button styling */
.logout-btn {
  margin-top: 1.5rem;
  background-color: #ca59c5;
  color: white;
  border: none;
  border-radius: 8px;
  padding: 0.75rem 1.5rem;
  font-weight: bold;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.logout-btn:hover {
  background-color: #b44fb0;
}

.status {
  margin-top: 1rem;
  color: green;
  font-weight: bold;
}
</style>
