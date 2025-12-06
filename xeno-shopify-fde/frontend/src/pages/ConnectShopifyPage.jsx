import { useState } from 'react';
import api from '../apiClient';
import { Box, Button, TextField, Typography, Paper } from '@mui/material';

export default function ConnectShopifyPage() {
  const [form, setForm] = useState({ shopifyDomain: '', accessToken: '' });
  const [message, setMessage] = useState('');
  const tenantId = localStorage.getItem('tenantId');

  const handleChange = (field) => (e) =>
    setForm((f) => ({ ...f, [field]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await api.post('/api/tenants/connect-shopify', {
        tenantId: Number(tenantId),
        ...form,
      });
      setMessage('Shopify connected successfully ✅');
    } catch (err) {
      setMessage('Error connecting Shopify ❌');
    }
  };

  return (
    <Box sx={{ p: 4, minHeight: '100vh', bgcolor: '#020617' }}>
      <Paper sx={{ p: 3, maxWidth: 600, mx: 'auto', borderRadius: 4 }}>
        <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
          Connect your Shopify store
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            label="Shopify Domain (my-store.myshopify.com)"
            fullWidth
            margin="normal"
            value={form.shopifyDomain}
            onChange={handleChange('shopifyDomain')}
          />
          <TextField
            label="Shopify Access Token"
            fullWidth
            margin="normal"
            type="password"
            value={form.accessToken}
            onChange={handleChange('accessToken')}
          />
          <Button
            type="submit"
            variant="contained"
            sx={{ mt: 2, borderRadius: 999, textTransform: 'none' }}
          >
            Save & Test
          </Button>
        </form>
        {message && <Typography sx={{ mt: 2 }}>{message}</Typography>}
      </Paper>
    </Box>
  );
}
