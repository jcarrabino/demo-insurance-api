import { useEffect, useState } from 'react'
import { getLines, createLine, updateLine, deleteLine } from '../api/client'
import { useAuth } from '../context/AuthContext'
import { useEditedFields } from '../hooks/useEditedFields'
import Spinner from '../components/Spinner'

const empty = { name: '', description: '', maxCoverage: '', minCoverage: '' }
const emptyEdit = { id: null, name: '', description: '', maxCoverage: '', minCoverage: '' }

export default function Lines() {
  const { user } = useAuth()
  const [lines, setLines] = useState([])
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)
  const [editingLine, setEditingLine] = useState(null)
  const [editForm, setEditForm] = useState(emptyEdit)
  const [isLoading, setIsLoading] = useState(true)
  const { trackEdit, getEditedData, reset: resetEditedFields } = useEditedFields({})

  const load = async () => {
    setIsLoading(true)
    try { 
      setLines((await getLines()).data) 
    } catch { 
      setError('Failed to load lines') 
    } finally {
      setIsLoading(false)
    }
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
    try { 
      await deleteLine(id)
      setLines(l => l.filter(x => x.id !== id))
      setSuccess('Line deleted')
      setTimeout(() => setSuccess(''), 3000)
    } catch { 
      setError('Failed to delete') 
    }
  }

  const startEdit = (line) => {
    setEditingLine(line.id)
    setEditForm({
      id: line.id,
      name: line.name || '',
      description: line.description || '',
      maxCoverage: line.maxCoverage || '',
      minCoverage: line.minCoverage || ''
    })
    resetEditedFields()
    setError('')
    setSuccess('')
  }

  const cancelEdit = () => {
    setEditingLine(null)
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
      
      await updateLine(id, partialData)
      setSuccess('Line updated')
      setEditingLine(null)
      setEditForm(emptyEdit)
      load()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || data?.Massege || 'Failed to update')
    }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Insurance Lines</h2>
        {user?.admin && (
          <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Line'}</button>
        )}
      </div>

      {showForm && user?.admin && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Line</h3>
          <form onSubmit={handleCreate}>
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Name</label>
            <input placeholder="Name" value={form.name} onChange={set('name')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Description</label>
            <input placeholder="Description" value={form.description} onChange={set('description')} />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Max Coverage</label>
            <input type="number" placeholder="Max Coverage" value={form.maxCoverage} onChange={set('maxCoverage')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Min Coverage</label>
            <input type="number" placeholder="Min Coverage" value={form.minCoverage} onChange={set('minCoverage')} required />
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Create</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      {success && !showForm && <p className="success">{success}</p>}
      
      {isLoading ? (
        <Spinner message="Loading insurance lines..." />
      ) : (
        <div className="grid">
          {lines.map(l => (
          <div className="card" key={l.id}>
            {editingLine === l.id ? (
              <form onSubmit={handleUpdate}>
                <h3>Edit Line</h3>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Name</label>
                <input value={editForm.name} onChange={setEdit('name')} required />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Description</label>
                <input value={editForm.description} onChange={setEdit('description')} />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Max Coverage</label>
                <input type="number" value={editForm.maxCoverage} onChange={setEdit('maxCoverage')} required />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Min Coverage</label>
                <input type="number" value={editForm.minCoverage} onChange={setEdit('minCoverage')} required />
                <div className="actions">
                  <button className="btn sm" type="submit">Save</button>
                  <button className="btn sm" type="button" onClick={cancelEdit}>Cancel</button>
                </div>
              </form>
            ) : (
              <>
                <h3>{l.name}</h3>
                <p>{l.description}</p>
                <p><strong>Coverage Range:</strong> ${l.minCoverage?.toLocaleString()} - ${l.maxCoverage?.toLocaleString()}</p>
                {user?.admin && (
                  <div className="actions">
                    <button className="btn sm" onClick={() => startEdit(l)}>Edit</button>
                    <button className="btn danger sm" onClick={() => handleDelete(l.id)}>Delete</button>
                  </div>
                )}
              </>
            )}
          </div>
        ))}
        {lines.length === 0 && <p style={{ color: '#888' }}>No lines found.</p>}
      </div>
      )}
    </div>
  )
}
