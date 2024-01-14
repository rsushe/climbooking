<!-- Pagination.vue -->
<template>
  <div class="pagination">
    <button @click="prevPage" :disabled="currentPage === 1" class="pagination-button">Previous</button>
    <span class="pagination-current">{{ currentPage }}</span>
    <button @click="nextPage" :disabled="currentPage === totalPages" class="pagination-button">Next</button>
  </div>
</template>

<script>
export default {
  props: {
    currentPage: Number,
    totalPages: Number,
    nextPage: Function,
    prevPage: Function,
  },
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination-button {
  margin: 0 5px;
  padding: 8px 12px;
  cursor: pointer;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.pagination-button:hover {
  background-color: #45a049;
}

.pagination-current {
  margin: 0 10px;
  font-size: 18px;
  font-weight: bold;
}
</style>
