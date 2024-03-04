<template>
  <div class="login-form">
    <h2>Login</h2>
    <form @submit.prevent="login">
      <input v-model="username" type="text" placeholder="username" required>
      <input v-model="password" type="password" placeholder="password" required>
      <button type="submit">Login</button>
      <p v-if="showAuthRequiredMessage" class="auth-message">
        Please, login or register
      </p>
      <div v-if="loginSuccess" class="message success">Login Successful!</div>
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
      loginSuccess: false,
      errorMessage: '',
    };
  },
  computed: {
    showAuthRequiredMessage() {
      return this.$route.query.authRequired;
    }
  },
  methods: {
    async login() {
      try {
        const response = await this.$axios.post('/v1/login', {
          username: this.username,
          password: this.password,
        });
        console.log('Login successful', response);
        this.loginSuccess = true;
        this.errorMessage = '';

      } catch (error) {
        console.log('Login failed', error);
        this.loginSuccess = false;
        this.errorMessage = error.response.data.message || 'Login failed';
      }
    }
  }
};
</script>

<style scoped>
.login-form h2 {
  text-align: center;
  padding: 10px;
  border: 2px solid #4CAF50; /* Измените цвет на ваше усмотрение */
  width: fit-content;
  margin: 0 auto; /* Автоматические отступы для центрирования */
  border-radius: 5px; /* Добавляет скругление углов */
}

/* Стили для формы, чтобы убедиться, что она центрирована и имеет отступы */
.login-form {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px;
}

/* Стилизация кнопок для согласованности */
.login-form button {
  cursor: pointer;
  background-color: #4CAF50;
  color: white;
  border: none;
  padding: 10px;
  margin-top: 10px;
}

.login-form input, .login-form select {
  margin: 10px 0;
  padding: 8px;
  width: 90%;
  box-sizing: border-box;
}

.message.success {
  color: green;
}

</style>
