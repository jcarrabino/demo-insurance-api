import { useEffect, useState } from 'react'
import { getClaims, createClaim, updateClaim, deleteClaim, getPolicies, getAccounts } from '../api/client'
import { useAuth } from '../context/AuthContext'
import { useEditedFields } from '../hooks/useEditedFields'
import { getErrorMessage } from '../utils/errorHandler'
import Spinner from '../components/Spinner'

const STATUSES = ['SUBMITTED', 'IN_PROGRESS', 'APPROVED', 'DENIED']
const empty = { description: '', claimDate: '', claimStatus: 'SUBMITTED', policyId: '' }
const emptyEdit = { id: null, policyId: null, description: '', claimDate: '', claimStatus: 'SUBMITTED' }

export default function Claims() {
  const { user } = useAuth()
  const [claims, setClaims] = useState([])
  const [policies, setPolicies] = useState([])
  const [accounts, setAccounts] = useState([])
  const [form, setForm] = useState(empty)
  const [selectedAccountId, setSelectedAccountId] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingClaim, setEditingClaim] = useState(null)
  const [editForm, setEditForm] = useState(emptyEdit)
  const [isLoading, setIsLoading] = useState(true)
  const { trackEdit, getEditedData, reset: resetEditedFields } = useEditedFields({})

  const load = async () => {
    setIsLoading(true)
    try {
      const [c, p] = await Promise.all([getClaims(), getPolicies()])
      // getClaims returns PagedResponse<T>, extract content
      const claimsData = c.data?.content || c.data || []
      // getPolicies returns List<T> directly (unwrapped by interceptor)
      const policiesData = Array.isArray(p.data) ? p.data : []
      
      setClaims(Array.isArray(claimsData) ? claimsData : [])
      setPolicies(Array.isArray(policiesData) ? policiesData : [])
      
      // If admin, load all accounts for filtering
      if (user?.admin) {
        const accountsData = await getAccounts()
        // getAccounts returns PagedResponse<T>, extract content
        const accounts = accountsData.data?.content || accountsData.data || []
        setAccounts(Array.isArray(accounts) ? accounts : [])
      }
    } catch (err) { 
      console.error('Failed to load data:', err)
      setError('Failed to load data')
      // Ensure arrays are initialized even on error
      setClaims([])
      setPolicies([])
      setAccounts([])
    } finally {
      setIsLoading(false)
    }
  }

  useEffect(() => { load() }, [user])

  const set = (f) => (e) => setForm(p => ({ ...p, [f]: e.target.value }))

  // Filter policies based on selected account (for admins)
  const filteredPolicies = user?.admin && selectedAccountId
    ? policies.filter(p => p.accountId === parseInt(selectedAccountId))
    : policies

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    const { policyId, ...claimData } = form
    try {
      // Include policyId in the claim data for validation
      const formData = { ...claimData, policyId: parseInt(policyId) }
      await createClaim(policyId, formData)
      setSuccess('Claim submitted'); setForm(empty); setSelectedAccountId(''); setShowForm(false); load()
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this claim?')) return
    try { 
      await deleteClaim(id)
      setClaims(c => c.filter(x => x.id !== id))
      setSuccess('Claim deleted')
      setTimeout(() => setSuccess(''), 3000)
    } catch { 
      setError('Failed to delete') 
    }
  }

  const startEdit = (claim) => {
    setEditingClaim(claim.id)
    setEditForm({
      id: claim.id,
      policyId: claim.policyId, // Include policyId for PUT request
      description: claim.description || '',
      claimDate: claim.claimDate || '', // Include for PUT but make non-editable
      claimStatus: claim.claimStatus || 'SUBMITTED'
    })
    resetEditedFields()
    setError('')
    setSuccess('')
  }

  const cancelEdit = () => {
    setEditingClaim(null)
    setEditForm(emptyEdit)
  }

  const setEdit = (f) => (e) => {
    trackEdit(f)
    setEditForm(p => ({ ...p, [f]: e.target.value }))
  }

  const handleUpdate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    try {
      const { id, ...updateData } = editForm
      const partialData = getEditedData(updateData)
      
      // Use PATCH for partial updates (only edited fields)
      await updateClaim(id, partialData)
      setSuccess('Claim updated')
      setEditingClaim(null)
      setEditForm(emptyEdit)
      load()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      setError(getErrorMessage(err))
    }
  }

  // Check if user can edit a claim
  const canEdit = (claim) => {
    if (user?.admin) return true
    return claim.policy?.accountId === user?.id
  }

  // Check if user can delete a claim (admin only)
  const canDelete = () => {
    return user?.admin
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Claims</h2>
        <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Claim'}</button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Claim</h3>
          <form onSubmit={handleCreate}>
            {user?.admin && (
              <>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Account (Optional Filter)</label>
                <select value={selectedAccountId} onChange={(e) => setSelectedAccountId(e.target.value)}>
                  <option value="">All Accounts</option>
                  {accounts.map(a => (
                    <option key={a.id} value={a.id}>
                      {a.firstName} {a.lastName} ({a.email})
                    </option>
                  ))}
                </select>
              </>
            )}
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Policy</label>
            <select value={form.policyId} onChange={set('policyId')} required>
              <option value="">Select Policy</option>
              {filteredPolicies.map(p => (
                <option key={p.id} value={p.id}>
                  POL-{String(p.id).padStart(5, '0')} - Line {p.lineId} - ${p.premium}
                  {user?.admin && p.account && ` (${p.account.firstName} ${p.account.lastName})`}
                </option>
              ))}
            </select>
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Description</label>
            <textarea 
              placeholder="Describe the claim" 
              value={form.description} 
              onChange={set('description')} 
              rows="3"
              required 
            />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Claim Date</label>
            <input type="date" value={form.claimDate} onChange={set('claimDate')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Status</label>
            <select value={form.claimStatus} onChange={set('claimStatus')}>
              {STATUSES.map(s => <option key={s} value={s}>{s.replace('_', ' ')}</option>)}
            </select>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Submit Claim</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      {success && !showForm && <p className="success">{success}</p>}
      
      {isLoading ? (
        <Spinner message="Loading claims..." />
      ) : (
        <div className="grid">
          {claims.map(c => (
          <div className="card" key={c.id}>
            {editingClaim === c.id ? (
              <form onSubmit={handleUpdate}>
                <h3>Edit Claim</h3>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Claim Number (Read-only)</label>
                <input 
                  value={`CLM-${String(c.id).padStart(5, '0')}`}
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Policy (Read-only)</label>
                <input 
                  value={`POL-${String(c.policy?.id).padStart(5, '0')} - Line ${c.policy?.lineId || 'N/A'} - ${c.policy?.premium || 0}`}
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Claim Date (Read-only)</label>
                <input 
                  type="date" 
                  value={c.claimDate} 
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Description</label>
                <textarea 
                  value={editForm.description} 
                  onChange={setEdit('description')} 
                  rows="3"
                  required 
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Status</label>
                <select value={editForm.claimStatus} onChange={setEdit('claimStatus')}>
                  {STATUSES.map(s => <option key={s} value={s}>{s.replace('_', ' ')}</option>)}
                </select>
                <div className="actions">
                  <button className="btn sm" type="submit">Save</button>
                  <button className="btn sm" type="button" onClick={cancelEdit}>Cancel</button>
                </div>
              </form>
            ) : (
              <>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                  <h3>CLM-{String(c.id).padStart(5, '0')}</h3>
                  <span className={`badge ${c.claimStatus}`}>{c.claimStatus?.replace('_', ' ')}</span>
                </div>
                {user?.admin && c.policy?.account && (
                  <p style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.5rem' }}>
                    <strong>Account:</strong> {c.policy.account.firstName} {c.policy.account.lastName}
                  </p>
                )}
                {c.policy && (
                  <p style={{ fontSize: '0.85rem', color: '#666' }}>
                    <strong>Policy:</strong> Line {c.policy.lineId}
                  </p>
                )}
                <p>{c.description}</p>
                <p style={{ fontSize: '0.8rem', color: '#888' }}>{c.claimDate}</p>
                <div className="actions">
                  {canEdit(c) && (
                    <button className="btn sm" onClick={() => startEdit(c)}>Edit</button>
                  )}
                  {canDelete() && (
                    <button className="btn danger sm" onClick={() => handleDelete(c.id)}>Delete</button>
                  )}
                </div>
              </>
            )}
          </div>
        ))}
        {claims.length === 0 && <p style={{ color: '#888' }}>No claims found.</p>}
      </div>
      )}
    </div>
  )
}
