<template>
  <Header />

  <section class="project-view">
    <h1>Create New Project</h1>

    <form @submit.prevent="createProject">
      <label for="projectName">Description</label>
      <input
        type="text"
        id="projectName"
        v-model="project.name"
        placeholder="Enter project name"
        required
      />

      <label for="participantSearch">Add Participants by Name</label>
      <div class="search-container">
        <input
          type="text"
          v-model="searchQuery"
          placeholder="Enter name to fetch ID"
        />
        <button type="button" @click="addParticipantByName">Add</button>
      </div>

      <div v-if="project.participant.length" class="selected-participants">
        <h4>Participants:</h4>
        <ul>
          <li v-for="p in project.participant" :key="p.id">
            {{ p.username }} (ID: {{ p.id }})
            <button type="button" @click="removeParticipant(p)">❌</button>
          </li>
        </ul>
      </div>

      <button type="submit">Create Project</button>
    </form>

    <p v-if="status.message" :style="{ color: status.success ? 'green' : 'red' }">
      {{ status.message }}
    </p>
  </section>

  <Footer />
</template>

<script setup>
import { reactive, ref } from 'vue'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const authToken = "1757002588886:7:2.16.27.0.17.62"

const project = reactive({
  name: '',
  participant: []
})

const searchQuery = ref('')
const status = reactive({ message: '', success: false })

// Fetch user by name and add to participants
async function addParticipantByName() {
  if (!searchQuery.value.trim()) return;

  try {
    const res = await fetch(`http://localhost:8080/users/by-name/${searchQuery.value}`, {
      headers: { 'Authorization': authToken }
    });
    if (!res.ok) throw new Error(`Server returned ${res.status}`);

    const data = await res.json();

    // Handle both single object and array response
    const users = Array.isArray(data) ? data : [data];

    if (users.length === 0) {
      status.message = `No users found with name "${searchQuery.value}"`;
      status.success = false;
      return;
    }

    users.forEach(user => {
      if (!project.participant.find(p => p.id === user.id)) {
        project.participant.push(user);
      }
    });

    searchQuery.value = '';
    status.message = '';
  } catch (err) {
    console.error(err);
    status.message = `Error fetching user: ${err.message}`;
    status.success = false;
  }
}


function removeParticipant(user) {
  project.participant = project.participant.filter(p => p.id !== user.id)
}

async function createProject() {
  if (!project.name) {
    status.message = 'Project name is required'
    status.success = false
    return
  }
  console.log("Creating project:", project)
  try {
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62");

  var raw = JSON.stringify({
    "id": 1,
    "description": project.name,
    "participant": project.participant,
    "messages": []
});

var requestOptions = {
  method: 'POST',
  headers: myHeaders,
  body: raw,
  redirect: 'follow'
};

fetch("http://localhost:8080/projects/", requestOptions)
  .then(response => response.text())
  .then(result => console.log(result))
  .catch(error => console.log('error', error));
  } catch (err) {
    console.error(err)
    status.message = `Failed to create project: ${err.message}`
    status.success = false
  }
}
</script>

<style scoped>
.project-view {
  max-width: 600px;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  font-family: 'Inter', system-ui, sans-serif;
}

h1 { color: #ca59c5; text-align: center; margin-bottom: 1.5rem; }
label { display: block; margin-top: 1rem; font-weight: bold; }
input[type="text"] { width: 100%; padding: 0.5rem; margin-top: 0.25rem; border: 1px solid #ccc; border-radius: 8px; box-sizing: border-box; }
button { margin-top: 1rem; background-color: #ca59c5; color: white; padding: 0.5rem 1rem; border: none; border-radius: 8px; font-weight: bold; cursor: pointer; }
button:hover { background-color: #b44fb0; }

.search-container { display: flex; gap: 0.5rem; margin-top: 0.25rem; }
.search-container input { flex: 1; }

.selected-participants ul { list-style: none; padding: 0; }
.selected-participants li { display: flex; justify-content: space-between; align-items: center; padding: 0.25rem 0; }
.selected-participants button { background: none; color: red; font-size: 1rem; border: none; cursor: pointer; }
</style>
