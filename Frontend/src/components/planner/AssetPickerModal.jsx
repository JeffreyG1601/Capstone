// src/components/planner/AssetPickerModal.jsx
import React, { useEffect, useState } from 'react'
import axios from '../../api/axiosInstance'

export default function AssetPickerModal({ onSelect, assetType = 'ONT', isOpen, onClose }) {
  const [assets, setAssets] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (!isOpen) return
    setLoading(true)
    axios.get(`/assets/helper?type=${encodeURIComponent(assetType)}`)
      .then(r => setAssets(r.data || []))
      .catch(e => setError(e))
      .finally(() => setLoading(false))
  }, [isOpen, assetType])

  if (!isOpen) return null

  return (
    <div className="modal-backdrop">
      <div className="modal">
        <header><h3>Choose {assetType}</h3><button onClick={onClose}>×</button></header>
        {loading && <div>Loading assets...</div>}
        {error && <div>Error loading assets</div>}
        <ul>
          {assets.map(a => (
            <li key={a.assetId ?? a.id}>
              <button onClick={() => { onSelect(a); onClose(); }}>
                {a.model ?? a.assetType} — {a.serialNumber ?? a.serial}
              </button>
            </li>
          ))}
          {assets.length === 0 && !loading && <li>No available assets</li>}
        </ul>
      </div>
    </div>
  )
}
