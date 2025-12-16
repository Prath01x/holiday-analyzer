import { useState } from 'react';
import './App.css';
import Dashboard from './components/Dashboard';
import AdminPanel from './components/AdminPanel';

function App() {
  const [currentView, setCurrentView] = useState<'dashboard' | 'admin'>('dashboard');

  return (
    <div className="app">
      {currentView === 'dashboard' ? (
        <Dashboard />
      ) : (
        <AdminPanel onBack={() => setCurrentView('dashboard')} />
      )}
      
      <footer className="app-footer">
        <button 
          onClick={() => setCurrentView(currentView === 'dashboard' ? 'admin' : 'dashboard')}
          className="footer-link"
        >
          {currentView === 'dashboard' ? 'Admin-Bereich' : 'Zurück zum Dashboard'}
        </button>
      </footer>
    </div>
  );
}

export default App;
