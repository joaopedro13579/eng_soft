<Header/>
<template>
<Header/>
  <section class="landing">
    <div class="hero">
      <h1>UserStoryHub</h1>
      <p>A modern tool to manage user stories with full control over users, messages, and projects.</p>
      <NuxtLink to="/projects/read" class="cta">Get Started</NuxtLink>
    </div>

    <div class="features">
      <div class="feature-card">
        <h2>📋 Projects</h2>
        <p>Create and manage projects to organize user stories efficiently.</p>
        <NuxtLink to="/projects/read">Manage Projects →</NuxtLink>
      </div>

      <div class="feature-card">
        <h2>💬 Messages</h2>
        <p>Communicate effectively with your team using integrated message threads.</p>
        <NuxtLink to="/messages/read">Go to Messages →</NuxtLink>
      </div>

      <div class="feature-card">
        <h2>👥 Users</h2>
        <p>Assign users to projects and track their progress in real time.</p>
        <NuxtLink to="/projects/read">Manage Users →</NuxtLink>
      </div>
    </div>
  </section>
<Footer/>
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { useUserStore } from '~/stores/user'
import { onMounted } from 'vue'

const userStore = useUserStore()

onMounted(() => {
  // If no user ID saved yet, set it to 1 for testing
  userStore.loadUserId()
  localStorage.setItem('userId', 0)
  if (!userStore.userId) {
    userStore.setUserId(0)
  }
})
</script>

<style scoped>
.landing {
  padding: 3rem 1.5rem;
  max-width: 1200px;
  margin: auto;
}

.hero {
  text-align: center;
  margin-bottom: 4rem;
}

.hero h1 {
  font-size: 3rem;
  color: #ca59c5;
}

.hero p {
  font-size: 1.25rem;
  color: #555;
  margin: 1rem 0 2rem;
}

.cta {
  background-color: #ca59c5;
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition: background 0.3s;
}

.cta:hover {
  background-color: #a8459d;
}

.features {
  display: flex;
  flex-wrap: wrap;
  gap: 2rem;
  justify-content: center;
}

.feature-card {
  flex: 1 1 300px;
  background: #f9f9f9;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
}

.feature-card h2 {
  color: #ca59c5;
  margin-bottom: 0.5rem;
}

.feature-card p {
  color: #666;
  margin-bottom: 1rem;
}

.feature-card a {
  text-decoration: none;
  color: #ca59c5;
  font-weight: bold;
}
</style>