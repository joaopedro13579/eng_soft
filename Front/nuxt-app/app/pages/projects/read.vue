<template>

    <Header/>
  <section v-if="logged" class="project-list">
    
    <div class="header">
      <h1>All Projects</h1>
      <router-link to="/projects/create" class="btn">+ New Project</router-link>
    </div>

    <div v-if="projects.length">
      <div v-for="project in projects" :key="project.id" class="project-card">
        <h2>{{ project.name }}</h2>
        <p>{{ project.description }}</p>
        <router-link :to="`/projects/edit/${project.id}`" class="edit">✏️ Edit</router-link>
      </div>
    </div>

    <p v-else>Loading projects...</p>
  </section>
  <section v-else class="project-list">
    <div class="header">
      <h1>Please log in to view your projects.</h1>
    </div>
  </section>
  <Footer/>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import Footer from '~/components/Footer.vue'
import Header from '~/components/Header.vue'
import { useUserStore } from '~/stores/user'

const projects = ref([])
const logged=ref(false);
const userStore = useUserStore()

onMounted(async () => {

  if (!userStore.userId) {
    userStore.loadUserId()
  }
  useUserStore().loadUserId()
  let id = userStore.userId
  if (id == 0) {
    logged.value = false
  } else {
    logged.value = true
  }
  try {

    const res = await fetch('/api/projects')
    projects.value = await res.json()
  } catch (err) {
    console.error('Error fetching projects:', err)
  }
})

</script>

<style scoped>
.project-list {
  max-width: 900px;
  margin: auto;
  padding: 2rem;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.btn {
  background-color: #ca59c5;
  color: white;
  padding: 0.5rem 1rem;
  text-decoration: none;
  border-radius: 6px;
  font-weight: bold;
}

.project-card {
  background: #f9f9f9;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.project-card h2 {
  margin-bottom: 0.5rem;
  color: #ca59c5;
}

.edit {
  display: inline-block;
  margin-top: 0.5rem;
  color: #ca59c5;
  text-decoration: none;
  font-weight: bold;
}
</style>
