<template>
  <header>
    <nav>
      <h1>User Story Hub</h1>
      <div class="nav-links">
        <div class="home-link"><a href="/">Home</a></div>
        <div class="enter-link"><a href="/user/signin">Enter</a></div>
        <div class="proj-link"><a href="/projects/read">My Projects</a></div>
        <div class="tasks-link"><a href="/messages/read">My Tasks</a></div>
        <div ><a href="/user/read"><img class="img" src="/src/user.jpeg" ></img></a></div>
      </div>
    </nav>
  </header>
</template>

<style scoped>
header {
  background: #ca59c5e7;
  color: white;
  padding: 1rem;
}
.img{
  width: 4rem;
  height: auto;
  position: absolute;
  top: 1rem;
  right: 1rem;
  border-radius: 50%;
}

nav {
  display: flex;
  align-items: center;
  gap: 2rem; /* space between the h1 and the nav links */
}

h1 {
  margin: 0;
}

.nav-links {
  display: flex;
  gap: 2rem; /* space between links */
}

.nav-links div a {
  color: white;
  text-decoration: none;
  font-weight: 500;
}

.nav-links div a:hover {
  text-decoration: underline;
}
</style>
