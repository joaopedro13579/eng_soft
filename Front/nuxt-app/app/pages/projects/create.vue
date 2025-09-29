<template>
    <Header/>
  <section class="project-form">
    <h1>Create Project</h1>
    <form @submit.prevent="handleSubmit">
      <input v-model="name" type="text" placeholder="Project Name" required />
      <textarea v-model="description" placeholder="Project Description"></textarea>
      <button type="submit">Create</button>
    </form>
  </section>
  <Footer/>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Footer from '~/components/Footer.vue'
import Header from '~/components/Header.vue'

const name = ref('')
const description = ref('')
const router = useRouter()

const handleSubmit = async () => {
  try {
    await fetch('/api/projects', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: name.value, description: description.value })
    })
    router.push('/projects')
  } catch (err) {
    alert('Failed to create project.')
    console.error(err)
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
