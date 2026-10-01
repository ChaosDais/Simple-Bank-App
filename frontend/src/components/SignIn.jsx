import { useEffect, useState } from 'react'

function SignIn({ onSignIn }) {
  const [mode, setMode] = useState('signin')
  const [name, setName] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [asAdmin, setAsAdmin] = useState(false)
  const [adminSecret, setAdminSecret] = useState('')
  const [adminExists, setAdminExists] = useState(true)
  const [status, setStatus] = useState('idle')
  const [message, setMessage] = useState('')

  const isSignUp = mode === 'signup'

  useEffect(() => {
    let cancelled = false

    fetch('/api/users/admin-exists')
      .then((res) => (res.ok ? res.json() : true))
      .then((exists) => {
        if (!cancelled) setAdminExists(exists)
      })
      .catch(() => {
        if (!cancelled) setAdminExists(true)
      })

    return () => {
      cancelled = true
    }
  }, [])

  const switchMode = (nextMode) => {
    setMode(nextMode)
    setStatus('idle')
    setMessage('')
    setAsAdmin(false)
    setAdminSecret('')
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setStatus('loading')
    setMessage('')

    const signUpAsAdmin = isSignUp && asAdmin
    const url = signUpAsAdmin
      ? '/api/users/signup-admin'
      : isSignUp
        ? '/api/users/signup'
        : '/api/users/signin'
    const body = isSignUp
      ? signUpAsAdmin
        ? { name, email, password, secret: adminSecret }
        : { name, email, password }
      : { email, password }

    try {
      const res = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
      })

      if (!res.ok) {
        if (isSignUp && res.status === 409) {
          throw new Error('An account with that email already exists.')
        }
        if (signUpAsAdmin && res.status === 403) {
          throw new Error('Invalid admin setup key.')
        }
        if (!isSignUp && res.status === 401) {
          throw new Error('Invalid email or password.')
        }
        throw new Error(`Request failed: ${res.status}`)
      }

      const user = await res.json()
      setStatus('success')
      if (isSignUp) {
        setMessage(`Account created. Welcome, ${user.name}! You can now sign in.`)
        if (signUpAsAdmin) setAdminExists(true)
      } else {
        setMessage(`Welcome back, ${user.name}!`)
        onSignIn(user)
      }
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

        {isSignUp && !adminExists && (
          <label className="signin-admin-toggle">
            <input
              type="checkbox"
              checked={asAdmin}
              onChange={(e) => setAsAdmin(e.target.checked)}
            />
            Sign up as admin
          </label>
        )}

        {isSignUp && !adminExists && asAdmin && (
          <>
            <label htmlFor="adminSecret">Admin setup key</label>
            <input
              id="adminSecret"
              type="password"
              value={adminSecret}
              onChange={(e) => setAdminSecret(e.target.value)}
              placeholder="Provided by the app owner"
              required
            />
          </>
        )}

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

