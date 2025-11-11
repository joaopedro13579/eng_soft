<template>
  <Header />

  <section class="edit-project">
    <h1>Edit Project #{{ project.id }}</h1>

    <form @submit.prevent="saveProject">
      <label>Description:</label>
      <textarea v-model="project.description" rows="3" />

      <h3>Participants (IDs)</h3>
      <div
        v-for="(p, index) in project.participant"
        :key="index"
        class="id-input"
      >
        <input type="number" v-model.number="p.id" placeholder="Participant ID" />
        <button type="button" @click="removeParticipant(index)">❌</button>
      </div>
      <button type="button" @click="addParticipant">+ Add Participant</button>

      <h3>Messages (IDs)</h3>
      <div v-for="(m, index) in project.messages" :key="index" class="id-input">
        <input type="number" v-model.number="m.id" placeholder="Message ID" />
        <button type="button" @click="removeMessage(index)">❌</button>
      </div>
      <button type="button" @click="addMessage">+ Add Message</button>

      <button type="submit" class="save-btn">💾 Save Project</button>
    </form>

    <p v-if="message" class="message">{{ message }}</p>
    <button type="button" class="delete-btn" @click="deleteProject">
  🗑️ Delete Project
</button>
  </section>

  <Footer />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const route = useRoute()
const message = ref('')
const authToken = '1757002588886:7:2.16.27.0.17.62'

// Reactive project object
const project = reactive({
  id: null,
  description: '',
  participant: [],
  messages: []
})
function deleteProject() {
  if (!project.id) {
    message.value = '⚠️ Project ID is missing.'
    return
  }

  if (!confirm('Are you sure you want to delete this project? This action cannot be undone.')) {
    return
  }

  var myHeaders = new Headers();
  myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62");

  var requestOptions = {
    method: 'DELETE',
    headers: myHeaders,
    redirect: 'follow'
  };

  fetch("http://localhost:8080/projects/"+ project.id, requestOptions)
    .then(response => {
      if (response.ok) {
        message.value = '✅ Project successfully deleted.'
        // Optionally redirect or clear the form here
      } else {
        return response.text().then(text => { throw new Error(text) })
      }
    })
    .catch(error => {
      console.error('Error deleting project:', error)
      message.value = `❌ Failed to delete project: ${error.message}`
    })
}
// 🔹 Fetch project data on mount
onMounted(async () => {
  const id = route.params.id
  try {
    const res = await fetch(`http://localhost:8080/projects/${id}`, {
      headers: { Authorization: authToken }
    })

    if (!res.ok) throw new Error(await res.text())

    const data = await res.json()
    if (!data || typeof data !== 'object') throw new Error('Invalid data format')

    Object.assign(project, data)
    message.value = '✅ Project data loaded successfully.'
  } catch (err) {
    console.error('Failed to load project:', err)
    message.value = `❌ Failed to load project: ${err.message}`
  }
})

// 🔹 Dynamic form helpers
function addParticipant() {
  project.participant.push({ id: null })
}
function removeParticipant(index) {
  project.participant.splice(index, 1)
}
function addMessage() {
  project.messages.push({ id: null })
}
function removeMessage(index) {
  project.messages.splice(index, 1)
}

// 🔹 Save updated project (full replace behavior)
async function saveProject() {
  if (!project.id || !project.description) {
    message.value = '⚠️ Please fill out the description before saving.'
    return
  }

  try {
    const myHeaders = new Headers({
      'Content-Type': 'application/json',
      Authorization: authToken
    })

    const body = JSON.stringify({
      description: project.description,
      participant: project.participant
        .filter(p => p.id != null && p.id !== '')
        .map(p => ({ id: Number(p.id) })),
      messages: project.messages
        .filter(m => m.id != null && m.id !== '')
        .map(m => ({ id: Number(m.id) }))
    })

    const res = await fetch(`http://localhost:8080/projects/${project.id}`, {
      method: 'PUT',
      headers: myHeaders,
      body
    })

    if (!res.ok) {
      const text = await res.text()
      throw new Error(text)
    }

    message.value = '✅ Project successfully updated to match server data!'
  } catch (err) {
    console.error('Save failed:', err)
    message.value = `❌ Failed to update project: ${err.message}`
  }
}
</script>

<style scoped>
.edit-project {
  max-width: 800px;
  margin: 3rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
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

textarea {
  width: 100%;
  border: 1.5px solid #ddd;
  border-radius: 10px;
  padding: 0.75rem;
  font-size: 1rem;
  transition: border-color 0.2s ease;
}

textarea:focus {
  outline: none;
  border-color: #ca59c5;
  box-shadow: 0 0 0 3px rgba(202, 89, 197, 0.15);
}

h3 {
  font-size: 1.2rem;
  color: #555;
  margin-top: 1.5rem;
  border-bottom: 2px solid #f0f0f0;
  padding-bottom: 0.5rem;
}

.id-input {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: #fafafa;
  padding: 0.5rem 0.75rem;
  border-radius: 8px;
  border: 1px solid #eee;
  transition: background 0.2s;
}

.id-input:hover {
  background: #f5f0f5;
}

.id-input input {
  flex: 1;
  padding: 0.5rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  transition: border-color 0.2s;
}

.id-input input:focus {
  outline: none;
  border-color: #ca59c5;
}

.id-input button {
  background: transparent;
  border: none;
  color: #d33;
  cursor: pointer;
  font-size: 1.2rem;
  transition: transform 0.2s;
}

.id-input button:hover {
  transform: scale(1.2);
}

button[type='button'] {
  align-self: flex-start;
  background-color: #f3e0f2;
  color: #ca59c5;
  border: 1px solid #e3c6e2;
  border-radius: 8px;
  padding: 0.5rem 1rem;
  cursor: pointer;
  font-weight: 600;
  transition: all 0.2s;
}

button[type='button']:hover {
  background-color: #ca59c5;
  color: white;
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

.message {
  margin-top: 1.5rem;
  text-align: center;
  font-weight: 600;
  font-size: 1rem;
}

.message:before {
  content: "💬 ";
}
</style>
