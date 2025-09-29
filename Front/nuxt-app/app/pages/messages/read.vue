<template>
  <Header />
  <section v-if="logged" class="messages">
    <div class="messages-header">
      <h1>All Messages</h1>

      <select v-model="selectedProject" class="project-filter">
        <option value="">All Projects</option>
        <option v-for="project in projects" :key="project.id" :value="project.name">
          {{ project.name }}
        </option>
      </select>
    </div>

    <div v-if="filteredMessages.length" class="message-list">
      <div v-for="(msg, index) in filteredMessages" :key="index" class="message-item">
        <h2>{{ msg.title }}</h2>
        <p>{{ msg.body }}</p>
        <div class="meta">
          <span>📅 {{ msg.date }}</span>
          <span>📁 {{ msg.project }}</span>
        </div>
      </div>
    </div>

    <p v-else class="no-messages">No messages found for this project.</p>
  </section>

  <section v-else class="messages">
    <div class="messages-header">
      <h1>Please log in to view your messages.</h1>
    </div>
  </section>

  <Footer />
</template>

<script setup>
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { ref, computed } from 'vue'
import { useUserStore } from '~/stores/user'

const userStore = useUserStore()
const logged=ref(false);
onMounted(() => {
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
})
// Dummy projects
const projects = [
  { id: 1, name: 'Alpha Project' },
  { id: 2, name: 'Beta Project' },
  { id: 3, name: 'Gamma Project' }
]

// Dummy messages with project names
const messages = ref([
  {
    title: 'Kickoff Meeting',
    body: 'Initial meeting for Alpha Project scheduled on Monday.',
    date: '2025-09-22',
    project: 'Alpha Project'
  },
  {
    title: 'Sprint Review',
    body: 'Reviewing progress of Beta Project sprint.',
    date: '2025-09-20',
    project: 'Beta Project'
  },
  {
    title: 'Design Updates',
    body: 'New UI designs for Gamma Project are ready.',
    date: '2025-09-19',
    project: 'Gamma Project'
  },
  {
    title: 'Reminder',
    body: 'Don’t forget to push updates for Alpha Project.',
    date: '2025-09-18',
    project: 'Alpha Project'
  }
])

const selectedProject = ref('')

// Filter messages based on selected project
const filteredMessages = computed(() => {
  if (!selectedProject.value) return messages.value
  return messages.value.filter(msg => msg.project === selectedProject.value)
})
</script>

<style scoped>
.messages {
  max-width: 900px;
  margin: auto;
  padding: 4rem 1rem;
}

.messages-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.messages-header h1 {
  color: #ca59c5;
  font-size: 2rem;
}

.project-filter {
  padding: 0.5rem 1rem;
  font-size: 1rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  margin-top: 0.5rem;
}

.message-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.message-item {
  background: #f9f9f9;
  padding: 1.5rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.message-item h2 {
  margin: 0 0 0.5rem;
  color: #ca59c5;
}

.message-item p {
  margin: 0 0 0.75rem;
  color: #444;
}

.meta {
  font-size: 0.9rem;
  color: #777;
  display: flex;
  gap: 1.5rem;
}

.no-messages {
  text-align: center;
  font-style: italic;
  color: #999;
  margin-top: 2rem;
}
</style>
