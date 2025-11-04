// src/services/axiosInstance.js
import axios from "axios";

const baseURL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

const instance = axios.create({
  baseURL,
  timeout: 15000,
  headers: {
    "Content-Type": "application/json",
  },
});

// request interceptor (optional)
instance.interceptors.request.use((cfg) => {
  // attach auth header if you have tokens, e.g.
  // const token = localStorage.getItem('authToken');
  // if (token) cfg.headers.Authorization = `Bearer ${token}`;
  return cfg;
}, (err) => Promise.reject(err));

// response interceptor (optional - better error messages)
instance.interceptors.response.use(
  (res) => res,
  (err) => {
    // map axios error to something friendly for UI
    if (err.response && err.response.data) {
      return Promise.reject(err.response.data);
    }
    return Promise.reject(err.message || "Network error");
  }
);

export default instance;
