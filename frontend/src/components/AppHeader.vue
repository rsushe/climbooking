<template>
  <header>
    <nav>
      <ul class="menu">
        <li><router-link to="/training-places">Training places</router-link></li>
        <li><router-link to="/tournaments">Tournaments</router-link></li>
        <li><router-link to="/climbers">Climbers</router-link></li>
        <li><router-link to="/bookings">Bookings</router-link></li>
        <li><router-link to="/routes">Routes</router-link></li>
      </ul>
    </nav>
  </header>
</template>

<script>
export default {
  name: 'AppHeader'
}
</script>

<style scoped>
header {
  background-color: #333;
  color: white;
  padding: 0.5em;
  text-align: center;
}

.menu {
  list-style-type: none;
  padding: 0;
  display: flex;
  justify-content: center;
}

.menu li {
  margin: 0 1em;
}

.menu li a {
  color: white;
  text-decoration: none;
  font-weight: bold;
}

.menu li a:hover {
  text-decoration: underline;
}
</style>
