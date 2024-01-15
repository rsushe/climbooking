<template>
  <div class="container">
    <h2 style="text-align: center;">All routes</h2>

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
      <tr v-for="route in paginatedRoutes" :key="route.id">
        <td>{{ route.id }}</td>
        <td>{{ route.placeId }}</td>
        <td>{{ route.name }}</td>
        <td>{{ route.difficulty }}</td>
        <td>{{ route.type }}</td>
        <td>{{ route.creationDate }}</td>
        <td>{{ route.isRolled ? 'Yes' : 'No' }}</td>
        <td>
          <button v-if="!route.isRolled" @click="rollRoute(route.id)">Roll route</button>
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

    <div class="forms-container">
      <div class="form">
        <h2 style="text-align: center;">Add new route</h2>
        <form @submit.prevent="addRoute">
          <label for="placeId">Place:</label>
          <select id="placeId" v-model="newRoute.placeId" required>
            <option disabled value="">Select place</option>
            <option v-for="place in places" :key="place.id" :value="place.id">{{ place.name }}</option>
          </select>

          <label for="name">Name:</label>
          <input type="text" id="name" v-model="newRoute.name">

          <label for="difficulty">Difficulty:</label>
          <select id="difficulty" v-model="newRoute.difficulty">
            <option disabled value="">Select difficulty</option>
            <option v-for="difficulty in difficulties" :key="difficulty" :value="difficulty">{{ difficulty }}</option>
          </select>

          <label for="type">Type:</label>
          <select id="type" v-model="newRoute.type">
            <option disabled value="">Select type</option>
            <option value="bouldering">bouldering</option>
            <option value="lead">lead</option>
          </select>

          <label for="creationDate">Creation date:</label>
          <input type="date" id="creationDate" v-model="newRoute.creationDate">

          <label for="organizers">Route authors:</label>
          <select id="organizers" v-model="newRoute.authors" multiple>
            <option v-for="climber in climbers" :value="climber.id" :key="climber.id">
              {{ climber.name }}
            </option>
          </select>

          <button type="submit">Add route</button>
        </form>

        <div v-if="showSuccessAddMessage" class="message success">
          Success!
        </div>

        <div v-if="showErrorAddMessage" class="message error">
          Error :(
        </div>

      </div>

      <div class="form">
        <div>
          <h2 style="text-align: center;">Book route</h2>
          <form @submit.prevent="bookRoute">
            <label for="climberId">Climber:</label>
            <select id="climberId" v-model="newBook.climberId" required>
              <option disabled value="">Select climber</option>
              <option v-for="climber in climbers" :value="climber.id" :key="climber.id">
                {{ climber.name }}
              </option>
            </select>

            <label for="routeId">Route:</label>
            <select id="routeId" v-model="newBook.routeId" required>
              <option disabled value="">Select route</option>
              <option v-for="route in routes" :key="route.id" :value="route.id">{{ route.name }}</option>
            </select>

            <label for="bookingTimeStart">Booking time start:</label>
            <input type="datetime-local" id="bookingTimeStart" v-model="newBook.startTime">

            <label for="duration">Duration (in minutes)</label>
            <input type="number" id="duration" v-model="newBook.duration">

            <button type="submit">Book route</button>
          </form>

          <div v-if="showSuccessBookMessage" class="message success">
            Success!
          </div>

          <div v-if="showErrorBookMessage" class="message error">
            Error :(
          </div>
        </div>
        <div v-if="showOverlayingBookings">
          <h2 style="text-align: center;">Overlaying Bookings</h2>
          <table>
            <thead>
            <tr>
              <th>Booking ID</th>
              <th>Climber ID</th>
              <th>Route ID</th>
              <th>Status</th>
              <th>Start Time</th>
              <th>End Time</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="booking in overlayingBookings" :key="booking.id">
              <td>{{ booking.id }}</td>
              <td>{{ booking.climberId }}</td>
              <td>{{ booking.routeId }}</td>
              <td>{{ booking.status }}</td>
              <td>{{ new Date(booking.startTime).toLocaleString() }}</td>
              <td>{{ new Date(booking.endTime).toLocaleString() }}</td>
            </tr>
            </tbody>
          </table>
          <button @click="cancel()">Cancel</button>
          <button @click="subscribeClimberToOverlayingBookings()">Subscribe climber to all bookings</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios, {HttpStatusCode} from 'axios';
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
      difficulties: [],
      overlayingBookings: [],
      currentPage: 1,
      itemsPerPage: 5,
      newRoute: {
        placeId: '',
        name: '',
        difficulty: '',
        type: '',
        creationDate: '',
        authors: [],
      },
      newBook: {
        climberId: '',
        routeId: '',
        startTime: '',
        duration: '',
      },
      showSuccessAddMessage: false,
      showSuccessBookMessage: false,
      showErrorAddMessage: false,
      showErrorBookMessage: false,
      showOverlayingBookings: false
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
    this.fetchDifficulties();
  },
  methods: {
    async fetchRoutes() {
      axios.get('http://localhost:8080/v1/routes')
          .then(response => {
            this.routes = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the routes: ', error);
          });
    },
    async fetchPlaces() {
      axios.get('http://localhost:8080/v1/places')
          .then(response => {
            this.places = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the places: ', error);
          });
    },
    async fetchClimbers() {
      axios.get('http://localhost:8080/v1/climbers')
          .then(response => {
            this.climbers = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the climbers: ', error);
          });
    },
    async fetchDifficulties() {
      axios.get('http://localhost:8080/v1/routes/difficulties')
          .then(response => {
            this.difficulties = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the difficulties: ', error);
          });
    },
    addRoute() {
      console.log('Добавление маршрута:', this.newRoute);

      axios.post('http://localhost:8080/v1/routes', this.newRoute)
          .then(_ => {

            this.newRoute = {
              placeId: '',
              name: '',
              difficulty: '',
              type: '',
              creationDate: '',
              authors: []
            };

            this.showSuccessAddMessage = true;
            this.hideMessageAfterDelay('showSuccessAddMessage');

            this.fetchRoutes();
          })
          .catch(error => {
            console.error('There was an error creating the routes: ', error);

            this.showErrorAddMessage = true;
            this.hideMessageAfterDelay('showErrorAddMessage');
          });
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
            console.error('There was an error fetching the routes: ', error);
          });

    },
    bookRoute() {
      axios.post('http://localhost:8080/v1/bookings', {
        climberId: this.newBook.climberId,
        routeId: this.newBook.routeId,
        startTime: new Date(this.newBook.startTime).toISOString(),
        endTime: this.addMinutes(new Date(this.newBook.startTime), this.newBook.duration).toISOString()
      })
          .then(_ => {

            this.newBook = {
              climberId: '',
              routeId: '',
              startTime: '',
              duration: '',
            };

            this.showSuccessBookMessage = true;
            this.hideMessageAfterDelay('showSuccessBookMessage');
          })
          .catch(error => {
            console.error('There was an error while booking route: ', error);

            this.showErrorBookMessage = true;
            this.hideMessageAfterDelay('showErrorBookMessage');

            if (error.response.status === HttpStatusCode.Conflict) {
              this.overlayingBookings = error.response.data.bookings;
              this.showOverlayingBookings = true;
            }
          });
    },
    addMinutes(date, minutes) {
      return new Date(date.getTime() + minutes * 60000);
    },
    cancel() {
      this.newBook = {
        climberId: '',
        routeId: '',
        startTime: '',
        duration: '',
      };

      this.showOverlayingBookings = false;
    },
    subscribeClimberToOverlayingBookings() {
      let bookingIds = this.overlayingBookings.map(it => it.id);
      axios.post(`http://localhost:8080/v1/notifications?climberId=${this.newBook.climberId}&bookingIds=${bookingIds}`)
          .then(_ => {
            this.cancel();
          })
          .catch(error => {
            console.error('There was an error subscribing to bookings: ', error);
          });
    },
    hideMessageAfterDelay(messageType) {
      setTimeout(() => {
        this[messageType] = false;
      }, 3000);
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

h2 {
  margin-bottom: 10px;
}

.forms-container {
  display: flex; /* Использование Flexbox для горизонтального выравнивания */
  justify-content: flex-start; /* Выравнивание в начале контейнера */
  align-items: flex-start; /* Выравнивание по верхнему краю элементов */
}

.form {
  flex: 1;
  width: 350px;
  margin-top: 30px;
  margin-right: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
  max-width: 1000px;
}

.form:last-child {
  width: 600px;
  margin-right: 0; /* Удаление отступа справа для последней формы */
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
}

label {
  margin-top: 10px;
}

input, select {
  margin-top: 5px;
  width: 300px;
  padding: 8px;
  box-sizing: border-box;
}

button {
  margin-top: 15px;
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.message {
  padding: 10px;
  margin-top: 10px;
  color: #fff;
  border-radius: 5px;
  text-align: center;
}

.success {
  background-color: #4CAF50; /* Зеленый цвет для успеха */
}

.error {
  background-color: #F44336; /* Красный цвет для ошибки */
}
</style>
