import { useState } from 'react'
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query'
import { getAccounts, updateAccount, deleteAccount } from '../api/client'

export default function Accounts() {
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
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.Massege || 'Update failed')
    },
  })

  const deleteMutation = useMutation({
    mutationFn: deleteAccount,
    onSuccess: () => {
      queryClient.invalidateQueries(['accounts'])
      setSuccess('Account deleted successfully')
      setTimeout(() => setSuccess(''), 3000)
    },
    onError: () => setError('Failed to delete account'),
  })

  const handleEdit = (account) => {
    setEditingId(account.id)
    setEditForm({
      firstName: account.firstName,
      lastName: account.lastName,
      email: account.email,
      phoneNumber: account.phoneNumber,
      about: account.about || '',
      dateOfBirth: account.dateOfBirth,
    })
    setError('')
  }

  const handleUpdate = (id) => {
    updateMutation.mutate({ id, data: editForm })
  }

  const handleDelete = (id) => {
    if (!confirm('Delete this account?')) return
    deleteMutation.mutate(id)
  }

  if (isLoading) return <div className="page">Loading...</div>

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
                />
                <input
                  placeholder="Last Name"
                  value={editForm.lastName}
                  onChange={(e) => setEditForm({ ...editForm, lastName: e.target.value })}
                />
                <input
                  type="email"
                  placeholder="Email"
                  value={editForm.email}
                  onChange={(e) => setEditForm({ ...editForm, email: e.target.value })}
                />
                <input
                  placeholder="Phone"
                  value={editForm.phoneNumber}
                  onChange={(e) => setEditForm({ ...editForm, phoneNumber: e.target.value })}
                />
                <div className="actions">
                  <button className="btn sm" onClick={() => handleUpdate(acc.id)}>Save</button>
                  <button className="btn sm" onClick={() => setEditingId(null)}>Cancel</button>
                </div>
              </div>
            ) : (
              <>
                <h3>{acc.firstName} {acc.lastName}</h3>
                {acc.admin && <span className="badge" style={{ background: '#4f46e5', color: 'white' }}>Admin</span>}
                <p><strong>Email:</strong> {acc.email}</p>
                <p><strong>Phone:</strong> {acc.phoneNumber}</p>
                <p><strong>DOB:</strong> {acc.dateOfBirth}</p>
                <div className="actions">
                  <button className="btn sm" onClick={() => handleEdit(acc)}>Edit</button>
                  <button className="btn danger sm" onClick={() => handleDelete(acc.id)}>Delete</button>
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
