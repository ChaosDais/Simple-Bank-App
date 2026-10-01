import { useState } from 'react'
import Navbar from './components/Navbar'
import SignIn from './components/SignIn'
import UsersList from './components/UsersList'
import './App.css'

function App() {
  const [page, setPage] = useState('home')

  return (
    <>
      <Navbar page={page} onNavigate={setPage} />
      <main className="page">
        {page === 'home' && <SignIn />}
        {page === 'users' && (
          <section className="users-page">
            <h1>All Users</h1>
            <UsersList />
          </section>
        )}
      </main>
    </>
  )
}

export default App
