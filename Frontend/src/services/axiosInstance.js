// src/services/axiosInstance.js
import axios from "axios";

const instance = axios.create({
  baseURL: "/api", // frontend requests use paths like "/admin/roles" -> final "/api/admin/roles"
  timeout: 10000,
});

export default instance;
