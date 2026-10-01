function Navbar({ page, onNavigate }) {
  return (
    <nav className="navbar">
      <span className="navbar-brand">BankApp</span>
      <div className="navbar-links">
        <button
          type="button"
          className={`navbar-link${page === 'home' ? ' active' : ''}`}
          onClick={() => onNavigate('home')}
        >
          Home
        </button>
        <button
          type="button"
          className={`navbar-link${page === 'users' ? ' active' : ''}`}
          onClick={() => onNavigate('users')}
        >
          Users
        </button>
      </div>
    </nav>
  )
}

export default Navbar
