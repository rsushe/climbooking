vue
<template>
  <div class="container">
    <h2 style="text-align: center;">All Bookings</h2>
    <table>
      <thead>
      <tr>
        <th>Booking ID</th>
        <th>Climber ID</th>
        <th>Route ID</th>
        <th>Status</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="booking in paginatedBookings" :key="booking.id">
        <td>{{ booking.id }}</td>
        <td>{{ booking.climberId }}</td>
        <td>{{ booking.routeId }}</td>
        <td>{{ booking.status }}</td>
        <td>{{ new Date(booking.startTime).toLocaleString() }}</td>
        <td>{{ new Date(booking.endTime).toLocaleString() }}</td>
        <td>
          <button v-if="booking.status === 'ACTIVE'" @click="cancelBooking(booking.id)">Cancel</button>
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
    <h2>Enter Climber ID to view active bookings:</h2>
    <div class="climberBookings">
      <input type="number" v-model="inputClimberId">
      <button @click="fetchActiveBookingsForClimber">Get Active Bookings</button>
    </div>
    <h2>Active Bookings for Climber {{ climberId }}</h2>


    <table>
      <thead>
      <tr>
        <th>Booking ID</th>
        <th>Climber ID</th>
        <th>Route ID</th>
        <th>Status</th>
        <th>Start Time</th>
        <th>End Time</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="booking in activeBookingsForClimber" :key="booking.id">
        <td>{{ booking.id }}</td>
        <td>{{ booking.climberId }}</td>
        <td>{{ booking.routeId }}</td>
        <td>{{ booking.status }}</td>
        <td>{{ new Date(booking.startTime).toLocaleString() }}</td>
        <td>{{ new Date(booking.endTime).toLocaleString() }}</td>
        <td>
          <button v-if="booking.status === 'ACTIVE'" @click="cancelBooking(booking.id)">Cancel</button>
        </td>
      </tr>
      </tbody>
    </table>

  </div>
</template>

<script>
import axios from 'axios';
import Pagination from './UI/Pagination.vue';

export default {
  components: {
    Pagination,
  },
  data() {
    return {
      bookings: [],
      climberId: null, // Assuming you know the current climber ID
      inputClimberId: null,
      activeBookingsForClimber: [],
      currentPage: 1,
      itemsPerPage: 5,
    };
  },
  computed: {
    totalBookings() {
      return this.bookings.length;
    },
    totalPages() {
      return Math.ceil(this.totalBookings / this.itemsPerPage);
    },
    paginatedBookings() {
      const startIndex = (this.currentPage - 1) * this.itemsPerPage;
      const endIndex = startIndex + this.itemsPerPage;
      return this.bookings.slice(startIndex, endIndex);
    },
  },
  created() {
    this.fetchBookings();
  },
  methods: {
    fetchBookings() {
      axios.get('http://localhost:8080/v1/bookings')
          .then(response => {
            this.bookings = response.data.bookings;
          })
          .catch(error => {
            console.error('There was an error fetching the bookings:', error);
          });
    },
    cancelBooking(bookingId) {
      axios.post(`http://localhost:8080/v1/bookings/${bookingId}/cancellation`)
          .then(() => {
            this.updateBookingStatus(bookingId, 'CANCELLED');
          })
          .catch(error => {
            console.error('There was an error cancelling the booking:', error);
          });
    },
    updateBookingStatus(bookingId, status) {
      let index = this.bookings.findIndex(booking => booking.id === bookingId);
      if (index !== -1) {
        this.bookings[index].status = status;
        this.bookings = [...this.bookings];
      }

      this.activeBookingsForClimber = this.activeBookingsForClimber.filter(booking => booking.id !== bookingId);
    },
    fetchActiveBookingsForClimber() {
      if (this.inputClimberId) {
        this.climberId = parseInt(this.inputClimberId, 10);
        axios.get(`http://localhost:8080/v1/climbers/${this.climberId}/bookings/active`)
            .then(response => {
              this.activeBookingsForClimber = response.data;
            })
            .catch(error => {
              console.error('There was an error fetching active bookings for climber:', error);
              this.activeBookingsForClimber = [];
            });
      } else {
        console.error('Please enter a valid Climber ID');
        this.activeBookingsForClimber = [];
      }
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
  },
};
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 800px;
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
}

input {
  padding: 8px;
  margin-right: 10px;
}

h2 {
  margin-bottom: 10px;
}
</style>
