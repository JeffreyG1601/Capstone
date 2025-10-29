// src/views/TaskListItem.jsx
import React from "react";

export default function TaskListItem({ task, isSelected, onSelect }) {
  const statusStyle = {
    PENDING: "bg-yellow-100 text-yellow-800",
    SCHEDULED: "bg-blue-100 text-blue-800",
    IN_PROGRESS: "bg-indigo-100 text-indigo-800",
    COMPLETED: "bg-green-100 text-green-800",
    CANCELLED: "bg-gray-100 text-gray-600",
  }[task.status] || "bg-gray-50 text-gray-700";

  return (
    <div
      onClick={onSelect}
      className={`border rounded p-3 cursor-pointer ${isSelected ? "ring-2 ring-indigo-300" : ""}`}
    >
      <div className="flex justify-between items-center">
        <div className="font-medium">{task.title ?? `Task #${task.taskId}`}</div>
        <div className={`px-2 py-1 rounded text-xs ${statusStyle}`}>{task.status}</div>
      </div>
      <div className="text-xs text-gray-500 mt-1">
        {task.customer?.name ?? "—"} • {task.customer?.address ?? ""}
      </div>
    </div>
  );
}
