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
          <div className="users-list-header">
            <span className="users-list-name">{user.name}</span>
            <span className="users-list-email">{user.email}</span>
          </div>
          {user.accounts && user.accounts.length > 0 ? (
            <ul className="users-list-accounts">
              {user.accounts.map((account) => (
                <li key={account.id}>
                  <span className="account-type">{account.accountType}</span>
                  <span className="account-balance">${account.balance.toFixed(2)}</span>
                </li>
              ))}
            </ul>
          ) : (
            <p className="users-list-no-accounts">No accounts</p>
          )}
        </li>
      ))}
    </ul>
  )
}

export default UsersList
