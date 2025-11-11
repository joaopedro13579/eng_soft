<template>
  <Header />

  <section v-if="logged" class="project-list">
    <div class="header">
      <h1>All Projects</h1>
      <router-link to="/projects/create" class="btn">+ New Project</router-link>
    </div>

    <div v-if="projects.length">
      <div v-for="project in projects" :key="project.id" class="project-card">

        <router-link :to="`/projects/${project.id}`" class="name"><h2>{{ project.name || ('Project #' + project.id) }}</h2></router-link>
        <p>{{ project.description }}</p>
        <router-link :to="`/projects/update/${project.id}`" class="edit">✏️ Edit</router-link>
      </div>
    </div>

    <p v-else>Loading projects...</p>
  </section>

  <section v-else class="project-list">
    <div class="header">
      <h1>Please log in to view your projects.</h1>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useUserStore } from '~/stores/user'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const userStore = useUserStore()
const logged = ref(false)
const projects = ref([])

onMounted(async () => {
  try {
    await userStore.loadUserId?.()
    const id = userStore.userId || localStorage.getItem('userId')
    console.log("User ID:", id)

    if (!id) throw new Error('User ID not found')

    const response = await apifetch(id)
    const data = await response.json()
    console.log("Full response:", data)

    // ✅ Handle both object and array formats
    if (Array.isArray(data)) {
      projects.value = data
    } else if (data?.projects) {
      projects.value = data.projects
    } else {
      throw new Error("Projects not found in response")
    }

    logged.value = true
  } catch (error) {
    console.error('Error fetching projects:', error)
    logged.value = false
  }
})

async function apifetch(id) {
  try {
    const myHeaders = new Headers()
    myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62")

    const requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    }

    const response = await fetch(`http://localhost:8080/projects/user/${id}`, requestOptions)
    return response
  } catch (err) {
    console.error('API error:', err)
    throw err
  }
}
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
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
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
