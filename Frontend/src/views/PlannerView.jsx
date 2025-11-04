// src/views/PlannerView.jsx
import React from "react";

export default function PlannerView({ panel, setPanel, newPanel, pendingPanel, topologyPanel }) {
  return (
    <div className="p-6">
      <h1 className="text-2xl font-semibold mb-4">Planner — Onboarding & Pending</h1>

      <div className="flex gap-3 mb-6">
        <button
          className={`px-3 py-1 rounded ${panel==='new' ? 'bg-blue-600 text-white' : 'bg-gray-100'}`}
          onClick={() => setPanel('new')}
        >
          New Customer
        </button>

        <button
          className={`px-3 py-1 rounded ${panel==='pending' ? 'bg-blue-600 text-white' : 'bg-gray-100'}`}
          onClick={() => setPanel('pending')}
        >
          Pending Customers
        </button>

        <button
          className={`px-3 py-1 rounded ${panel==='topology' ? 'bg-blue-600 text-white' : 'bg-gray-100'}`}
          onClick={() => setPanel('topology')}
        >
          Topology (view-only)
        </button>
      </div>

      <div className="bg-white shadow rounded p-4">
        {panel === 'new' && newPanel}
        {panel === 'pending' && pendingPanel}
        {panel === 'topology' && topologyPanel}
      </div>
    </div>
  );
}
