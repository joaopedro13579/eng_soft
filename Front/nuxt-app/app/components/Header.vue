<template>
  <header>
    <nav>
      <h1>User Story Hub</h1>
      <div class="nav-links">
        <div class="home-link"><a href="/">Home</a></div>
        <div class="enter-link"><a href="/user/signin">Enter</a></div>
        <div class="proj-link"><a href="/projects/read">My Projects</a></div>
        <div class="tasks-link"><a href="/messages/read">My Tasks</a></div>
        <div>
          <button @click="goToUser" class="user-btn">
            <img class="img" src="/src/user.jpeg" alt="User" />
          </button>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '~/stores/user'

const router = useRouter()
const userStore = useUserStore()

const goToUser = () => {
  const id = localStorage.getItem('userId') || userStore.userId
  if (!id || id == 0) {
    router.push('/user/signin')
  } else {
    router.push(`/user/${id}`)
  }
}
</script>

<style scoped>
header {
  background: #ca59c5e7;
  color: white;
  padding: 1rem;
  position: relative;
}

.img {
  width: 4rem;
  height: 4rem;
  border-radius: 50%;
  cursor: pointer;
  object-fit: cover;
}

nav {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

h1 {
  margin: 0;
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-links div a {
  color: white;
  text-decoration: none;
  font-weight: 500;
}

.nav-links div a:hover {
  text-decoration: underline;
}

.user-btn {
  background: none;
  border: none;
  padding: 0;
}
</style>
