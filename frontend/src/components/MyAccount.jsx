function MyAccount({ user }) {
  return (
    <section className="users-page">
      <h1>My Account</h1>
      <div className="users-list-item">
        <div className="users-list-header">
          <span className="users-list-name">{user.name}</span>
          <span className="users-list-email">{user.email}</span>
        </div>
        <div className="users-list-meta">
          <span className="dashboard-id">{user.id}</span>
          <span>{user.admin ? 'Admin' : 'Standard user'}</span>
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
      </div>
    </section>
  )
}

export default MyAccount
