import { useEffect, useState } from 'react'
import { getLines, createLine, deleteLine } from '../api/client'

const empty = { name: '', description: '', maxCoverage: '', minCoverage: '' }

export default function Lines() {
  const [lines, setLines] = useState([])
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = async () => {
    try { setLines((await getLines()).data) }
    catch { setError('Failed to load lines') }
  }

  useEffect(() => { load() }, [])

  const set = (f) => (e) => setForm(p => ({ ...p, [f]: e.target.value }))

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    try {
      await createLine(form)
      setSuccess('Line created'); setForm(empty); setShowForm(false); load()
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.Massege || 'Failed')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this line?')) return
    try { await deleteLine(id); setLines(l => l.filter(x => x.id !== id)) }
    catch { setError('Failed to delete') }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Insurance Lines</h2>
        <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Line'}</button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Line</h3>
          <form onSubmit={handleCreate}>
            <input placeholder="Name" value={form.name} onChange={set('name')} required />
            <input placeholder="Description" value={form.description} onChange={set('description')} />
            <input type="number" placeholder="Max Coverage" value={form.maxCoverage} onChange={set('maxCoverage')} required />
            <input type="number" placeholder="Min Coverage" value={form.minCoverage} onChange={set('minCoverage')} required />
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Create</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      <div className="grid">
        {lines.map(l => (
          <div className="card" key={l.id}>
            <h3>{l.name}</h3>
            <p>{l.description}</p>
            <p><strong>Coverage Range:</strong> ${l.minCoverage?.toLocaleString()} - ${l.maxCoverage?.toLocaleString()}</p>
            <div className="actions">
              <button className="btn danger sm" onClick={() => handleDelete(l.id)}>Delete</button>
            </div>
          </div>
        ))}
        {lines.length === 0 && <p style={{ color: '#888' }}>No lines found.</p>}
      </div>
    </div>
  )
}
