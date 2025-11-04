// src/controllers/PlannerController.jsx
import React, { useState } from "react";
import PlannerView from "../views/PlannerView";
import OnboardForm from "../components/planner/OnboardForm";
import PendingCustomers from "../components/planner/PendingCustomers";

/**
 * PlannerController - top-level planner page controller.
 * Provides tab switching between New Customer, Pending, and Topology.
 */
export default function PlannerController() {
  const [panel, setPanel] = useState("new"); // "new" | "pending" | "topology"

  return (
    <PlannerView
      panel={panel}
      setPanel={setPanel}
      newPanel={<OnboardForm onSuccess={() => setPanel('pending')} />}
      pendingPanel={<PendingCustomers />}
      topologyPanel={<div className="p-6">Topology viewer coming soon. (Button placeholder)</div>}
    />
  );
}
