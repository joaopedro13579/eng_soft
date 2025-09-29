<template>
  <Header />

  <section class="auth">
    <div class="auth-card">
      <h1>Sign In</h1>
      <form @submit.prevent="handleSignIn">
        <input type="name" v-model="name" placeholder="Name" required />
        <input type="password" v-model="password" placeholder="Password" required />
        <button type="submit">Sign In</button>

        <p class="switch">Don't have an account?
          <NuxtLink to="/user/signup">Sign Up</NuxtLink>
        </p>
        <button @click="erro = true">Show Error</button>

      </form>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { ref } from 'vue'
import { useUserStore } from '~/stores/user'
import { useRouter } from 'vue-router'

const Router = useRouter()
const email = ref('')
const password = ref('')
const erro = ref(false)
const handleSignIn = async () => {
  localStorage.setItem('userId', 1)
  const userStore = useUserStore()
  userStore.setUserId(1)
  console.log("Signing in with", email.value, password.value)
  Router.push('/projects/read')
  try {
    // await some API call to validate login
    const response = await fakeLoginApi(email.value, password.value)
    if (response.success) {
      erro.value = false
      // redirect or set user data
    } else {
      erro.value = true
    }
  } catch (err) {
    erro.value = true
  }

}
</script>

<style scoped>
.auth {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 4rem 1rem;
}

.auth-card {
  background: #f9f9f9;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  max-width: 400px;
  width: 100%;
  text-align: center;
}

.auth-card h1 {
  color: #ca59c5;
  margin-bottom: 1.5rem;
}

.auth-card form {
  display: flex;
  flex-direction: column;
}

.auth-card input {
  padding: 0.75rem;
  margin-bottom: 1rem;
  border: 1px solid #ccc;
  border-radius: 6px;
}

.auth-card button {
  background-color: #ca59c5;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
}

.auth-card button:hover {
  background-color: #a8459d;
}

.switch {
  margin-top: 1rem;
}

.switch a {
  color: #ca59c5;
  font-weight: bold;
  text-decoration: none;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>
