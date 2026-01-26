import { useEffect, useState } from 'react';
import { inventarioService } from '../services/inventarioService';
import { productoService } from '../services/productoService';

export default function InventarioPage() {
  const [items, setItems] = useState([]);
  const [productosMap, setProductosMap] = useState({});
  const [loading, setLoading] = useState(true);
  const [alert, setAlert] = useState(null);

  useEffect(() => {
    loadInventory();
  }, []);

  const loadInventory = async () => {
    try {
      setLoading(true);
      const [inventario, productos] = await Promise.all([
        inventarioService.getAll(),
        productoService.getAll()
      ]);

      const map = productos.reduce((acc, p) => {
        acc[p.id] = p.name || p.sku;
        return acc;
      }, {});

      setProductosMap(map);
      setItems(inventario);
    } catch (error) {
      setAlert({ type: 'error', message: error.response?.data?.message || error.message });
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div>
      {alert && (
        <div className={`alert alert-${alert.type}`}>
          {alert.message}
        </div>
      )}

      <div className="card">
        <div className="card-header">
          <h1 className="card-title">Inventario</h1>
        </div>

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Bodega</th>
                <th>Producto</th>
                <th>SKU</th>
                <th>Cantidad</th>
                <th>Actualizado</th>
              </tr>
            </thead>
            <tbody>
              {items.length === 0 ? (
                <tr>
                  <td colSpan="5" style={{ textAlign: 'center', padding: '2rem' }}>
                    Sin registros de inventario
                  </td>
                </tr>
              ) : (
                items.map(item => (
                  <tr key={`${item.warehouseId}-${item.productId}`}>
                    <td>{item.warehouseName || '-'}</td>
                    <td>{productosMap[item.productId] || item.productName || item.productId || '-'}</td>
                    <td>{item.sku}</td>
                    <td>{item.quantity}</td>
                    <td>{item.updatedAt ? new Date(item.updatedAt).toLocaleString() : '-'}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
