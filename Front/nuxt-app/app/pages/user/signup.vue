<template>
  <Header />

  <section class="auth">
    <div class="auth-card">
      <h1>Sign Up</h1>
      <form @submit.prevent="handleSignUp">
        <input type="email" v-model="email" placeholder="Email" required />
        <input type="password" v-model="password" placeholder="Password" required />
        <input type="password" v-model="confirmPassword" placeholder="Confirm Password" required />
        <button type="submit">Sign Up</button>
        <p class="switch">Already have an account?
          <NuxtLink to="/user/signin">Sign In</NuxtLink>
        </p>
      </form>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { ref } from 'vue'

const email = ref('')
const password = ref('')
const confirmPassword = ref('')

const handleSignUp = () => {
  if (password.value !== confirmPassword.value) {
    alert("Passwords do not match!")
    return
  }
  // Replace with real signup logic
  console.log('Signing up with:', email.value, password.value)
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
</style>
