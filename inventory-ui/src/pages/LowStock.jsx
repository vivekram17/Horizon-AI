
import React, { useState, useEffect } from "react";
import { productApi } from "../api/api";

const LowStock = () => {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchLowStock();
  }, []);

  const fetchLowStock = async () => {
    try {
      setLoading(true);
      const data = await productApi.getLowStock();
      setItems(Array.isArray(data) ? data : []);
    } catch (error) {
      console.error("Error fetching low stock:", error);
      setItems([]);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="card p-4 shadow-sm">
        <p className="text-muted">Loading low stock alerts...</p>
      </div>
    );
  }

  return (
    <div className="card p-4 shadow-sm border-danger">
      <h2 className="text-danger mb-4">⚠️ Low Stock Alerts</h2>

      {items.length === 0 ? (
        <p className="text-muted">No low stock products 🎉</p>
      ) : (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>Product</th>
              <th>Current Stock</th>
              <th>Status</th>
              <th>Action</th>
            </tr>
          </thead>

          <tbody>
            {items.map((item) => (
              <tr key={item.id}>
                <td>{item.name || "Unknown"}</td>

                <td className="fw-bold text-danger">
                  {item.quantity ?? 0}
                </td>

                <td>
                  <span className="badge bg-warning text-dark">
                    Urgent Restock
                  </span>
                </td>

                <td>
                  <button className="btn btn-sm btn-outline-dark">
                    Notify Supplier
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default LowStock;

