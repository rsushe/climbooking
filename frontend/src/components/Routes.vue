<template>
  <div class="container">
    <h2 style="text-align: center;">All routes</h2>

    <div v-if="showAddRouteModal" class="modal">
      <div class="modal-content">
        <span class="close" @click="showAddRouteModal = false">&times;</span>
        <h2>Add new route</h2>
        <form @submit.prevent="addRoute">
          <div>
            <label for="placeId">Place:</label>
            <select id="placeId" v-model="newRoute.placeId" required>
              <option disabled value="">Select place</option>
              <option v-for="place in places" :key="place.id" :value="place.id">{{ place.name }}</option>
            </select>
          </div>
          <div>
            <label for="name">Name:</label>
            <input type="text" id="name" v-model="newRoute.name">
          </div>
          <div>
            <label for="difficulty">Difficulty:</label>
            <select id="difficulty" v-model="newRoute.difficulty">
              <option value="6a">6a</option>
              <option value="6a+">6a+</option>
              <option value="6b">6b</option>
              <option value="6b+">6b+</option>
              <option value="6c">6c</option>
              <option value="6c">6c</option>
              <option value="6c+">6c+</option>
              <option value="7a">7a</option>
            </select>
          </div>
          <div>
            <label for="type">Type:</label>
            <select id="type" v-model="newRoute.type">
              <option value="bouldering">bouldering</option>
              <option value="lead">lead</option>
            </select>
          </div>
          <div>
            <label for="creationDate">Creation date:</label>
            <input type="date" id="creationDate" v-model="newRoute.creationDate">
          </div>
          <div v-for="(author, index) in newRoute.authors" :key="index" class="author-field">
            <label>Route author:</label>
            <select v-model="author.id">
              <option disabled value="">Select author</option>
              <option v-for="climber in climbers" :value="climber.id" :key="climber.id">
                {{ climber.name }}
              </option>
            </select>
            <button type="button" @click="removeAuthor(index)">Remove author</button>
          </div>
          <button type="button" @click="addAuthor">Add author</button>

          <button type="submit">Add route</button>
        </form>
      </div>
    </div>

    <button v-if="!showAddRouteModal" @click="showAddRouteModal=true">Add route</button>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Place ID</th>
        <th>Name</th>
        <th>Difficulty</th>
        <th>Type</th>
        <th>Creation Date</th>
        <th>Is rolled</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="route in routes" :key="route.id">
        <td>{{ route.id }}</td>
        <td>{{ route.placeId }}</td>
        <td>{{ route.name }}</td>
        <td>{{ route.difficulty }}</td>
        <td>{{ route.type }}</td>
        <td>{{ route.creationDate }}</td>
        <td>{{ route.isRolled ? 'Да' : 'Нет' }}</td>
        <td>
          <button @click="rollRoute(route.id)">Roll route</button>
          <button @click="bookRoute(route.id)">Book route</button>
        </td>
      </tr>
      </tbody>
    </table>

    <pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        :nextPage="nextPage"
        :prevPage="prevPage"
    ></pagination>
  </div>
</template>

<script>
import axios from 'axios';
import Pagination from '@/components/UI/Pagination.vue';

export default {
  components: {
    Pagination,
  },
  data() {
    return {
      routes: [],
      places: [],
      climbers: [],
      currentPage: 1,
      itemsPerPage: 5,
      showAddRouteModal: false,
      newRoute: {
        placeId: '',
        name: '',
        difficulty: 'easy', // Значение по умолчанию
        type: 'boulder', // Значение по умолчанию
        creationDate: '',
        authors: [{id: ''}]
      },
    };
  },
  computed: {
    totalRoutes() {
      return this.routes.length;
    },
    totalPages() {
      return Math.ceil(this.totalRoutes / this.itemsPerPage);
    },
    paginatedRoutes() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.routes.slice(startIndex, endIndex);
    },
  },
  created() {
    this.fetchRoutes();
    this.fetchPlaces();
    this.fetchClimbers();
  },
  methods: {
    async fetchRoutes() {
      axios.get('http://localhost:8080/v1/routes')
          .then(response => {
            this.routes = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the routes:', error);
          });
    },
    async fetchPlaces() {
      axios.get('http://localhost:8080/v1/places')
          .then(response => {
            this.places = response.data;
          })
          .catch(error => {
            console.error('Ошибка при загрузке мест:', error);
          });
    },
    async fetchClimbers() {
      axios.get('http://localhost:8080/v1/climbers')
          .then(response => {
            this.climbers = response.data;
          })
          .catch(error => {
            console.error('Ошибка при загрузке скалолазов:', error);
          });
    },
    addAuthor() {
      this.newRoute.authors.push({id: ''}); // Добавляем новый ID автора со значением по умолчанию
      console.log(this.newRoute.authors);
    },
    removeAuthor(index) {
      this.newRoute.authors.splice(index, 1);
      console.log(this.newRoute.authors);
    },
    addRoute() {
      console.log('Добавление маршрута:', this.newRoute);

      axios.post('http://localhost:8080/v1/routes', {
        'placeId': this.newRoute.placeId,
        'name': this.newRoute.name,
        'difficulty': this.newRoute.difficulty,
        'type': this.newRoute.type,
        'creationDate': this.newRoute.creationDate,
        'authors': this.newRoute.authors.map(it => it.id)
      }).then(_ => {
        this.fetchRoutes();
      });

      this.showAddRouteModal = false;
    },
    rollRoute(routeId) {
      axios.patch('http://localhost:8080/v1/routes/' + routeId + '/roll')
          .then(_ => {
            axios.get('http://localhost:8080/v1/routes/' + routeId).then(response => {
              let serverRoute = response.data;
              let route = this.routes.find(r => r.id === routeId);
              route.isRolled = serverRoute.isRolled;
            });
          })
          .catch(error => {
            console.error('There was an error fetching the routes:', error);
          });

    },
    bookRoute(id) {
      // Логика бронирования трассы
    },
    nextPage() {
      if (this.currentPage < this.totalPages) {
        this.currentPage++;
      }
    },
    prevPage() {
      if (this.currentPage > 1) {
        this.currentPage--;
      }
    },
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

input {
  padding: 8px;
  margin-right: 10px;
}

h2 {
  margin-bottom: 10px;
}

.modal {
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fefefe;
  margin: 10% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
  max-width: 500px;
}

.modal-content div {
  margin-bottom: 10px;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>
