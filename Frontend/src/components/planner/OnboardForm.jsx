// src/components/planner/OnboardForm.jsx
import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import FDHSelector from './FDHSelector'
import SplitterSelector from './SplitterSelector'
import AssetPickerModal from './AssetPickerModal'
import usePlannerOnboard from '../../hooks/usePlannerOnboard'

export default function OnboardForm() {
  const { register, handleSubmit, watch, setValue } = useForm({
    defaultValues: {
      customerName: '',
      address: '',
      neighborhood: '',
      plan: '',
      connectionType: 'WIRED',
      fdhId: '',
      splitterId: '',
      assignedPort: '',
      ontAssetId: '',
      routerAssetId: '',
      technicianId: '',
      scheduledDate: ''
    }
  })

  const [step, setStep] = useState(0)
  const [openAssetModal, setOpenAssetModal] = useState({ open: false, field: null, type: null })
  const mutation = usePlannerOnboard()

  const openPicker = (field, type) => setOpenAssetModal({ open: true, field, type })

  // Build payload and omit empty/default values.
  const preparePayload = (data) => {
    const p = {}

    // backend accepts "customerName" alias for "name"
    if (data.customerName && data.customerName.trim() !== '') p.name = data.customerName.trim()

    if (data.address && data.address.trim() !== '') p.address = data.address.trim()
    if (data.neighborhood && data.neighborhood.trim() !== '') p.neighborhood = data.neighborhood.trim()
    if (data.plan && data.plan.trim() !== '') p.plan = data.plan.trim()
    if (data.connectionType && data.connectionType.trim() !== '') p.connectionType = data.connectionType.trim()

    if (data.fdhId !== undefined && data.fdhId !== null && data.fdhId !== '') p.fdhId = Number(data.fdhId)
    if (data.splitterId !== undefined && data.splitterId !== null && data.splitterId !== '') p.splitterId = Number(data.splitterId)

    if (data.assignedPort !== undefined && data.assignedPort !== null && data.assignedPort !== '') {
      const portNum = Number(data.assignedPort)
      if (!Number.isNaN(portNum)) p.assignedPort = portNum
    }

    // Include asset ids only when explicitly selected (non-empty)
    if (data.ontAssetId !== undefined && data.ontAssetId !== null && data.ontAssetId !== '') {
      const val = Number(data.ontAssetId)
      if (!Number.isNaN(val)) p.ontAssetId = val
    }
    if (data.routerAssetId !== undefined && data.routerAssetId !== null && data.routerAssetId !== '') {
      const val = Number(data.routerAssetId)
      if (!Number.isNaN(val)) p.routerAssetId = val
    }

    if (data.technicianId !== undefined && data.technicianId !== null && data.technicianId !== '') {
      const t = Number(data.technicianId)
      if (!Number.isNaN(t)) p.technicianId = t
    }

    // scheduledDate: send only if set. Backend expects ISO-like string for LocalDateTime.
    if (data.scheduledDate && data.scheduledDate !== '') {
      // data.scheduledDate from <input type="datetime-local"> is e.g. "2025-11-13T15:06"
      // Backend LocalDateTime binding should accept this; if not convert to full ISO as needed.
      p.scheduledDate = data.scheduledDate
    }

    return p
  }

  const onSubmit = (data) => {
    const payload = preparePayload(data)
    mutation.mutate(payload)
  }

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {step === 0 && (
        <div>
          <h3>Customer Info</h3>
          <input placeholder="Name" {...register('customerName', { required: true })} />
          <input placeholder="Address" {...register('address')} />
          <input placeholder="Neighborhood" {...register('neighborhood')} />
          <input placeholder="Plan" {...register('plan')} />
          <button type="button" onClick={() => setStep(1)}>Next</button>
        </div>
      )}

      {step === 1 && (
        <div>
          <h3>Network Assignment</h3>

          <FDHSelector
            value={watch('fdhId')}
            onChange={(e) => setValue('fdhId', e.target.value ? Number(e.target.value) : '')}
          />

          <SplitterSelector
            fdhId={watch('fdhId')}
            value={watch('splitterId')}
            onChange={(val) => setValue('splitterId', val)}
            onAssignedPortSuggested={(port) => {
              const current = watch('assignedPort')
              if ((!current || current === '') && port) setValue('assignedPort', port)
            }}
          />

          <input placeholder="Assigned Port" {...register('assignedPort')} readOnly />

          <button type="button" onClick={() => setStep(0)}>Back</button>
          <button type="button" onClick={() => setStep(2)}>Next</button>
        </div>
      )}

      {step === 2 && (
        <div>
          <h3>Assets & Schedule</h3>

          <div>
            <label>ONT</label>
            <button type="button" onClick={() => openPicker('ontAssetId', 'ONT')}>Pick ONT</button>
            <input readOnly {...register('ontAssetId')} />
          </div>

          <div>
            <label>Router</label>
            <button type="button" onClick={() => openPicker('routerAssetId', 'ROUTER')}>Pick Router</button>
            <input readOnly {...register('routerAssetId')} />
          </div>

          <div>
            <label>Schedule</label>
            <input type="datetime-local" {...register('scheduledDate')} />
          </div>

          <button type="button" onClick={() => setStep(1)}>Back</button>
          <button type="submit">Onboard</button>
        </div>
      )}

      {mutation.isLoading && <p>Submitting...</p>}
      {mutation.isError && <p className="error">{mutation.error?.message}</p>}
      {mutation.isSuccess && <p className="success">{mutation.data?.message}</p>}

      <AssetPickerModal
        isOpen={openAssetModal.open}
        assetType={openAssetModal.type}
        onClose={() => setOpenAssetModal({ open: false, field: null, type: null })}
        onSelect={(asset) => {
          // keep ID consistent with backend expectations (assetId or id)
          const id = asset.assetId ?? asset.id
          setValue(openAssetModal.field, id ?? '')
          setOpenAssetModal({ open: false, field: null, type: null })
        }}
      />
    </form>
  )
}
