import React, { useEffect, useState } from 'react'
import axios from '../../services/axiosInstance'

export default function SplitterSelector({ fdhId, value, onChange, onAssignedPortSuggested }) {
  const [splitters, setSplitters] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!fdhId) {
      setSplitters([])
      return
    }
    setLoading(true)
    axios.get(`/fdhs/${fdhId}/splitters`)
      .then(r => {
        const data = r.data || []
        // ensure availablePorts property for display (backend should set it but fallback here)
        const enriched = data.map(s => ({
          ...s,
          usedPorts: s.usedPorts || 0,
          portCapacity: s.portCapacity || 0,
          availablePorts: (s.portCapacity || 0) - (s.usedPorts || 0)
        }))
        setSplitters(enriched)
      })
      .catch(() => setError('Failed to load splitters'))
      .finally(() => setLoading(false))
  }, [fdhId])

  function handleSelect(e) {
    const id = e.target.value ? Number(e.target.value) : ''
    const selected = splitters.find(s => String(s.splitterId) === String(id))
    onChange(id)
    if (selected) {
      const used = Number(selected.usedPorts || 0)
      const capacity = Number(selected.portCapacity || 0)
      const next = (used + 1) <= capacity ? (used + 1) : null
      onAssignedPortSuggested(next)
    } else onAssignedPortSuggested(null)
  }

  if (!fdhId) return <div className="text-sm">Select FDH first</div>
  if (loading) return <div className="text-sm">Loading splitters...</div>
  if (error) return <div className="text-sm text-red-600">{error}</div>

  return (
    <div>
      <select value={value || ''} onChange={handleSelect}>
        <option value="">Select splitter</option>
        {splitters.map(s => (
          <option key={s.splitterId} value={s.splitterId}>
            {s.model} — {s.availablePorts} free of {s.portCapacity}
          </option>
        ))}
      </select>

      {value && (() => {
        const sel = splitters.find(s => String(s.splitterId) === String(value))
        if (!sel) return null
        return (
          <div className="mt-1 text-xs text-gray-600">
            Used: {sel.usedPorts || 0} | Available: {sel.availablePorts || 0}
          </div>
        )
      })()}
    </div>
  )
}
