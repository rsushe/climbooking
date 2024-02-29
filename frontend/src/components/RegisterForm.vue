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
      } catch (error) {
        console.log('Registration failed', error);
      }
    }
  }
};
</script>

<style scoped>

</style>