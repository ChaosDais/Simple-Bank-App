import { useEffect, useState } from 'react'

function UsersList() {
  const [users, setUsers] = useState([])
  const [status, setStatus] = useState('loading')

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

  if (status === 'loading') {
    return <p className="users-status">Loading users...</p>
  }
  if (status === 'error') {
    return <p className="users-status">Could not load users. Is the backend running?</p>
  }
  if (users.length === 0) {
    return <p className="users-status">No users found.</p>
  }

  return (
    <ul className="users-list">
      {users.map((user) => (
        <li key={user.id} className="users-list-item">
          <span className="users-list-name">{user.name}</span>
          <span className="users-list-id">{user.id}</span>
        </li>
      ))}
    </ul>
  )
}

export default UsersList
