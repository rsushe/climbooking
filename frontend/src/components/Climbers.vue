<template>
  <div>
    <ClimberForm @climber-created="fetchClimbers" />
    <ClimberTable :climbers="climbers" />
  </div>
</template>

<script>
import ClimberForm from './ClimberForm.vue'; // Adjust the path
import ClimberTable from './ClimberTable.vue'; // Adjust the path

export default {
  components: {
    ClimberForm,
    ClimberTable,
  },
  data() {
    return {
      climbers: [],
    };
  },
  methods: {
    fetchClimbers() {
      this.$axios.get('/v1/climbers')
          .then(response => {
            this.climbers = response.data;
          })
          .catch(error => {
            console.error('Error fetching climbers:', error);
          });
    },
  },
  created() {
    this.fetchClimbers();
  },
};
</script>

<style scoped>

</style>
