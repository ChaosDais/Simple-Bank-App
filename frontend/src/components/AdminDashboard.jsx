import { useEffect, useState } from 'react'

function formatAccounts(accounts) {
  if (!accounts || accounts.length === 0) return 'No accounts'
  return accounts.map((a) => `${a.accountType} ($${a.balance.toFixed(2)})`).join(', ')
}

function AdminDashboard() {
  const [users, setUsers] = useState([])
  const [status, setStatus] = useState('loading')
  const [search, setSearch] = useState('')
  const [confirmingId, setConfirmingId] = useState(null)
  const [deleteError, setDeleteError] = useState('')

  const [newName, setNewName] = useState('')
  const [newEmail, setNewEmail] = useState('')
  const [newPassword, setNewPassword] = useState('')
  const [addStatus, setAddStatus] = useState('idle')
  const [addMessage, setAddMessage] = useState('')

  useEffect(() => {
    let cancelled = false

    fetch('/api/users')
      .then((res) => {
        if (!res.ok) throw new Error(`Request failed: ${res.status}`)
        return res.json()
      })
      .then((data) => {
        if (!cancelled) {
          setUsers(data)
          setStatus('success')
        }
      })
      .catch(() => {
        if (!cancelled) setStatus('error')
      })

    return () => {
      cancelled = true
    }
  }, [])

  const filteredUsers = users.filter((user) => {
    const term = search.trim().toLowerCase()
    if (!term) return true
    return (
      user.name?.toLowerCase().includes(term) || user.email?.toLowerCase().includes(term)
    )
  })

  const handleDelete = async (id) => {
    setDeleteError('')
    try {
      const res = await fetch(`/api/users/${id}`, { method: 'DELETE' })
      if (!res.ok) throw new Error(`Request failed: ${res.status}`)
      setUsers((prev) => prev.filter((user) => user.id !== id))
    } catch {
      setDeleteError('Could not delete user. Please try again.')
    } finally {
      setConfirmingId(null)
    }
  }

  const handleAddUser = async (e) => {
    e.preventDefault()
    setAddStatus('loading')
    setAddMessage('')

    try {
      const res = await fetch('/api/users/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name: newName, email: newEmail, password: newPassword }),
      })

      if (!res.ok) {
        if (res.status === 409) throw new Error('An account with that email already exists.')
        throw new Error(`Request failed: ${res.status}`)
      }

      const user = await res.json()
      setUsers((prev) => [...prev, user])
      setAddStatus('success')
      setAddMessage(`${user.name} was added.`)
      setNewName('')
      setNewEmail('')
      setNewPassword('')
    } catch (err) {
      setAddStatus('error')
      setAddMessage(err.message)
    }
  }

  return (
    <section className="admin-dashboard">
      <h1>Admin Dashboard</h1>

      <div className="dashboard-toolbar">
        <input
          type="search"
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          placeholder="Search by name or email..."
        />
      </div>

      {status === 'loading' && <p className="users-status">Loading users...</p>}
      {status === 'error' && <p className="users-status">Could not load users.</p>}

      {status === 'success' && (
        <table className="dashboard-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Email</th>
              <th>ID</th>
              <th>Admin</th>
              <th>Accounts</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user) => (
              <tr key={user.id}>
                <td>{user.name}</td>
                <td>{user.email}</td>
                <td className="dashboard-id">{user.id}</td>
                <td>{user.admin ? 'Yes' : 'No'}</td>
                <td>{formatAccounts(user.accounts)}</td>
                <td className="dashboard-actions">
                  {confirmingId === user.id ? (
                    <span className="dashboard-confirm">
                      <span>Delete {user.name}?</span>
                      <button type="button" className="dashboard-delete-confirm" onClick={() => handleDelete(user.id)}>
                        Confirm
                      </button>
                      <button type="button" onClick={() => setConfirmingId(null)}>
                        Cancel
                      </button>
                    </span>
                  ) : (
                    <button type="button" className="dashboard-delete" onClick={() => setConfirmingId(user.id)}>
                      Delete
                    </button>
                  )}
                </td>
              </tr>
            ))}
            {filteredUsers.length === 0 && (
              <tr>
                <td colSpan={6}>No matching users.</td>
              </tr>
            )}
          </tbody>
        </table>
      )}

      {deleteError && (
        <p className="signin-status" data-status="error">
          {deleteError}
        </p>
      )}

      <h2>Add user</h2>
      <form className="dashboard-add-form" onSubmit={handleAddUser}>
        <input
          type="text"
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Name"
          required
        />
        <input
          type="email"
          value={newEmail}
          onChange={(e) => setNewEmail(e.target.value)}
          placeholder="Email"
          required
        />
        <input
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          placeholder="Password"
          required
        />
        <button type="submit" className="signin-button" disabled={addStatus === 'loading'}>
          Add user
        </button>
      </form>
      {addMessage && (
        <p className="signin-status" data-status={addStatus}>
          {addMessage}
        </p>
      )}
    </section>
  )
}

export default AdminDashboard
