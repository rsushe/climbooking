<template>
  <div>
    <h1>Места для тренировок</h1>
    <table>
      <thead>
      <tr>
        <th>Название</th>
        <th>Тип</th>
        <th>Местоположение</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="place in places" :key="place.id">
        <td>{{ place.name }}</td>
        <td>{{ place.type }}</td>
        <td>{{ "x: " + place.location.x + ", y: " + place.location.y }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      places: []
    };
  },
  created() {
    this.fetchPlaces();
  },
  methods: {
    async fetchPlaces() {
      try {
        const response = await axios.get('http://localhost:8080/v1/places');
        this.places = response.data;
      } catch (error) {
        console.log(error);
        // Обработка ошибок или отображение сообщения об ошибке
        // Например: this.error = 'Ошибка загрузки данных.'
      }
    }
  }
}
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 8px;
  text-align: left;
  border: 1px solid #ddd;
}

th {
  background-color: #f2f2f2;
}
</style>
