// src/hooks/usePlannerOnboard.js
import { useMutation, useQueryClient } from '@tanstack/react-query'
import axios from '../api/axiosInstance'

export default function usePlannerOnboard() {
  const qc = useQueryClient()
  return useMutation(
    (onboardRequest) => axios.post('/planner/onboard', onboardRequest).then(r => r.data),
    {
      onSuccess: (data) => {
        // invalidate lists that likely changed
        qc.invalidateQueries(['customers'])
        qc.invalidateQueries(['tasks'])
      }
    }
  )
}
