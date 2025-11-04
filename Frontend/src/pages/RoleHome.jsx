import React from 'react'
import PlannerView from '../views/PlannerView'
import TechnicianDashboard from '../views/TechnicianDashboard'
import AdminAuditView from '../views/AdminAuditView'
import AdminRolesView from '../views/AdminRolesView'
import TaskListItem from '../components/TaskListItem'


// Fallback lightweight pages when a specific view is not present
function SupportAgentFallback(){
return (
<div className="grid grid-cols-2 gap-4">
<div className="p-6 bg-white rounded shadow">Open Tickets</div>
<div className="p-6 bg-white rounded shadow">Customer Lookup</div>
</div>
)
}
function PlannerManagerFallback(){
return (
<div className="grid grid-cols-3 gap-4">
<div className="p-6 bg-white rounded shadow">KPI</div>
<div className="p-6 bg-white rounded shadow">Capacity</div>
<div className="p-6 bg-white rounded shadow">Region Summary</div>
</div>
)
}


export default function RoleHome(){
const { user, logout } = useAuth()
if (!user) return <div className="min-h-screen flex items-center justify-center">Not logged</div>
const role = user.role


return (
<div className="min-h-screen bg-gray-50 p-6">
<div className="max-w-7xl mx-auto grid grid-cols-12 gap-6">
<aside className="col-span-3">
<div className="bg-white p-4 rounded shadow">
<div className="font-medium">{user.name}</div>
<div className="text-xs text-gray-500">{role}</div>
<button onClick={logout} className="mt-3 px-3 py-1 rounded bg-red-600 text-white text-sm">Sign out</button>
</div>
<div className="mt-4 space-y-2">
<button className="w-full text-left p-2 rounded hover:bg-gray-100">Overview</button>
<button className="w-full text-left p-2 rounded hover:bg-gray-100">Assets</button>
<button className="w-full text-left p-2 rounded hover:bg-gray-100">Tasks</button>
<button className="w-full text-left p-2 rounded hover:bg-gray-100">Reports</button>
</div>
</aside>


<main className="col-span-9 space-y-4">
{role === 'INVENTORY_MANAGER' && <InventoryView />}
{role === 'NETWORK_PLANNER' && <PlannerView />}
{role === 'TECHNICIAN' && <TechnicianDashboard />}
{role === 'SUPPORT_AGENT' && <SupportAgentFallback />}
{role === 'ADMIN' && (
<div className="space-y-4">
<AdminAuditView />
<AdminRolesView />
</div>
)}
{role === 'PLANNER_MANAGER' && <PlannerManagerFallback />}
</main>
</div>
</div>
)
}