// src/controllers/PlannerController.jsx
import React, { useEffect, useState } from "react";
import PlannerView from "../views/PlannerView";
import axios from "../services/axiosInstance";

/**
 * PlannerController - fetch FDHs, Splitters, Technicians and manage form state
 */
export default function PlannerController() {
  const [fdhs, setFdhs] = useState([]);
  const [splitters, setSplitters] = useState([]);
  const [technicians, setTechnicians] = useState([]);
  const [form, setForm] = useState({
    name: "",
    address: "",
    neighborhood: "",
    plan: "",
    connectionType: "FIBER", // default to backend enum
    fdhId: "",
    splitterId: "",
    assignedPort: "",
    technicianId: "",
    scheduledDate: ""
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchFDHs();
    fetchTechnicians();
  }, []);

  async function fetchFDHs() {
    try {
      const res = await axios.get(`/fdhs`);
      const data = res.data || [];
      // normalize IDs
      setFdhs(data.map(d => ({ ...d, fdhId: d.fdhId ?? d.id })));
    } catch (err) {
      console.error(err);
      setFdhs([]);
      setError("Failed to load FDHs");
    }
  }

  async function fetchSplitters(fdhId) {
    setSplitters([]);
    if (!fdhId) return;
    try {
      const res = await axios.get(`/fdhs/${fdhId}/splitters`);
      const data = res.data || [];
      setSplitters(data.map(s => ({ ...s, splitterId: s.splitterId ?? s.id })));
    } catch (err) {
      console.error(err);
      setSplitters([]);
      setError("Failed to load splitters");
    }
  }

  async function fetchTechnicians() {
    try {
      const res = await axios.get(`/technicians`);
      const data = res.data || [];
      setTechnicians(data.map(t => ({ ...t, technicianId: t.technicianId ?? t.id })));
    } catch (err) {
      console.error(err);
      setTechnicians([]);
      setError("Failed to load technicians");
    }
  }

  return (
    <PlannerView
      fdhs={fdhs}
      splitters={splitters}
      technicians={technicians}
      form={form}
      setForm={setForm}
      fetchSplitters={fetchSplitters}
      loading={loading}
      error={error}
    />
  );
}
