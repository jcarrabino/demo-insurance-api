import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getAccounts, updateAccount, deleteAccount } from '../api/client'
import { useAuth } from '../context/AuthContext'
import Spinner from '../components/Spinner'

export default function Accounts() {
  const { user } = useAuth()
  const queryClient = useQueryClient()
  const [editingId, setEditingId] = useState(null)
  const [editForm, setEditForm] = useState({})
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')

  const { data: accounts, isLoading } = useQuery({
    queryKey: ['accounts'],
    queryFn: async () => (await getAccounts()).data,
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
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || data?.Massege || 'Update failed')
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
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || 'Failed to delete account')
    },
  })

  const handleEdit = (account) => {
    setEditingId(account.id)
    setEditForm({
      firstName: account.firstName || '',
      middleName: account.middleName || '',
      lastName: account.lastName || '',
      email: account.email || '',
      phoneNumber: account.phoneNumber || '',
      dateOfBirth: account.dateOfBirth || '',
      address: account.address || {
        street: '',
        city: '',
        state: '',
        zipCode: '',
        country: ''
      },
    })
    setError('')
  }

  const handleUpdate = (id) => {
    updateMutation.mutate({ id, data: editForm })
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
                <input
                  placeholder="First Name"
                  value={editForm.firstName}
                  onChange={(e) => setEditForm({ ...editForm, firstName: e.target.value })}
                  minLength="2"
                  maxLength="50"
                  required
                />
                <input
                  placeholder="Middle Name"
                  value={editForm.middleName}
                  onChange={(e) => setEditForm({ ...editForm, middleName: e.target.value })}
                  maxLength="50"
                />
                <input
                  placeholder="Last Name"
                  value={editForm.lastName}
                  onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })}
                  minLength="2"
                  maxLength="50"
                  required
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={editForm.email}
                  onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                  required
                />
                <input
                  type="tel"
                  placeholder="Phone (10 digits)"
                  value={editForm.phoneNumber || ''}
                  onChange={(e) => setEditForm({ ...editForm, phoneNumber: e.target.value })}
                  pattern="[0-9]{10}"
                  maxLength="10"
                />
                <input
                  type="date"
                  placeholder="Date of Birth"
                  value={editForm.dateOfBirth || ''}
                  onChange={(e) => setEditForm({ ...editForm, dateOfBirth: e.target.value })}
                  max={new Date().toISOString().split('T')[0]}
                />
                <input
                  placeholder="Zip Code"
                  value={editForm.address?.zipCode || ''}
                  onChange={(e) => setEditForm({ ...editForm, address: { ...editForm.address, zipCode: e.target.value } })}
                />
                <div className="actions">
                  <button className="btn sm" onClick={() => handleUpdate(acc.id)}>Save</button>
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
