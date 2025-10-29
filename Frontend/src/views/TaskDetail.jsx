// src/views/TaskDetail.jsx
import React, { useState } from "react";
import axios from "../services/axiosInstance";

export default function TaskDetail({ task, onTaskUpdated }) {
  const [note, setNote] = useState("");
  const [busy, setBusy] = useState(false);

  async function startTask() {
    setBusy(true);
    try {
      const res = await axios.post(`/technician/tasks/${task.taskId}/start`);
      onTaskUpdated(res.data);
    } catch (e) {
      alert("Start failed: " + (e?.response?.data || e.message));
    } finally { setBusy(false); }
  }

  async function toggleItem(item) {
    try {
      const done = !item.done;
      const res = await axios.post(`/technician/tasks/${task.taskId}/checklist/${item.checklistItemId}?done=${done}`);
      onTaskUpdated(res.data);
    } catch (e) {
      alert("Checklist update failed: " + (e?.response?.data || e.message));
    }
  }

  async function addNote() {
    if (!note.trim()) return;
    setBusy(true);
    try {
      const payload = { author: localStorage.getItem("techName") || "Technician", note };
      const res = await axios.post(`/technician/tasks/${task.taskId}/notes`, payload);
      onTaskUpdated(res.data);
      setNote("");
    } catch (e) {
      alert("Add note failed: " + (e?.response?.data || e.message));
    } finally { setBusy(false); }
  }

  async function completeTask() {
    if (!window.confirm("Mark task completed?")) return;
    setBusy(true);
    try {
      await axios.post(`/technician/tasks/${task.taskId}/complete`);
      const techId = task.technician?.technicianId ?? Number(localStorage.getItem("techId"));
      if (techId) {
        const res = await axios.get(`/technician/tasks/technician/${techId}`);
        const updated = res.data.find((t) => t.taskId === task.taskId);
        if (updated) onTaskUpdated(updated);
        else onTaskUpdated({ ...task, status: "COMPLETED" });
      } else {
        onTaskUpdated({ ...task, status: "COMPLETED" });
      }
      alert("Task completed — customer & inventory updated (if applicable).");
    } catch (e) {
      alert("Complete failed: " + (e?.response?.data || e.message));
    } finally { setBusy(false); }
  }

  return (
    <div className="border rounded p-4">
      <div className="flex justify-between items-start">
        <div>
          <h3 className="text-xl font-semibold">{task.title ?? `Task #${task.taskId}`}</h3>
          <div className="text-sm text-gray-600">Customer: {task.customer?.name} — {task.customer?.address}</div>
          <div className="text-sm text-gray-500">Scheduled: {task.scheduledDate ?? "—"}</div>
        </div>
        <div className="space-x-2">
          <button className="px-3 py-1 rounded bg-indigo-600 text-white" onClick={startTask} disabled={busy || task.status === "IN_PROGRESS"}>Start</button>
          <button className="px-3 py-1 rounded bg-green-600 text-white" onClick={completeTask} disabled={busy || task.status === "COMPLETED"}>Complete</button>
        </div>
      </div>

      <section className="mt-4">
        <h4 className="font-medium">Checklist</h4>
        <ul className="mt-2 space-y-2">
          {(!task.checklist || task.checklist.length === 0) && <div className="text-sm text-gray-500">No checklist defined</div>}
          {task.checklist?.map((it) => (
            <li key={it.checklistItemId} className="flex items-center gap-3">
              <input type="checkbox" checked={!!it.done} onChange={() => toggleItem(it)} />
              <div>
                <div className="font-medium">{it.label}</div>
                {it.details && <div className="text-xs text-gray-500">{it.details}</div>}
              </div>
            </li>
          ))}
        </ul>
      </section>

      <section className="mt-4">
        <h4 className="font-medium">Installation Notes</h4>
        <textarea className="w-full border rounded p-2 mt-2" rows={3} value={note} onChange={(e) => setNote(e.target.value)} placeholder="Add note (serials, observations)"/>
        <div className="mt-2">
          <button className="px-3 py-1 rounded bg-indigo-600 text-white" onClick={addNote} disabled={busy}>Add Note</button>
        </div>

        <div className="mt-3 space-y-2">
          {(!task.notesList || task.notesList.length === 0) && <div className="text-sm text-gray-500">No notes yet</div>}
          {task.notesList?.map(n => (
            <div key={n.noteId} className="border rounded p-2">
              <div className="text-xs text-gray-500">{n.author} • {new Date(n.timestamp).toLocaleString()}</div>
              <div>{n.note}</div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}
