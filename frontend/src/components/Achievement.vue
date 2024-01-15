<template>
  <div class="container">
    <h2 style="text-align: center;">Achievement</h2>
    <table>
      <thead>
      <tr>
        <th>Title</th>
        <th>Description</th>
        <th>Owners names</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="achievement in achievements" :key="achievement.id">
        <td>{{ achievement.title }}</td>
        <td>{{ achievement.description }}</td>
        <td>
          <div>
            <ul>
              <li v-for="ownerName in achievement.ownersNames" :key="ownerName">{{ ownerName }}</li>
            </ul>
          </div>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      achievements: [],
      owners: [],
      selectedAchievementId: null
    };
  },
  created() {
    this.fetchAchievements(); // Получаем список ачивок при создании компонента
  },
  methods: {
    fetchAchievements() {
      axios.get('http://localhost:8080/v1/achievements')
          .then(response => {
            this.achievements = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the achievements: ', error);
          });
    }
  }
};
</script>

<style>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 1000px;
  margin: auto;
  padding: 20px;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

button {
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
  margin-right: 10px;
}

h2 {
  margin-bottom: 10px;
}
</style>
