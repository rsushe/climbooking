<template>
  <header>
    <nav>
      <ul class="menu">
        <li><router-link to="/training-places">Места для тренировок</router-link></li>
        <li><router-link to="/tournaments">Турниры</router-link></li>
        <li><router-link to="/climbers">Скалолазы</router-link></li>
        <li><router-link to="/bookings">Букинги</router-link></li>
        <li><router-link to="/notifications">Нотификации</router-link></li>
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
