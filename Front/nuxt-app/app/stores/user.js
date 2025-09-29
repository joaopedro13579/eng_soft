import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    userId: null,
  }),
  actions: {
    loadUserId() {
      const savedId = localStorage.getItem('userId')
      this.userId = savedId ? Number(savedId) : null
    },
    setUserId(id) {
      this.userId = id
      localStorage.setItem('userId', id)
    },
  },
})
