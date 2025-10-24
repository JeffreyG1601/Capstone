import React, { useEffect, useState } from "react";

const API = ""; // use proxy or set "http://localhost:8080"

export default function PlannerDashboard() {
  const [fdhs, setFdhs] = useState([]);
  const [splitters, setSplitters] = useState([]);
  const [assetsONT, setAssetsONT] = useState([]);
  const [assetsRouter, setAssetsRouter] = useState([]);
  const [technicians, setTechnicians] = useState([]);

  const [form, setForm] = useState({
    name: "",
    address: "",
    neighborhood: "",
    plan: "",
    connectionType: "Wired",
    fdhId: "",
    splitterId: "",
    assignedPort: "",
    ontAssetId: "",
    routerAssetId: "",
    technicianId: "",
    scheduledDate: ""
  });

  const [message, setMessage] = useState(null);

  useEffect(() => {
    fetchFDHs();
    fetchTechnicians();
  }, []);

  async function fetchFDHs() {
    const res = await fetch(`${API}/api/fdhs`);
    const data = await res.json();
    setFdhs(data || []);
  }

  async function fetchSplitters(fdhId) {
    setSplitters([]);
    if (!fdhId) return;
    const res = await fetch(`${API}/api/fdhs/${fdhId}/splitters`);
    const data = await res.json();
    setSplitters(data || []);
  }

  async function fetchAvailableAssets(type) {
    const res = await fetch(`${API}/api/assets/helper/available?type=${type}`);
    const data = await res.json();
    return data || [];
  }

  async function fetchTechnicians() {
    const res = await fetch(`${API}/api/technician`); // assume exists; else /api/technicians
    if (!res.ok) {
      // fallback: try /api/technicians
      const res2 = await fetch(`${API}/api/technicians`);
      if (res2.ok) {
        setTechnicians(await res2.json());
      }
      return;
    }
    setTechnicians(await res.json());
  }

  async function onFdhChange(e) {
    const id = e.target.value;
    setForm(f => ({ ...f, fdhId: id, splitterId: "" }));
    await fetchSplitters(id);
  }

  async function onSplitterChange(e) {
    const sid = e.target.value;
    setForm(f => ({ ...f, splitterId: sid }));
    // load available assets for ONT and Router
    const ont = await fetchAvailableAssets("ONT");
    const router = await fetchAvailableAssets("Router");
    setAssetsONT(ont);
    setAssetsRouter(router);
  }

  async function handleOnboard(e) {
    e.preventDefault();
    setMessage(null);
    // basic validation
    if (!form.name || !form.splitterId || !form.assignedPort) {
      setMessage({ type: "error", text: "Name, splitter and assignedPort required." });
      return;
    }

    // Prepare payload
    const payload = {
      name: form.name,
      address: form.address,
      neighborhood: form.neighborhood,
      plan: form.plan,
      connectionType: form.connectionType,
      fdhId: form.fdhId ? Number(form.fdhId) : null,
      splitterId: Number(form.splitterId),
      assignedPort: Number(form.assignedPort),
      ontAssetId: form.ontAssetId ? Number(form.ontAssetId) : null,
      routerAssetId: form.routerAssetId ? Number(form.routerAssetId) : null,
      technicianId: form.technicianId ? Number(form.technicianId) : null,
      scheduledDate: form.scheduledDate || null
    };

    try {
      const res = await fetch(`${API}/api/planner/onboard`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      });
      if (!res.ok) {
        const text = await res.text();
        setMessage({ type: "error", text: `Failed: ${res.status} ${text}` });
        return;
      }
      const data = await res.json();
      setMessage({ type: "success", text: `Onboarded customer ${data.customerId}. Task ${data.deploymentTaskId}` });
      // reset form or partial
      setForm({
        name: "",
        address: "",
        neighborhood: "",
        plan: "",
        connectionType: "Wired",
        fdhId: "",
        splitterId: "",
        assignedPort: "",
        ontAssetId: "",
        routerAssetId: "",
        technicianId: "",
        scheduledDate: ""
      });
    } catch (err) {
      setMessage({ type: "error", text: err.message });
    }
  }

  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Planner — Onboarding</h1>

      {message && (
        <div className={`p-3 mb-4 ${message.type === "error" ? "bg-red-100 text-red-800" : "bg-green-100 text-green-800"}`}>
          {message.text}
        </div>
      )}

      <form onSubmit={handleOnboard} className="space-y-3 max-w-2xl">
        <div>
          <label className="block text-sm">Customer name</label>
          <input value={form.name} onChange={e=>setForm(f=>({...f, name: e.target.value}))} className="w-full border px-2 py-1 rounded" />
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm">Address</label>
            <input value={form.address} onChange={e=>setForm(f=>({...f, address: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>
          <div>
            <label className="block text-sm">Neighborhood</label>
            <input value={form.neighborhood} onChange={e=>setForm(f=>({...f, neighborhood: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>
        </div>

        <div className="grid grid-cols-3 gap-3">
          <div>
            <label className="block text-sm">Plan</label>
            <input value={form.plan} onChange={e=>setForm(f=>({...f, plan: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>
          <div>
            <label className="block text-sm">Connection Type</label>
            <select value={form.connectionType} onChange={e=>setForm(f=>({...f, connectionType: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option>Wired</option>
              <option>Wireless</option>
            </select>
          </div>
          <div>
            <label className="block text-sm">FDH</label>
            <select value={form.fdhId} onChange={onFdhChange} className="w-full border px-2 py-1 rounded">
              <option value="">Select FDH</option>
              {fdhs.map(f => <option key={f.id} value={f.id}>{f.name} ({f.region})</option>)}
            </select>
          </div>
        </div>

        <div className="grid grid-cols-3 gap-3">
          <div>
            <label className="block text-sm">Splitter</label>
            <select value={form.splitterId} onChange={onSplitterChange} className="w-full border px-2 py-1 rounded">
              <option value="">Select Splitter</option>
              {splitters.map(s => <option key={s.splitterId} value={s.splitterId}>{s.model} - Ports {s.portCapacity} - used {s.usedPorts}</option>)}
            </select>
          </div>

          <div>
            <label className="block text-sm">Assigned Port (number)</label>
            <input type="number" value={form.assignedPort} onChange={e=>setForm(f=>({...f, assignedPort: e.target.value}))} className="w-full border px-2 py-1 rounded" />
          </div>

          <div>
            <label className="block text-sm">Technician</label>
            <select value={form.technicianId} onChange={e=>setForm(f=>({...f, technicianId: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="">Select (optional)</option>
              {technicians.map(t => <option key={t.technicianId} value={t.technicianId}>{t.name} - {t.region}</option>)}
            </select>
          </div>
        </div>

        <div className="grid grid-cols-2 gap-3">
          <div>
            <label className="block text-sm">ONT (Available)</label>
            <select value={form.ontAssetId} onChange={e=>setForm(f=>({...f, ontAssetId: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="">None</option>
              {assetsONT.map(a => <option key={a.assetId} value={a.assetId}>{a.serialNumber} - {a.model}</option>)}
            </select>
          </div>

          <div>
            <label className="block text-sm">Router (Available)</label>
            <select value={form.routerAssetId} onChange={e=>setForm(f=>({...f, routerAssetId: e.target.value}))} className="w-full border px-2 py-1 rounded">
              <option value="">None</option>
              {assetsRouter.map(a => <option key={a.assetId} value={a.assetId}>{a.serialNumber} - {a.model}</option>)}
            </select>
          </div>
        </div>

        <div>
          <label className="block text-sm">Scheduled Date</label>
          <input type="date" value={form.scheduledDate} onChange={e=>setForm(f=>({...f, scheduledDate: e.target.value}))} className="border px-2 py-1 rounded" />
        </div>

        <div>
          <button type="submit" className="px-3 py-1 bg-blue-600 text-white rounded">Onboard & Create Task</button>
        </div>
      </form>
    </div>
  );
}
