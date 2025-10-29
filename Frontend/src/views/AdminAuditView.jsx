// frontend/src/views/AdminAuditView.jsx
import React from "react";

export default function AdminAuditView({ rows, fetch, setFilters, exportPdf, page, setPage }){
  return (
    <div className="p-4">
      <h2 className="text-xl mb-2">Audit Logs</h2>
      <div className="flex gap-2 mb-3">
        <input placeholder="User" onChange={e=> setFilters(f=>({...f, username: e.target.value}))} />
        <input placeholder="Action" onChange={e=> setFilters(f=>({...f, actionType: e.target.value}))} />
        <input type="datetime-local" onChange={e=> setFilters(f=>({...f, from: e.target.value}))} />
        <input type="datetime-local" onChange={e=> setFilters(f=>({...f, to: e.target.value}))} />
        <button onClick={()=> fetch(0)}>Filter</button>
        <button onClick={exportPdf}>Export PDF</button>
      </div>
      <table className="w-full">
        <thead><tr><th>Time</th><th>User</th><th>Action</th><th>IP</th><th>Desc</th></tr></thead>
        <tbody>
          {rows.map(r=> <tr key={r.id}><td>{r.timestamp}</td><td>{r.username}</td><td>{r.actionType}</td><td>{r.ipAddress}</td><td>{r.description}</td></tr>)}
        </tbody>
      </table>
      <div className="mt-3">
        <button onClick={()=> fetch(page-1)} disabled={page<=0}>Prev</button>
        <span> Page {page+1} </span>
        <button onClick={()=> fetch(page+1)}>Next</button>
      </div>
    </div>
  );
}
