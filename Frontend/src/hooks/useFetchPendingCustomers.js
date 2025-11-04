// src/hooks/useFetchPendingCustomers.js
import { useQuery } from "@tanstack/react-query";
import axios from "../services/axiosInstance"; // correct relative path

export default function useFetchPendingCustomers() {
  return useQuery({
    queryKey: ["pendingCustomers"],
    queryFn: async () => {
      const res = await axios.get("/planner/pending");
      return res.data || [];
    },
    staleTime: 5 * 1000,
    retry: false,
  });
}
