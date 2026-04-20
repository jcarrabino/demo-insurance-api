import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getAccounts, updateAccount, deleteAccount } from '../api/client'
import { useAuth } from '../context/AuthContext'
import { useEditedFields } from '../hooks/useEditedFields'
import { getErrorMessage } from '../utils/errorHandler'
import Spinner from '../components/Spinner'

export default function Accounts() {
  const { user } = useAuth()
  const queryClient = useQueryClient()
  const [editingId, setEditingId] = useState(null)
  const [editForm, setEditForm] = useState({})
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const { trackEdit, getEditedData, reset: resetEditedFields } = useEditedFields({})

  const { data: accounts, isLoading } = useQuery({
    queryKey: ['accounts'],
    queryFn: async () => {
      const res = await getAccounts()
      // Response is now wrapped in PagedResponse<T>
      // res.data is PagedResponse with content, page, size, totalElements, totalPages, hasNext, hasPrevious
      return res.data?.content || res.data || []
    },
  })

  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => updateAccount(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries(['accounts'])
      setSuccess('Account updated successfully')
      setEditingId(null)
      setTimeout(() => setSuccess(''), 3000)
    },
    onError: (err) => {
      const data = err.response?.data
      setError(getErrorMessage(err))
    },
  })

  const deleteMutation = useMutation({
    mutationFn: deleteAccount,
    onSuccess: () => {
      queryClient.invalidateQueries(['accounts'])
      setSuccess('Account deleted successfully')
      setTimeout(() => setSuccess(''), 3000)
    },
    onError: (err) => {
      const data = err.response?.data
      setError(getErrorMessage(err))
    },
  })

  const handleEdit = (account) => {
    setEditingId(account.id)
    setEditForm({
      firstName: account.firstName || '',
      middleName: account.middleName || '',
      lastName: account.lastName || '',
      phoneNumber: account.phoneNumber || '',
      dateOfBirth: account.dateOfBirth || '',
      password: '', // New password (optional)
      email: '',
      address: {
        street: account.address?.street || '',
        city: account.address?.city || '',
        state: account.address?.state || '',
        zipCode: account.address?.zipCode || '',
        country: account.address?.country || ''
      },
    })
    resetEditedFields()
    setError('')
  }

  const handleUpdate = (acc) => {
    const {id} = acc;
    const updateData = getEditedData(editForm)
    
    // Only include password if it was edited and not empty
    if (editForm.password && editForm.password.trim() !== '') {
      updateData.password = editForm.password
    }

    console.log('Sending only edited fields:', updateData)
    
    updateMutation.mutate({ id, data: updateData })
  }

  const handleDelete = (id) => {
    // Prevent deleting own account
    if (id === user?.id) {
      setError('Cannot delete your own account')
      setTimeout(() => setError(''), 3000)
      return
    }
    
    if (!confirm('Delete this account? This will also delete all associated policies and claims.')) return
    deleteMutation.mutate(id)
  }

  if (isLoading) return (
    <div className="page">
      <h2>Accounts Management</h2>
      <Spinner message="Loading accounts..." />
    </div>
  )

  return (
    <div className="page">
      <h2>Accounts Management</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">{success}</p>}

      <div className="grid">
        {accounts?.map(acc => (
          <div className="card" key={acc.id}>
            {editingId === acc.id ? (
              <div style={{ display: 'flex', flexDirection: 'column', gap: '0.75rem' }}>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Email (Read-only)</label>
                <input
                  type="email"
                  value={acc.email}
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>First Name *</label>
                <input
                  placeholder="First Name"
                  value={editForm.firstName}
                  onChange={(e) => {
                    trackEdit('firstName')
                    setEditForm({ ...editForm, firstName: e.target.value })
                  }}
                  minLength="2"
                  maxLength="50"
                  required
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Middle Name</label>
                <input
                  placeholder="Middle Name"
                  value={editForm.middleName}
                  onChange={(e) => {
                    trackEdit('middleName')
                    setEditForm({ ...editForm, middleName: e.target.value })
                  }}
                  maxLength="50"
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Last Name *</label>
                <input
                  placeholder="Last Name"
                  value={editForm.lastName}
                  onChange={(e) => {
                    trackEdit('lastName')
                    setEditForm({ ...editForm, lastName: e.target.value })
                  }}
                  minLength="2"
                  maxLength="50"
                  required
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Phone (10 digits)</label>
                <input
                  type="tel"
                  placeholder="Phone (10 digits)"
                  value={editForm.phoneNumber || ''}
                  onChange={(e) => {
                    trackEdit('phoneNumber')
                    setEditForm({ ...editForm, phoneNumber: e.target.value })
                  }}
                  pattern="[0-9]{10}"
                  maxLength="10"
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Date of Birth</label>
                <input
                  type="date"
                  placeholder="Date of Birth"
                  value={editForm.dateOfBirth || ''}
                  onChange={(e) => {
                    trackEdit('dateOfBirth')
                    setEditForm({ ...editForm, dateOfBirth: e.target.value })
                  }}
                  max={new Date().toISOString().split('T')[0]}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>New Password (optional)</label>
                <input
                  type="password"
                  placeholder="Leave blank to keep current password"
                  value={editForm.password || ''}
                  onChange={(e) => {
                    trackEdit('password')
                    setEditForm({ ...editForm, password: e.target.value })
                  }}
                  minLength="8"
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Street</label>
                <input
                  placeholder="Street"
                  value={editForm.address?.street || ''}
                  onChange={(e) => {
                    trackEdit('address')
                    setEditForm({ ...editForm, address: { ...editForm.address, street: e.target.value } })
                  }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>City</label>
                <input
                  placeholder="City"
                  value={editForm.address?.city || ''}
                  onChange={(e) => {
                    trackEdit('address')
                    setEditForm({ ...editForm, address: { ...editForm.address, city: e.target.value } })
                  }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>State</label>
                <input
                  placeholder="State"
                  value={editForm.address?.state || ''}
                  onChange={(e) => {
                    trackEdit('address')
                    setEditForm({ ...editForm, address: { ...editForm.address, state: e.target.value } })
                  }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Zip Code</label>
                <input
                  placeholder="Zip Code"
                  value={editForm.address?.zipCode || ''}
                  onChange={(e) => {
                    trackEdit('address')
                    setEditForm({ ...editForm, address: { ...editForm.address, zipCode: e.target.value } })
                  }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Country</label>
                <input
                  placeholder="Country"
                  value={editForm.address?.country || ''}
                  onChange={(e) => {
                    trackEdit('address')
                    setEditForm({ ...editForm, address: { ...editForm.address, country: e.target.value } })
                  }}
                />
                <div className="actions">
                  <button className="btn sm" onClick={() => handleUpdate(acc)}>Save</button>
                  <button className="btn sm" onClick={() => setEditingId(null)}>Cancel</button>
                </div>
              </div>
            ) : (
              <>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <h3>{acc.firstName} {acc.lastName}</h3>
                  {acc.admin && <span className="badge" style={{ background: '#4f46e5', color: 'white' }}>Admin</span>}
                </div>
                <p><strong>Email:</strong> {acc.email}</p>
                {acc.phoneNumber && <p><strong>Phone:</strong> {acc.phoneNumber}</p>}
                {acc.dateOfBirth && <p><strong>DOB:</strong> {acc.dateOfBirth}</p>}
                {acc.address?.zipCode && <p><strong>Zip:</strong> {acc.address.zipCode}</p>}
                <div className="actions">
                  <button className="btn sm" onClick={() => handleEdit(acc)}>Edit</button>
                  {acc.id !== user?.id && (
                    <button className="btn danger sm" onClick={() => handleDelete(acc.id)}>Delete</button>
                  )}
                </div>
              </>
            )}
          </div>
        ))}
        {accounts?.length === 0 && <p style={{ color: '#888' }}>No accounts found.</p>}
      </div>
    </div>
  )
}
