import React, { useEffect, useState } from 'react'
import { getPolicies, createPolicy, deletePolicy } from '../api/client'
import { useAuth } from '../context/AuthContext'

const empty = {
  policyNumber: '', policyType: '', coverageAmount: '', premium: '',
  startDate: '', endDate: ''
}

export default function Policies() {
  const { user } = useAuth()
  const [policies, setPolicies] = useState([])
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = async () => {
    try { setPolicies((await getPolicies()).data) }
    catch { setError('Failed to load policies') }
  }

  useEffect(() => { load() }, [])

  const set = (f) => (e) => setForm(p => ({ ...p, [f]: e.target.value }))

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    try {
      await createPolicy(user?.id ?? 1, form)
      setSuccess('Policy created'); setForm(empty); setShowForm(false); load()
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.Massege || 'Failed')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this policy?')) return
    try { await deletePolicy(id); setPolicies(p => p.filter(x => x.id !== id)) }
    catch { setError('Failed to delete') }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Policies</h2>
        <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Policy'}</button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Policy</h3>
          <form onSubmit={handleCreate}>
            <input placeholder="Policy Number" value={form.policyNumber} onChange={set('policyNumber')} required />
            <input placeholder="Policy Type" value={form.policyType} onChange={set('policyType')} required />
            <input type="number" placeholder="Coverage Amount" value={form.coverageAmount} onChange={set('coverageAmount')} required />
            <input type="number" placeholder="Premium" value={form.premium} onChange={set('premium')} required />
            <label style={{ fontSize: '0.85rem', color: '#555' }}>Start Date</label>
            <input type="date" value={form.startDate} onChange={set('startDate')} required />
            <label style={{ fontSize: '0.85rem', color: '#555' }}>End Date</label>
            <input type="date" value={form.endDate} onChange={set('endDate')} required />
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Create</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      <div className="grid">
        {policies.map(p => (
          <div className="card" key={p.id}>
            <h3>{p.policyNumber}</h3>
            <p><strong>Type:</strong> {p.policyType}</p>
            <p><strong>Premium:</strong> ${p.premium}</p>
            <p><strong>Coverage:</strong> ${p.coverageAmount}</p>
            <p><strong>Period:</strong> {p.startDate} → {p.endDate}</p>
            <div className="actions">
              <button className="btn danger sm" onClick={() => handleDelete(p.id)}>Delete</button>
            </div>
          </div>
        ))}
        {policies.length === 0 && <p style={{ color: '#888' }}>No policies found.</p>}
      </div>
    </div>
  )
}
