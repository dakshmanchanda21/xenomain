import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import ConnectShopifyPage from './pages/ConnectShopifyPage.jsx';

function App() {
  const isLoggedIn = !!localStorage.getItem('token');

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route
        path="/dashboard"
        element={isLoggedIn ? <DashboardPage /> : <Navigate to="/login" />}
      />
      <Route
        path="/connect-shopify"
        element={isLoggedIn ? <ConnectShopifyPage /> : <Navigate to="/login" />}
      />
      <Route
        path="*"
        element={<Navigate to={isLoggedIn ? '/dashboard' : '/login'} />}
      />
    </Routes>
  );
}

export default App;
