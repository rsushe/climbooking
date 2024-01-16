<!-- ClimberForm.vue -->
<template>
  <div class="climber-form-container">
    <h2>Create Climber</h2>
    <form @submit.prevent="createClimber">
      <label for="climberName">Name:</label>
      <input type="text" id="climberName" v-model="newClimber.name" required>

      <label for="climberBirthday">Birthday:</label>
      <input type="date" id="climberBirthday" v-model="newClimber.birthday" required>

      <label for="climberCategory">Category:</label>
      <select id="climberCategory" v-model="newClimber.category" required>
        <option v-for="category in categories" :key="category.id" :value="category.id">
          {{ category.name }}
        </option>
      </select>

      <button type="submit">Create Climber</button>
    </form>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      newClimber: {
        name: '',
        birthday: '',
        category: '', // Updated to store category ID
      },
      categories: [],
      errorMessage: null,
    };
  },
  created() {
    this.fetchCategories();
  },
  methods: {
    fetchCategories() {
      axios.get('http://localhost:8080/v1/categories')
          .then(response => {
            this.categories = response.data;
          })
          .catch(error => {
            console.error('Error fetching categories:', error);
          });
    },
    createClimber() {
      // Convert birthday to ISO format
      this.newClimber.birthday = new Date(this.newClimber.birthday).toISOString();

      axios.post('http://localhost:8080/v1/climbers', {
        name: this.newClimber.name,
        birthday: this.newClimber.birthday,
        category_id: this.newClimber.category,
      })
          .then(response => {
            // Reset the form after successful creation
            this.newClimber = {
              name: '',
              birthday: '',
              category: '',
            };
            console.log('Climber created successfully:', response.data);
            this.$emit('climber-created');
          })
          .catch(error => {
            if (error.response && error.response.status === 400) {
              // Display the error message from the response body
              this.errorMessage = error.response.data.message;
            } else {
              console.error('Error creating climber:', error);
            }
          });
    },
  },
};
</script>

<style scoped>
.climber-form-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 400px;
  margin: auto;
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 5px;
}

form {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;
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

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
