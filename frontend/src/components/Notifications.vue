<template>
  <div class="notifications-table-container">
    <h2>Notifications</h2>
    <div class="climber-id-input">
      <label for="climberIdInput">Enter Climber ID:</label>
      <input type="number" id="climberIdInput" v-model="inputClimberId">
      <button @click="fetchNotificationsForClimber">Get Notifications</button>
    </div>
    <div class="notifications-table">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Climber ID</th>
          <th>Status</th>
          <th>RouteId</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="notification in notifications" :key="notification.id">
          <td>{{ notification.id }}</td>
          <td>{{ notification.climberId }}</td>
          <td>{{ notification.status }}</td>
          <td>{{ notification.routeId }}</td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>

export default {
  data() {
    return {
      notifications: [],
      inputClimberId: null,
    };
  },
  methods: {
    fetchNotificationsForClimber() {
      if (this.inputClimberId) {
        this.$axios.get(`/v1/notifications?climberId=${this.inputClimberId}`)
            .then(response => {
              this.notifications = response.data.notifications;
            })
            .catch(error => {
              console.error('There was an error fetching notifications for climber:', error);
            });
      } else {
        console.error('Please enter a valid Climber ID');
        this.notifications = [];
      }
    },
  },
};
</script>

<style scoped>
.notifications-table-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 800px;
  margin: auto;
  padding: 20px;
}

.climber-id-input {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
}

.climber-id-input label {
  margin-right: 10px;
}

.notifications-table {
  width: 100%;
  overflow-x: auto;
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
</style>
