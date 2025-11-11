<template>
  <Header />

  <section class="edit-user">
    <h1>Edit User #{{ user.id }}</h1>

    <form @submit.prevent="saveUser">
      <label>Username:</label>
      <input type="text" v-model="user.username" placeholder="Username" required />
      <label>Password:</label>
      <input type="password" v-model="user.password" placeholder="New Password" />

      <button type="submit" class="save-btn">💾 Save User</button>
    </form>

    <p v-if="status" class="status">{{ status }}</p>

    <button type="button" class="delete-btn" @click="deleteUser">
      🗑️ Delete User
    </button>
  </section>

  <Footer />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { useRouter } from 'vue-router'


const Router = useRouter()
const route = useRoute()
const authToken = '1757002588886:7:2.16.27.0.17.62'
const status = ref('')

// 🧍 Reactive user object
const user = reactive({
  id: null,
  username: '',
  password: ''
})

// ✅ Fetch user data on mount
onMounted(async () => {
  const id = route.params.id
  try {
    const res = await fetch(`http://localhost:8080/users/${id}`, {
      headers: { Authorization: authToken }
    })

    if (!res.ok) throw new Error(await res.text())

    const data = await res.json()
    Object.assign(user, data)

    status.value = '✅ User data loaded successfully.'
  } catch (err) {
    console.error('Failed to load user:', err)
    status.value = `❌ Failed to load user: ${err.message}`
  }
})

// ✅ Save user (PUT)
async function saveUser() {
  if (!user.username) {
    status.value = '⚠️ Username is required.'
    return
  }

  try {
    const myHeaders = new Headers()
    myHeaders.append('Content-Type', 'application/json')
    myHeaders.append('Authorization', authToken)

    const raw = JSON.stringify({
      username: user.username,
      email: user.email,
      password: user.password
    })

    const res = await fetch(`http://localhost:8080/users/${user.id}`, {
      method: 'PUT',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    })

    if (!res.ok) throw new Error(await res.text())

    status.value = '✅ User successfully updated!'
  } catch (err) {
    console.error('Save failed:', err)
    status.value = `❌ Failed to update user: ${err.message}`
  }
}

// ✅ Delete user (DELETE)
async function deleteUser() {
      var requestOptions = {
      method: 'DELETE',
      redirect: 'follow'
    };
    
    fetch("http://localhost:8080/users/"+user.id+"?Authorization=1757005097588:3:50.2.1.14.12.79", requestOptions)

    localStorage.setItem('userId', '0')
    Router.push('/user/signin')
}
</script>

<style scoped>
.edit-user {
  max-width: 600px;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
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

input[type='text'],
input[type='email'],
input[type='password'] {
  width: 100%;
  border: 1.5px solid #ddd;
  border-radius: 10px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

input:focus {
  outline: none;
  border-color: #ca59c5;
  box-shadow: 0 0 0 3px rgba(202, 89, 197, 0.15);
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
  transition: background-color 0.2s, transform 0.1s;
}

.save-btn:hover {
  background-color: #b44fb0;
  transform: translateY(-1px);
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
