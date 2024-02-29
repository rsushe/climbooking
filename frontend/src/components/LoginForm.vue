<template>
  <div class="login-form">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <input v-model="username" type="text" placeholder="username" required>
      <input v-model="password" type="password" placeholder="password" required>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script>
export default {
  data() {
    return {
      username: '',
      password: '',
    };
  },
  methods: {
    async login() {
      try {
        const response = await this.$axios.post('/v1/login', {
          username: this.username,
          password: this.password,
        });
        console.log('Login successful', response);
        this.$store.dispatch('authenticateUser', response.data);
      } catch (error) {
        console.log('Login failed', error);
      }
    }
  }
};
</script>

<style scoped>

</style>