<template>
    <Header/>
  <section class="project-form">
    <h1>Edit Project</h1>
    <form v-if="loaded" @submit.prevent="handleUpdate">
      <input v-model="name" type="text" placeholder="Project Name" required />
      <textarea v-model="description" placeholder="Project Description"></textarea>
      <button type="submit">Update</button>
    </form>
    <p v-else>Loading...</p>
  </section>
  <Footer/>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import Footer from '~/components/Footer.vue'
import Header from '~/components/Header.vue'

const route = useRoute()
const router = useRouter()
const id = route.params.id

const name = ref('')
const description = ref('')
const loaded = ref(false)

onMounted(async () => {
  try {
    const res = await fetch(`/api/projects/${id}`)
    const project = await res.json()
    name.value = project.name
    description.value = project.description
    loaded.value = true
  } catch (err) {
    console.error('Failed to fetch project:', err)
  }
})

const handleUpdate = async () => {
  try {
    await fetch(`/api/projects/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: name.value, description: description.value })
    })
    router.push('/projects')
  } catch (err) {
    console.error('Failed to update project:', err)
  }
}
</script>

<style scoped>
.project-form {
  max-width: 600px;
  margin: 4rem auto;
  padding: 2rem;
  background: #f9f9f9;
  border-radius: 12px;
}

.project-form h1 {
  color: #ca59c5;
  margin-bottom: 1rem;
}

.project-form input,
.project-form textarea {
  width: 100%;
  padding: 0.75rem;
  margin-bottom: 1rem;
  border-radius: 6px;
  border: 1px solid #ccc;
}

.project-form button {
  background: #ca59c5;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  cursor: pointer;
}
</style>
