<template>
  <Header/>
  <section class="user-update">
    <h1>Your Profile</h1>

    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="name">Name</label>
        <input
          id="name"
          :readonly="!isEditing"
          :class="{ editable: isEditing }"
          v-model="user.name"
          @focus="enableEdit"
          type="text"
          placeholder="Enter your name"
          required
        />
      </div>

      <div class="form-group">
        <label for="password">Password</label>
        <input
          id="password"
          :readonly="!isEditing"
          :class="{ editable: isEditing }"
          v-model="user.password"
          @focus="enableEdit"
          type="password"
          placeholder="Enter your password"
          required
        />
      </div>

      <div class="actions">
        <button type="submit" class="btn-submit">Update Profile</button>
        <button type="button" class="btn-cancel" @click="cancelEdit">Cancel</button>
      </div>
      <button type="button" class="btn-submit" @click="checkout">checkout</button>
    </form>

    <p v-if="message" class="message">{{ message }}</p>

  </section>
  <Footer/>
</template>

<script setup lang="ts">
import Header from '~/components/Header.vue'
import Footer from '~/components/Footer.vue'
import { reactive, ref, onMounted } from 'vue';
import { useUserStore } from '~/stores/user'
import { useRouter } from 'vue-router'

const Router = useRouter()
const userStore = useUserStore()

const checkout = () => {
  localStorage.setItem('userId', "0")
  userStore.setUserId(0);
  window.location.reload();
};

interface User {
  name: string;
  password: string;
}

const user = reactive<User>({
  name: '',
  password: '',
});

const originalUser = reactive<User>({
  name: '',
  password: '',
});

const isEditing = ref<boolean>(false);
const message = ref<string>('');

// Simulate fetching logged-in user data
onMounted(() => {
  // Simulate API call here
  const fetchedUser: User = {
    name: 'Jane Doe',
    password: '123456', // For demo only — do not store plaintext passwords in real apps!
  };

  Object.assign(user, fetchedUser);
  Object.assign(originalUser, fetchedUser);
});

function enableEdit(): void {
  if (!isEditing.value) {
    isEditing.value = true;
    message.value = '';
  }
}

function cancelEdit(): void {
  Object.assign(user, originalUser);
  isEditing.value = false;
  message.value = '';
}

function handleSubmit(): void {
  // Simulate API call to update user data here
  Object.assign(originalUser, user);

  message.value = 'Profile updated successfully!';
  isEditing.value = false;
}
</script>

<style scoped>
.user-update {
  max-width: 600px;
  margin: 2rem auto;
  padding: 1.5rem;
  background: #f9f9f9;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

h1 {
  color: #ca59c5;
  text-align: center;
  margin-bottom: 2rem;
}

.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}

label {
  margin-bottom: 0.5rem;
  color: #555;
  font-weight: 600;
}

input {
  padding: 0.75rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  font-size: 1rem;
  background-color: #eee;
  cursor: default;
}

input.editable {
  background-color: white;
  border-color: #ca59c5;
  cursor: text;
}

input.editable:focus {
  outline: none;
  box-shadow: 0 0 5px #ca59c5;
}

input[readonly] {
  user-select: text;
}

.actions {
  display: flex;
  gap: 1rem;
  justify-content: center;
  margin-top: 1.5rem;
}

.btn-submit {
  background-color: #ca59c5;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s;
}

.btn-submit:hover {
  background-color: #a8459d;
}

.btn-cancel {
  background-color: #ccc;
  color: #333;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  cursor: pointer;
  font-weight: bold;
  transition: background-color 0.3s;
}

.btn-cancel:hover {
  background-color: #aaa;
}

.message {
  margin-top: 1rem;
  text-align: center;
  color: green;
  font-weight: 600;
}
</style>
