<template>
  <Header />

  <section v-if="projectLoaded" class="project-view">
    <h1>{{ project.name || 'Project #' + project.id }}</h1>

    <p><strong>Description:</strong></p>
    <p>{{ project.description || 'No description provided.' }}</p>

    <div>
      <h3>Participants</h3>
      <ul>
        <li v-for="p in project.totalUser" :key="p.id">
          ID: {{ p.id }}-{{ p.username }}
        </li>
        <li v-if="!project.participant || project.participant.length === 0">
          No participants.
        </li>
      </ul>
    </div>

    <div>
      <h3>Messages</h3>
      <ul>
        <li v-for="m in totalMessages" :key="m.id">
          <strong>ID:</strong> {{ m.id }} — {{ m.text }}
        </li>
        <li v-if="!totalMessages || totalMessages.length === 0">
          No messages.
        </li>
      </ul>
    </div>

    <router-link :to="`/projects/update/${project.id}`" class="edit-btn">
      ✏️ Edit Project
    </router-link>
  </section>

  <section v-else class="project-view">
    <p>Loading project...</p>
  </section>

  <Footer />
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'

const route = useRoute()
const projectLoaded = ref(false)
const project = reactive({
  id: null,
  name: '',
  description: '',
  participant: [],
  messages: [] // messages will include content after fetching
})

const authToken = "1757002588886:7:2.16.27.0.17.62"

// Fetch a single message by ID and update its content
async function fetchMessage(message) {
    var myHeaders = new Headers();
    myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62");

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    let mes= await fetch("http://localhost:8080/messages/"+message.id, requestOptions)
    return await mes.json()
}
const totalUser = ref([])
const totalMessages = ref([])
async function fetchUser(User){
    var myHeaders = new Headers();
    myHeaders.append("Authorization", "1757002588886:7:2.16.27.0.17.62");

    var requestOptions = {
      method: 'GET',
      headers: myHeaders,
      redirect: 'follow'
    };
    let Us=await fetch("http://localhost:8080/users/"+User.id, requestOptions)
    return await Us.json()
}
onMounted(async () => {
  const id = route.params.id
  try {
    const res = await fetch(`http://localhost:8080/projects/${id}`, {
      headers: { Authorization: authToken }
    })
    if (!res.ok) {
      const text = await res.text()
      throw new Error(`Server responded ${res.status}: ${text}`)
    }

    const data = await res.json()
    Object.assign(project, data)

    // Set project loaded immediately
    projectLoaded.value = true

    // Fetch full content for each message and store in totalMessages
    let messagesgotten = []
    if (project.messages && project.messages.length > 0) {
      console.log("Project messages fetching initiated.")
      for (const msg of project.messages) {
        messagesgotten=await fetchMessage(msg)
        console.log("Fetched message:", messagesgotten)
        totalMessages.value.push(messagesgotten)
      }
        console.log("All messages fetched:", totalMessages.value)
    }

    // Replace original messages array with the full data
    project.messages = totalMessages
    let Usergotten = []
    if (project.participant && project.participant.length > 0) {
      console.log("Project messages fetching initiated.")
      for (const User of project.participant) {
        Usergotten=await fetchUser(User)
        console.log("Fetched message:", Usergotten)
        totalUser.value.push(Usergotten)
      }
        console.log("All messages fetched:", totalUser.value)
    }
    project.totalUser = totalUser.value
  } catch (err) {
    console.error("Failed to load project:", err)
    projectLoaded.value = false
  }
})
</script>

<style scoped>
.project-view {
  max-width: 800px;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 16px;
  box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  font-family: 'Inter', system-ui, sans-serif;
}

.project-view h1 {
  color: #ca59c5;
  margin-bottom: 1rem;
}

.project-view h3 {
  color: #555;
  margin-top: 1.5rem;
  margin-bottom: 0.5rem;
}

.project-view ul {
  list-style: disc;
  padding-left: 1.5rem;
}

.edit-btn {
  display: inline-block;
  margin-top: 2rem;
  background-color: #ca59c5;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
}

.edit-btn:hover {
  background-color: #b44fb0;
}
</style>
