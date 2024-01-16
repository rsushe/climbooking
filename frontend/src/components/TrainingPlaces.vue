<template>
  <div class="container">
    <h1 style="text-align: center;">Training places</h1>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Type</th>
        <th>Location</th>
<!--        <th>Routes</th>-->
      </tr>
      </thead>
      <tbody>
      <tr v-for="place in places" :key="place.id">
        <td>{{ place.id }}</td>
        <td>{{ place.name }}</td>
        <td>{{ place.type }}</td>
        <td>{{ 'x: ' + place.location.x + ', y: ' + place.location.y }}</td>
<!--        <td>-->
<!--          <div>-->
<!--            <ul>-->
<!--              <li v-for="routeName in place.routesNames" :key="routeName">{{ routeName }}</li>-->
<!--            </ul>-->
<!--          </div>-->
<!--        </td>-->
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>

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
      this.$axios.get('/v1/places')
          .then(response => {
            this.places = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the training places:', error);
          });
    }
  }
};
</script>

<style scoped>
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
</style>
