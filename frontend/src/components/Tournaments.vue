<template>
  <div class="tournaments-table-container">
    <h2>Tournaments</h2>
    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Start Date</th>
        <th>End Date</th>
        <th>Status</th>
        <th>Action</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="tournament in tournaments" :key="tournament.id">
        <td>{{ tournament.id }}</td>
        <td>{{ tournament.name }}</td>
        <td>{{ new Date(tournament.startDate).toLocaleDateString() }}</td>
        <td>{{ new Date(tournament.endDate).toLocaleDateString() }}</td>
        <td>{{ tournament.status }}</td>
        <td>
          <button @click="fetchTournamentDetails(tournament.id)">View Details</button>
        </td>
      </tr>
      </tbody>
    </table>

    <!-- Detailed view section -->
    <div v-if="selectedTournament" class="detailed-view">
      <div class="close-button-container">
        <button @click="closeDetailedView">&times; Close</button>
      </div>
      <h2>{{ selectedTournament.name }} Details</h2>
      <div class="organizers">
        <p>Organizers:</p>
        <ul>
          <li v-for="organizer in selectedTournament.organizers" :key="organizer.id">
            {{ organizer.name }}
          </li>
        </ul>
      </div>
      <div class="routes">
        <p>Routes:</p>
        <ul>
          <li v-for="route in selectedTournament.routes" :key="route.id">
            {{ route.name }} - {{ route.difficulty }}
          </li>
        </ul>
      </div>
    </div>

    <div class="create-tournament-form">
      <h2 style="text-align: center;">Create Tournament</h2>
      <form @submit.prevent="createTournament">
        <label for="tournamentName">Name:</label>
        <input type="text" id="tournamentName" v-model="newTournament.name" required>

        <label for="startDate">Start Date:</label>
        <input type="date" id="startDate" v-model="newTournament.startDate" required>

        <label for="endDate">End Date:</label>
        <input type="date" id="endDate" v-model="newTournament.endDate" required>

        <label for="organizers">Organizers:</label>
        <select id="organizers" v-model="newTournament.organizerIds" multiple>
          <option v-for="organizer in allOrganizers" :key="organizer.id" :value="organizer.id">
            {{ organizer.name }}
          </option>
        </select>

        <label for="routes">Routes:</label>
        <select id="routes" v-model="newTournament.routeIds" multiple>
          <option v-for="route in allRoutes" :key="route.id" :value="route.id">
            {{ route.name }}
          </option>
        </select>

        <button type="submit">Create Tournament</button>
      </form>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      tournaments: [],
      selectedTournament: null,
      newTournament: {
        name: '',
        startDate: '',
        endDate: '',
        organizerIds: [],
        routeIds: [],
      },
      allOrganizers: [],
      allRoutes: [],
    };
  },
  created() {
    this.fetchTournaments();
    this.fetchOrganizers();
    this.fetchRoutes();
  },
  methods: {
    fetchTournaments() {
      axios.get('http://localhost:8080/v1/tournaments')
          .then(response => {
            this.tournaments = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching tournaments:', error);
          });
    },
    fetchTournamentDetails(tournamentId) {
      axios.get(`http://localhost:8080/v1/tournaments/${tournamentId}`)
          .then(response => {
            this.selectedTournament = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching tournament details:', error);
          });
    },
    closeDetailedView() {
      this.selectedTournament = null;
    },
    fetchOrganizers() {
      axios.get('http://localhost:8080/v1/climbers')
          .then(response => {
            this.allOrganizers = response.data;
          })
          .catch(error => {
            console.error('Error fetching organizers:', error);
          });
    },

    fetchRoutes() {
      axios.get('http://localhost:8080/v1/routes')
          .then(response => {
            this.allRoutes = response.data;
          })
          .catch(error => {
            console.error('Error fetching routes:', error);
          });
    },

    createTournament() {
      this.newTournament.startDate = new Date(this.newTournament.startDate).toISOString();
      this.newTournament.endDate = new Date(this.newTournament.endDate).toISOString();

      axios.post('http://localhost:8080/v1/tournaments', this.newTournament)
          .then(response => {
            this.newTournament = {
              name: '',
              startDate: '',
              endDate: '',
              organizerIds: [],
              routeIds: [],
            };
            console.log('Tournament created successfully:', response.data);
            this.fetchTournaments();
          })
          .catch(error => {
            console.error('Error creating tournament:', error);
          });
    },
  },
};
</script>

<style scoped>
.tournaments-table-container {
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

button:hover {
  background-color: #45a049;
}

.detailed-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
}

.organizers, .routes {
  margin-top: 10px;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin-bottom: 5px;
}

.detailed-view {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.close-button-container {
  align-self: flex-end;
}

.close-button-container button {
  background-color: #f44336;
  color: white;
  border: none;
  padding: 8px 12px;
  cursor: pointer;
}

.close-button-container button:hover {
  background-color: #d32f2f;
}

.organizers, .routes {
  width: 100%;
}

.create-tournament-form {
  width: 400px;
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
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
  width: 100%;
  padding: 8px;
  box-sizing: border-box;
}
</style>
