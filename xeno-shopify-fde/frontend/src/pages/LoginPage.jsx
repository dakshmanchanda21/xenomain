import { useState } from 'react';
import api from '../apiClient';
import { Box, Button, TextField, Typography, Paper } from '@mui/material';

export default function LoginPage() {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [error, setError] = useState('');

  const handleChange = (field) => (e) =>
    setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const url = mode === 'login' ? '/api/auth/login' : '/api/auth/register';
      const { data } = await api.post(url, form);
      localStorage.setItem('token', data.token);
      localStorage.setItem('tenantId', data.tenantId);
      localStorage.setItem('email', data.email);
      window.location.href = '/dashboard';
    } catch (err) {
      setError('Invalid credentials or server error');
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        background: 'radial-gradient(circle at top, #1d4ed8 0, #020617 60%)',
      }}
    >
      <Paper
        elevation={12}
        sx={{
          p: 4,
          width: 380,
          borderRadius: 4,
          bgcolor: 'rgba(15,23,42,0.9)',
          backdropFilter: 'blur(20px)',
          color: '#e5e7eb',
        }}
      >
        <Typography variant="h5" sx={{ mb: 1, fontWeight: 700 }}>
          Xeno Shopify Insights
        </Typography>
        <Typography sx={{ mb: 3, opacity: 0.7 }}>
          {mode === 'login' ? 'Welcome back 👋' : 'Create your tenant account'}
        </Typography>

        <form onSubmit={handleSubmit}>
          {mode === 'register' && (
            <TextField
              label="Name"
              fullWidth
              margin="normal"
              variant="outlined"
              value={form.name}
              onChange={handleChange('name')}
            />
          )}
          <TextField
            label="Email"
            fullWidth
            margin="normal"
            variant="outlined"
            value={form.email}
            onChange={handleChange('email')}
          />
          <TextField
            label="Password"
            fullWidth
            margin="normal"
            variant="outlined"
            type="password"
            value={form.password}
            onChange={handleChange('password')}
          />

          {error && (
            <Typography color="error" sx={{ mt: 1 }}>
              {error}
            </Typography>
          )}

          <Button
            fullWidth
            type="submit"
            variant="contained"
            sx={{
              mt: 2,
              borderRadius: 999,
              textTransform: 'none',
              fontWeight: 600,
            }}
          >
            {mode === 'login' ? 'Log in' : 'Sign up'}
          </Button>

          <Button
            fullWidth
            variant="text"
            sx={{ mt: 1, textTransform: 'none', color: '#9ca3af' }}
            onClick={() => {
              setMode((m) => (m === 'login' ? 'register' : 'login'));
              setError('');
            }}
          >
            {mode === 'login'
              ? "Don't have an account? Sign up"
              : 'Already have an account? Log in'}
          </Button>
        </form>
      </Paper>
    </Box>
  );
}
