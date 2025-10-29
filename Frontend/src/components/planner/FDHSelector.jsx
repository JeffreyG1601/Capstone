// src/components/planner/FDHSelector.jsx
import React from 'react'
import useFetchFDHs from '../../hooks/useFetchFDHs'

export default function FDHSelector({ value, onChange, name = 'fdhId' }) {
  const { data: fdhs, isLoading, isError } = useFetchFDHs()

  if (isLoading) return <select name={name}><option>Loading FDHs...</option></select>
  if (isError) return <select name={name}><option>Error loading FDHs</option></select>

  return (
    <select name={name} value={value} onChange={onChange}>
      <option value="">Select FDH</option>
      {fdhs.map(f => (
        <option key={f.id ?? f.fdhId ?? f.fdhId} value={f.id ?? f.fdhId ?? f.id}>
          {f.name ?? f.label ?? `FDH ${f.id ?? f.fdhId}`} — {f.location ?? f.address ?? 'unknown'}
        </option>
      ))}
    </select>
  )
}
