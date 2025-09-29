<template>
  <footer>
    <nav>
      <h1>My Website</h1>
    </nav>
    <p class="copyright">© 2025 My Website. All rights reserved.</p>
  </footer>
</template>

<style scoped>
footer {
  background: #ca59c5e7; /* same as header */
  color: white;
  padding: 1rem;
  margin-top: 2rem;
  text-align: center;
}

nav {
  display: flex;
  align-items: center;
  justify-content: center; /* center nav items horizontally */
  gap: 2rem; /* space between h1 and nav links */
  flex-wrap: wrap; /* wrap on smaller screens */
}

h1 {
  margin: 0;
  font-size: 1.25rem;
}

.nav-links {
  display: flex;
  gap: 2rem; /* space between links */
  flex-wrap: wrap;
}

.nav-links div a {
  color: white;
  text-decoration: none;
  font-weight: 500;
}

.nav-links div a:hover {
  text-decoration: underline;
}

footer p {
  margin-top: 1rem;
  font-size: 0.9rem;
  opacity: 0.8;
}
</style>
