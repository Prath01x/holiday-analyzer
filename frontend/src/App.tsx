import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import './App.css';
import Dashboard from './components/Dashboard';
import AdminPanel from './components/AdminPanel';
import Login from './components/Login';
import ProtectedRoute from './components/ProtectedRoute';
import { AuthProvider, useAuth } from './context/AuthContext';

function AppContent() {
  const { login, logout, isAuthenticated } = useAuth();

  return (
    <div className="app">
      <Routes>
        <Route path="/" element={<Dashboard />} />
        <Route 
          path="/login" 
          element={
            isAuthenticated ? (
              <Navigate to="/admin" replace />
            ) : (
              <Login onLoginSuccess={login} />
            )
          } 
        />
        <Route
          path="/admin"
          element={
            <ProtectedRoute>
              <AdminPanel onBack={() => window.location.href = '/'} />
            </ProtectedRoute>
          }
        />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
      
      {!window.location.pathname.includes('/login') && (
        <footer className="app-footer">
          <button 
            onClick={() => {
              if (window.location.pathname === '/admin') {
                logout();
                window.location.href = '/';
              } else {
                window.location.href = isAuthenticated ? '/admin' : '/login';
              }
            }}
            className="footer-link"
          >
            {window.location.pathname === '/admin' ? 'Logout' : 'Admin-Bereich'}
          </button>
        </footer>
      )}
    </div>
  );
}

function App() {
  return (
    <Router>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </Router>
  );
}

export default App;
