import { useEffect, useState } from 'react'
import { getAccounts, deleteAccount } from '../api/client'

export default function Accounts() {
  const [accounts, setAccounts] = useState([])
  const [error, setError] = useState('')

  const load = async () => {
    try {
      const res = await getAccounts()
      setAccounts(res.data)
    } catch {
      setError('Failed to load accounts')
    }
  }

  useEffect(() => { load() }, [])

  const handleDelete = async (id) => {
    if (!confirm('Delete this account?')) return
    try {
      await deleteAccount(id)
      setAccounts(a => a.filter(x => x.id !== id))
    } catch {
      setError('Failed to delete account')
    }
  }

  return (
    <div className="page">
      <h2>Accounts</h2>
      {error && <p className="error">{error}</p>}
      <div className="grid">
        {accounts.map(a => (
          <div className="card" key={a.id}>
            <h3>{a.firstName} {a.lastName}</h3>
            <p>{a.email}</p>
            <p>{a.phoneNumber}</p>
            <p style={{ fontSize: '0.8rem', color: '#888' }}>{a.about}</p>
            <div className="actions">
              <button className="btn danger sm" onClick={() => handleDelete(a.id)}>Delete</button>
            </div>
          </div>
        ))}
        {accounts.length === 0 && <p style={{ color: '#888' }}>No accounts found.</p>}
      </div>
    </div>
  )
}
