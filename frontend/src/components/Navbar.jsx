function Navbar({ page, onNavigate, currentUser, onSignOut }) {
  return (
    <nav className="navbar">
      <span className="navbar-brand">BankApp</span>
      <div className="navbar-links">
        {!currentUser && (
          <button
            type="button"
            className={`navbar-link${page === 'home' ? ' active' : ''}`}
            onClick={() => onNavigate('home')}
          >
            Sign in
          </button>
        )}
        {currentUser && !currentUser.admin && (
          <button
            type="button"
            className={`navbar-link${page === 'account' ? ' active' : ''}`}
            onClick={() => onNavigate('account')}
          >
            My Account
          </button>
        )}
        {currentUser && currentUser.admin && (
          <button
            type="button"
            className={`navbar-link${page === 'dashboard' ? ' active' : ''}`}
            onClick={() => onNavigate('dashboard')}
          >
            Dashboard
          </button>
        )}
        {currentUser && (
          <button type="button" className="navbar-link" onClick={onSignOut}>
            Sign out
          </button>
        )}
      </div>
    </nav>
  )
}

export default Navbar
