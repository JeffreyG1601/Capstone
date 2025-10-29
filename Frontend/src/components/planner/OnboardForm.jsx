// src/components/planner/OnboardForm.jsx
import React, { useState } from 'react'
import { useForm } from 'react-hook-form'
import FDHSelector from './FDHSelector'
import AssetPickerModal from './AssetPickerModal'
import usePlannerOnboard from '../../hooks/usePlannerOnboard'

export default function OnboardForm() {
  const { register, handleSubmit, watch, setValue } = useForm({
    defaultValues: {
      customerName: '',
      address: '',
      neighborhood: '',
      plan: '',
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

  const onSubmit = data => {
    mutation.mutate(data)
  }

  const openPicker = (field, type) => setOpenAssetModal({ open: true, field, type })

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      {step === 0 && (
        <div>
          <h3>Customer</h3>
          <input placeholder="Name" {...register('customerName', { required: true })} />
          <input placeholder="Address" {...register('address')} />
          <input placeholder="Neighborhood" {...register('neighborhood')} />
          <input placeholder="Plan" {...register('plan')} />
          <button type="button" onClick={() => setStep(1)}>Next</button>
        </div>
      )}

      {step === 1 && (
        <div>
          <h3>Network assignment</h3>
          <FDHSelector value={watch('fdhId')} onChange={(e) => setValue('fdhId', e.target.value)} />
          <input placeholder="Splitter ID" {...register('splitterId')} />
          <input placeholder="Assigned Port" {...register('assignedPort')} />
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
      {mutation.isSuccess && <p className="success">{mutation.data?.message || 'Onboarded'}</p>}

      <AssetPickerModal
        isOpen={openAssetModal.open}
        assetType={openAssetModal.type}
        onClose={() => setOpenAssetModal({ open: false, field: null, type: null })}
        onSelect={(asset) => setValue(openAssetModal.field, asset.assetId ?? asset.id)}
      />
    </form>
  )
}
