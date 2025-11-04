// src/hooks/usePlannerOnboard.js
import { useMutation, useQueryClient } from '@tanstack/react-query'
import axios from '../services/axiosInstance'

export default function usePlannerOnboard() {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: async (onboardRequest) => {
      try {
        const res = await axios.post('/planner/onboard', onboardRequest)
        return res.data
      } catch (err) {
        // axios error with response from server
        if (err.response) {
          if (err.response.status === 409) {
            // backend may return plain text or JSON message
            const msg = typeof err.response.data === 'string'
              ? err.response.data
              : err.response.data?.message || 'Selected port is already assigned'
            throw new Error(msg)
          }
          const msg = err.response.data?.message || err.response.statusText || 'Server error'
          throw new Error(msg)
        }
        throw new Error(err.message || 'Network error')
      }
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['customers'] })
      queryClient.invalidateQueries({ queryKey: ['tasks'] })
    },
    // avoid automatic retry on POST conflicts
    retry: false,
  })
}
