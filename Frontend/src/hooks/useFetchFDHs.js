// src/hooks/useFetchFDHs.js
import { useQuery } from '@tanstack/react-query'
import axios from '../api/axiosInstance'

export default function useFetchFDHs() {
  return useQuery(['fdhs'], () => axios.get('/fdhs').then(r => r.data))
}
