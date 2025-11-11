<template>
  <Header />

  <section class="auth">
    <div class="auth-card">
      <h1>Sign Up</h1>
      <form @submit.prevent="handleSignUp">
        <input type="text" v-model="nome" placeholder="Name" required />
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
import { useRouter } from 'vue-router'

const Router = useRouter()
const nome = ref('')
const password = ref('')
const confirmPassword = ref('')

const handleSignUp = () => {
  if (password.value !== confirmPassword.value) {
    alert("Passwords do not match!")
    return
  }
  // Replace with real signup logic
  console.log('Signing up with:', nome.value, password.value)
  registerUser(nome, password)
  Router.forward('/user/signin')
}
async function registerUser(nome, password) {
  try {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");
    myHeaders.append("Authorization", "1757005097588:3:50.2.1.14.12.79");

    var raw = JSON.stringify({
      "username": nome.value,
      "password": password.value
    });

    var requestOptions = {
      method: 'POST',
      headers: myHeaders,
      body: raw,
      redirect: 'follow'
    };

    fetch("http://localhost:8080/users/", requestOptions)
    Router.push("/user/signin")
  } catch (error) {
    console.log('Error registering user:', error)
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
</style>
