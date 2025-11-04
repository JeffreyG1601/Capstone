// src/hooks/useFetchFDHs.js
import { useQuery } from '@tanstack/react-query'
import axios from '../services/axiosInstance'

export default function useFetchFDHs() {
  return useQuery({
    queryKey: ['fdhs'],
    queryFn: async () => {
      const r = await axios.get('/fdhs')
      return r.data
    },
  })
}
