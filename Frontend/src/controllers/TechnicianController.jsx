// src/controllers/TechnicianController.jsx
import React, { useEffect, useState } from "react";
import axios from "../services/axiosInstance";
import TaskListItem from "../views/TaskListItem";
import TaskDetail from "../views/TaskDetail";

/**
 * TechnicianController (controller in MVC) contains state, effects and orchestration for technician view.
 */
export default function TechnicianController({ techIdProp }) {
  const storedId = localStorage.getItem("techId");
  const techId = techIdProp ?? (storedId ? Number(storedId) : null);

  const [tasks, setTasks] = useState([]);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(false);
  const [err, setErr] = useState(null);

  const fetchTasks = async () => {
    if (!techId) return setErr("No technician id available");
    setLoading(true); setErr(null);
    try {
      const res = await axios.get(`/technician/tasks/technician/${techId}`);
      setTasks(res.data || []);
      // refresh selected if present
      if (selected) {
        const updated = (res.data || []).find((t) => t.taskId === selected.taskId);
        if (updated) setSelected(updated);
      }
    } catch (e) {
      setErr(e?.response?.data || e.message || "Failed to load tasks");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { fetchTasks(); /* eslint-disable-next-line*/ }, [techId]);

  const handleUpdated = (updatedTask) => {
    setTasks((prev) => prev.map((t) => (t.taskId === updatedTask.taskId ? updatedTask : t)));
    setSelected(updatedTask);
  };

  return (
    <div className="p-4 max-w-6xl mx-auto">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-semibold">Field Technician</h1>
        <div className="text-sm text-gray-600">Tech ID: {techId ?? "—"}</div>
      </div>

      {err && <div className="mb-4 text-red-600">{String(err)}</div>}

      <div className="grid grid-cols-3 gap-6">
        <div>
          <div className="flex justify-between items-center mb-3">
            <h2 className="font-medium">My Tasks</h2>
            <button className="text-sm underline" onClick={fetchTasks}>Refresh</button>
          </div>

          <div className="space-y-2">
            {loading && <div className="text-sm text-gray-500">Loading…</div>}
            {!loading && tasks.length === 0 && <div className="text-sm text-gray-500">No tasks</div>}
            {tasks.map((t) => (
              <TaskListItem
                key={t.taskId}
                task={t}
                isSelected={selected?.taskId === t.taskId}
                onSelect={() => setSelected(t)}
              />
            ))}
          </div>
        </div>

        <div className="col-span-2">
          {selected ? (
            <TaskDetail task={selected} onTaskUpdated={handleUpdated} />
          ) : (
            <div className="p-6 border rounded text-gray-600">Select a task to view details.</div>
          )}
        </div>
      </div>
    </div>
  );
}
