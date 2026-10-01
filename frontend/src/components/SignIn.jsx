import { useState } from 'react'

// Sign-in UI only; no authentication is wired up yet.
function SignIn() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  return (
    <div className="signin-card">
      <h1>Sign in</h1>
      <p>Access your BankApp account</p>
      <form onSubmit={(e) => e.preventDefault()}>
        <label htmlFor="email">Email</label>
        <input
          id="email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder="you@example.com"
        />

        <label htmlFor="password">Password</label>
        <input
          id="password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="••••••••"
        />

        <button type="submit" className="signin-button">
          Sign in
        </button>
      </form>
    </div>
  )
}

export default SignIn
