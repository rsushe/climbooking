<template>
  <div class="register-form">
    <h2>Register</h2>
    <form @submit.prevent="register">
      <input v-model="username" type="text" placeholder="username" required>
      <input v-model="password" type="password" placeholder="password" required>
      <select v-model="role">
        <option disabled value="">
          Please select one
        </option>
        <option>
          Climber
        </option>
        <option>
          Admin
        </option>
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
      registrationSuccess: false,
      errorMessage: '',
    };
  },
  methods: {
    async register() {
      try {
        const response = await this.$axios.post('/v1/register', {
          username: this.username,
          password: this.password,
          role: this.role,
        });
        console.log('Registration successful', response);
        this.registrationSuccess = true;
        this.errorMessage = '';
      } catch (error) {
        console.log('Registration failed', error);
        this.registrationSuccess = false;
        this.errorMessage = error.response.data.message || 'Registration failed';
      }
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