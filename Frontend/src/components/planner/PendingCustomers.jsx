// src/components/planner/PendingCustomers.jsx
import React, { useState } from 'react'
import useFetchPendingCustomers from '../../hooks/useFetchPendingCustomers'
import usePlannerOnboard from '../../hooks/usePlannerOnboard'
import useFetchTechnicians from '../../hooks/useFetchTechnicians'
import AssetPickerModal from './AssetPickerModal'

export default function PendingCustomers() {
  const { data: pending = [], isLoading, isError } = useFetchPendingCustomers()
  const { data: techs = [] } = useFetchTechnicians()
  const onboardMutation = usePlannerOnboard()
  const [assetPicker, setAssetPicker] = useState({ open: false, type: null, field: null, customerId: null })
  const [localAssign, setLocalAssign] = useState({}) // { customerId: { technicianId, ontAssetId, routerAssetId, scheduledDate } }

  // Filter to only customers that do NOT have ONT or Router assigned yet.
  const pendingUnassigned = (pending || []).filter(c => {
    // resilient checks for various possible backend field names/structures
    const hasONT = Boolean(
      c.ontAssetId || c.ont_asset_id || c.ont?.assetId || c.ontAsset || c.ont || c.assignedOntId || c.assigned_ont_id
    )
    const hasRouter = Boolean(
      c.routerAssetId || c.router_asset_id || c.router?.assetId || c.routerAsset || c.router || c.assignedRouterId || c.assigned_router_id
    )
    return !hasONT && !hasRouter
  })

  if (isLoading) return <div>Loading pending customers...</div>
  if (isError) return <div className="text-red-600">Failed to load pending customers</div>

  function openPicker(customerId, field, type) {
    setAssetPicker({ open: true, type, field, customerId })
  }

  function onAssetSelect(asset) {
    const { customerId, field } = assetPicker
    setLocalAssign(prev => ({
      ...prev,
      [customerId]: { ...(prev[customerId] || {}), [field]: asset.assetId ?? asset.id }
    }))
    setAssetPicker({ open: false, type: null, field: null, customerId: null })
  }

  function setTech(customerId, techId) {
    setLocalAssign(prev => ({ ...prev, [customerId]: { ...(prev[customerId] || {}), technicianId: techId } }))
  }

  function setDate(customerId, dt) {
    setLocalAssign(prev => ({ ...prev, [customerId]: { ...(prev[customerId] || {}), scheduledDate: dt } }))
  }

  function assignNow(customer) {
    const payload = {
      customerName: customer.name || customer.fullName || '',
      address: customer.address || '',
      neighborhood: customer.neighborhood || '',
      plan: customer.plan || '',
      fdhId: customer.fdh?.fdhId ?? customer.fdhId ?? '',
      splitterId: customer.splitter?.splitterId ?? customer.splitterId ?? customer.splitterId,
      assignedPort: customer.assignedPort ?? customer.assigned_port ?? '',
      ontAssetId: (localAssign[customer.customerId] || {}).ontAssetId,
      routerAssetId: (localAssign[customer.customerId] || {}).routerAssetId,
      technicianId: (localAssign[customer.customerId] || {}).technicianId,
      scheduledDate: (localAssign[customer.customerId] || {}).scheduledDate
    }

    // Keep minimal required fields; backend should handle merging. This will attempt to assign assets/tech.
    onboardMutation.mutate(payload)
  }

  return (
    <div>
      {pendingUnassigned.length === 0 && <div>No pending customers.</div>}
      <ul className="space-y-4">
        {pendingUnassigned.map(c => (
          <li key={c.customerId} className="border rounded p-3">
            <div className="flex justify-between items-start">
              <div>
                <div className="font-semibold">{c.name || c.customerName || `Customer ${c.customerId}`}</div>
                <div className="text-sm text-gray-600">{c.address}</div>
                <div className="text-xs text-gray-500 mt-1">Status: {c.status}</div>
              </div>

              <div className="space-y-2 text-right">
                <div>
                  <div className="text-xs">Technician</div>
                  <select
                    value={(localAssign[c.customerId] || {}).technicianId || ''}
                    onChange={(e) => setTech(c.customerId, e.target.value)}
                    className="border px-2 py-1 rounded"
                  >
                    <option value="">Select</option>
                    {techs.map(t => <option key={t.technicianId ?? t.id} value={t.technicianId ?? t.id}>{t.name}</option>)}
                  </select>
                </div>

                <div>
                  <div className="text-xs">ONT</div>
                  <button className="px-2 py-1 border rounded" onClick={() => openPicker(c.customerId, 'ontAssetId', 'ONT')}>
                    Pick ONT
                  </button>
                  <div className="text-xs mt-1">{(localAssign[c.customerId] || {}).ontAssetId || '—'}</div>
                </div>

                <div>
                  <div className="text-xs">Router</div>
                  <button className="px-2 py-1 border rounded" onClick={() => openPicker(c.customerId, 'routerAssetId', 'ROUTER')}>
                    Pick Router
                  </button>
                  <div className="text-xs mt-1">{(localAssign[c.customerId] || {}).routerAssetId || '—'}</div>
                </div>

                <div>
                  <div className="text-xs">Scheduled</div>
                  <input
                    type="datetime-local"
                    value={(localAssign[c.customerId] || {}).scheduledDate || ''}
                    onChange={(e) => setDate(c.customerId, e.target.value)}
                    className="border px-2 py-1 rounded"
                  />
                </div>

                <div>
                  <button
                    onClick={() => assignNow(c)}
                    className="mt-2 bg-green-600 text-white px-3 py-1 rounded"
                    disabled={onboardMutation.isLoading}
                  >
                    Assign / Push
                  </button>
                </div>

                {onboardMutation.isError && <div className="text-red-600 text-xs mt-1">{onboardMutation.error?.message}</div>}
                {onboardMutation.isSuccess && <div className="text-green-600 text-xs mt-1">Assigned. {onboardMutation.data?.message}</div>}
              </div>
            </div>
          </li>
        ))}
      </ul>

      <AssetPickerModal
        isOpen={assetPicker.open}
        assetType={assetPicker.type}
        onClose={() => setAssetPicker({ open: false, type: null, field: null, customerId: null })}
        onSelect={onAssetSelect}
      />
    </div>
  )
}
