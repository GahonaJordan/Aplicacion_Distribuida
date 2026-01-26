import './App.css'
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { ProtectedRoute } from './components/ProtectedRoute';
import Layout from './components/Layout';
import HomePage from './pages/HomePage';
import ProductosPage from './pages/ProductosPage';
import ProveedoresPage from './pages/ProveedoresPage';
import BodegasPage from './pages/BodegasPage';
import OrdenesPage from './pages/OrdenesPage';
import InventarioPage from './pages/InventarioPage';
import UsersManagementPage from './pages/UsersManagementPage';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <ProtectedRoute>
          <Layout>
            <Routes>
              <Route path="/" element={<HomePage />} />
              <Route path="/productos" element={<ProductosPage />} />
              <Route path="/proveedores" element={<ProveedoresPage />} />
              <Route path="/bodegas" element={<BodegasPage />} />
              <Route path="/ordenes" element={<OrdenesPage />} />
              <Route path="/inventario" element={<InventarioPage />} />
              <Route path="/usuarios" element={<UsersManagementPage />} />
            </Routes>
          </Layout>
        </ProtectedRoute>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App
