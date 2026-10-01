import { useState } from 'react'
import Navbar from './components/Navbar'
import SignIn from './components/SignIn'
import MyAccount from './components/MyAccount'
import AdminDashboard from './components/AdminDashboard'
import './App.css'

function App() {
  const [currentUser, setCurrentUser] = useState(null)
  const [page, setPage] = useState('home')

  const handleSignIn = (user) => {
    setCurrentUser(user)
    setPage(user.admin ? 'dashboard' : 'account')
  }

  const handleSignOut = () => {
    setCurrentUser(null)
    setPage('home')
  }

  return (
    <>
      <Navbar page={page} onNavigate={setPage} currentUser={currentUser} onSignOut={handleSignOut} />
      <main className="page">
        {page === 'home' && !currentUser && <SignIn onSignIn={handleSignIn} />}
        {page === 'account' && currentUser && !currentUser.admin && <MyAccount user={currentUser} />}
        {page === 'dashboard' && currentUser?.admin && <AdminDashboard />}
      </main>
    </>
  )
}

export default App
