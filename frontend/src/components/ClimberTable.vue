<template>
  <div class="climbers-table-container">
    <h2>Climbers</h2>

    <Pagination
        :currentPage="currentPage"
        :totalPages="totalPages"
        @nextPage="nextPage"
        @prevPage="prevPage"
    ></Pagination>

    <table>
      <thead>
      <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Birthday</th>
        <th>Category Name</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="climber in paginatedClimbers" :key="climber.id">
        <td>{{ climber.id }}</td>
        <td>{{ climber.name }}</td>
        <td>{{ new Date(climber.birthday).toLocaleDateString() }}</td>
        <td>{{ climber.categoryName }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import Pagination from './UI/Pagination.vue';

export default {
  components: {
    Pagination,
  },
  props: {
    climbers: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      perPage: 10,
      currentPage: 1,
    };
  },
  computed: {
    totalClimbers() {
      return this.climbers.length;
    },
    totalPages() {
      return Math.ceil(this.totalClimbers / this.perPage);
    },
    paginatedClimbers() {
      const start = (this.currentPage - 1) * this.perPage;
      const end = start + this.perPage;
      return this.climbers.slice(start, end);
    },
  },
  methods: {
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
.climbers-table-container {
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
</style>
