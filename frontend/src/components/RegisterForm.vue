<template>
  <div class="register-form">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="username" type="text" placeholder="username" required>
      <input v-model="password" type="password" placeholder="password" required>

      <select v-model="role">
        <option disabled value="">Select role</option>
        <option v-for="role in roles" :key="role" :value="role">{{ role }}</option>
      </select>
      <button type="submit">Register</button>
      <div v-if="registrationSuccess" class="message success">Registration Successful!</div>
      <div v-if="errorMessage" class="message error">{{ errorMessage }}</div>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
      role: '',
      roles: [],
      registrationSuccess: false,
      errorMessage: '',
    };
  },
  created() {
    this.fetchRoles();
  },
  methods: {
    async register() {
      this.$axios.post('/v1/authentication/register', {
        username: this.username,
        password: this.password,
        role: this.role,
      })
          .then(response => {
            console.log('Registration successful', response);
            this.registrationSuccess = true;
            this.errorMessage = '';
          })
          .catch(error => {
            console.log('Registration failed', error);
            this.registrationSuccess = false;
            this.errorMessage = error.response.data.message || 'Registration failed';
          });
    },
    async fetchRoles() {
      this.$axios.get('/v1/authentication/roles')
          .then(response => {
            this.roles = response.data;
          })
          .catch(error => {
            console.error('There was an error fetching the roles: ', error);
          });
    }
  }
};
</script>

<style scoped>
.register-form h2 {
  text-align: center;
  padding: 10px;
  border: 2px solid #4CAF50;
  width: fit-content;
  margin: 0 auto;
}

.register-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}


.register-form button {
  cursor: pointer;
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px;
  margin-top: 10px;
}

.register-form input,
.register-form select {
  margin: 10px 0;
  padding: 8px;
  width: 90%;
  box-sizing: border-box;
}

.message.success {
  color: green;
}

</style>