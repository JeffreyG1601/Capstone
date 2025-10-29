// src/views/AdminRolesView.jsx
import React from "react";

export default function AdminRolesView({ roles, options, form, setForm, save, del }){
  return (
    <div className="p-4">
      <h2 className="text-xl">Roles</h2>
      <div className="mb-3 flex items-center gap-2">
        <select
          value={form.name}
          onChange={e => setForm({...form, name: e.target.value})}
          className="border px-2 py-1 rounded"
        >
          <option value="">Select Role Type</option>
          {(options || []).map(o => <option key={o} value={o}>{o.replaceAll('_',' ')}</option>)}
        </select>

        <input
          placeholder="Description"
          value={form.description}
          onChange={e => setForm({...form, description: e.target.value})}
          className="border px-2 py-1 rounded"
        />
        <button onClick={save} className="px-3 py-1 bg-blue-600 text-white rounded">Create</button>
      </div>

      <ul>
        {roles.map(r => (
          <li key={r.id} className="mb-2">
            <strong>{r.name}</strong> — {r.description}
            <button onClick={()=> del(r.id)} className="ml-3 text-sm text-red-600">Delete</button>
          </li>
        ))}
      </ul>
    </div>
  );
}
