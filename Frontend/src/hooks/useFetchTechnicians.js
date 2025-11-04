// src/hooks/useFetchTechnicians.js
import { useQuery } from '@tanstack/react-query'
import axios from '../services/axiosInstance'

export default function useFetchTechnicians() {
  return useQuery({
    queryKey: ['technicians'],
    queryFn: async () => {
      const r = await axios.get('/technicians')
      return r.data || []
    },
  })
}
