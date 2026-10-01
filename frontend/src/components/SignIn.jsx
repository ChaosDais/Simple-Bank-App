import { useState } from 'react'

function SignIn() {
  const [mode, setMode] = useState('signin')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [status, setStatus] = useState('idle')
  const [message, setMessage] = useState('')

  const isSignUp = mode === 'signup'

  const switchMode = (nextMode) => {
    setMode(nextMode)
    setStatus('idle')
    setMessage('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setStatus('loading')
    setMessage('')

    try {
      const res = await fetch(isSignUp ? '/api/users/signup' : '/api/users/signin', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(isSignUp ? { name, email, password } : { email, password }),
      })

      if (!res.ok) {
        if (isSignUp && res.status === 409) {
          throw new Error('An account with that email already exists.')
        }
        if (!isSignUp && res.status === 401) {
          throw new Error('Invalid email or password.')
        }
        throw new Error(`Request failed: ${res.status}`)
      }

      const user = await res.json()
      setStatus('success')
      setMessage(isSignUp ? `Account created. Welcome, ${user.name}!` : `Welcome back, ${user.name}!`)
    } catch (err) {
      setStatus('error')
      setMessage(err.message)
    }
  }

  return (
    <div className="signin-card">
      <h1>{isSignUp ? 'Sign up' : 'Sign in'}</h1>
      <p>Access your BankApp account</p>
      <form onSubmit={handleSubmit}>
        {isSignUp && (
          <>
            <label htmlFor="name">Name</label>
            <input
              id="name"
              type="text"
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Your Name"
              required
            />
          </>
        )}

        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
          required
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="••••••••"
          required
        />

        <button type="submit" className="signin-button" disabled={status === 'loading'}>
          {isSignUp ? 'Sign up' : 'Sign in'}
        </button>
      </form>

      {message && <p className="signin-status" data-status={status}>{message}</p>}

      <p className="signin-toggle">
        {isSignUp ? 'Already have an account?' : "Don't have an account?"}{' '}
        <button type="button" onClick={() => switchMode(isSignUp ? 'signin' : 'signup')}>
          {isSignUp ? 'Sign in' : 'Sign up'}
        </button>
      </p>
    </div>
  )
}

export default SignIn

