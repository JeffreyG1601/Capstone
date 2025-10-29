// src/controllers/AdminAuditController.jsx
import React, { useEffect, useState } from "react";
import AdminAuditView from "../views/AdminAuditView";
import axios from "../services/axiosInstance";

export default function AdminAuditController(){
  const [rows, setRows] = useState([]);
  const [filters, setFilters] = useState({ username: "", actionType: "", from: "", to: "" });
  const [page, setPage] = useState(0);

  const fetch = async (p = 0) => {
    try {
      const res = await axios.get("/admin/audit-logs", { params: { ...filters, page: p, size: 20 } });
      // backend returns a Page object -> content, or plain array
      const payload = res.data && res.data.content ? res.data.content : (res.data || []);
      setRows(payload);
      setPage(p);
    } catch (err) {
      console.error("fetch audit logs", err);
      setRows([]);
    }
  };

  useEffect(() => { fetch(0); /* eslint-disable-next-line */ }, []);

  const exportPdf = async () => {
    try {
      const res = await axios.get("/admin/audit-logs/export/pdf", { params: filters, responseType: "blob" });
      const url = window.URL.createObjectURL(new Blob([res.data], { type: "application/pdf" }));
      const a = document.createElement("a"); a.href = url; a.download = "audit-report.pdf"; a.click();
    } catch (e) {
      console.error("export pdf", e);
    }
  };

  return <AdminAuditView rows={rows} fetch={fetch} setFilters={setFilters} exportPdf={exportPdf} page={page} setPage={setPage} />;
}
