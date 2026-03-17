import React, { useState, useEffect } from "react";
import { ShoppingCart, History, Package, Loader2, AlertCircle, TrendingUp, Calendar } from "lucide-react";
import { orderApi } from "../api/api";

const Orders = () => {
  const [salesData, setSalesData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const [order, setOrder] = useState({
    productId: "",
    quantity: 1
  });

  useEffect(() => {
    fetchSales();
  }, []);

  const fetchSales = async () => {
    try {
      setLoading(true);
      const data = await orderApi.getRecentSales();
      setSalesData(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Error loading sales:", err);
    } finally {
      setLoading(false);
    }
  };

  // Helper to format the [YYYY, M, D, H, M, S] array from Spring Boot
  const formatBackendDate = (dateArray) => {
    if (!Array.isArray(dateArray) || dateArray.length < 3) return "N/A";
    const [year, month, day] = dateArray;
    // month is usually 1-indexed in these arrays
    return new Date(year, month - 1, day).toLocaleDateString('en-IN', {
      day: '2-digit',
      month: 'short',
      year: 'numeric'
    });
  };

  const placeOrder = async (e) => {
    e.preventDefault();
    const pId = parseInt(order.productId);
    const qty = parseInt(order.quantity);

    if (isNaN(pId) || pId <= 0) return alert("Please enter a valid Product ID");

    try {
      setIsSubmitting(true);
      await orderApi.create({ productId: pId, quantity: qty });

      alert("🎉 Order Processed Successfully!");
      setOrder({ productId: "", quantity: 1 });
      await fetchSales();
    } catch (err) {
      console.error("Order failed:", err);
      const msg = typeof err === 'string' ? err : err.message || "Check stock levels or ID";
      alert(`Order Failed: ${msg}`);
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container-fluid p-4 bg-light min-vh-100">
      <div className="row g-4">
        
        {/* FORM COLUMN */}
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4">
              <div className="d-flex align-items-center mb-4">
                <div className="bg-success bg-opacity-10 p-2 rounded-3 me-3 text-success">
                  <ShoppingCart size={24} />
                </div>
                <h4 className="fw-bold mb-0">Create Order</h4>
              </div>

              <form onSubmit={placeOrder}>
                <div className="mb-3">
                  <label className="form-label fw-semibold small text-muted text-uppercase">Product ID</label>
                  <div className="input-group shadow-sm rounded">
                    <span className="input-group-text bg-white border-end-0"><Package size={18} className="text-muted"/></span>
                    <input
                      type="number"
                      className="form-control border-start-0 ps-0"
                      placeholder="Enter ID..."
                      value={order.productId}
                      onChange={(e) => setOrder({ ...order, productId: e.target.value })}
                      required
                    />
                  </div>
                </div>

                <div className="mb-4">
                  <label className="form-label fw-semibold small text-muted text-uppercase">Quantity</label>
                  <input
                    type="number"
                    className="form-control shadow-sm"
                    min="1"
                    value={order.quantity}
                    onChange={(e) => setOrder({ ...order, quantity: e.target.value })}
                    required
                  />
                </div>

                <button 
                  className="btn btn-success w-100 py-2 fw-bold d-flex align-items-center justify-content-center shadow-sm"
                  disabled={isSubmitting}
                >
                  {isSubmitting ? (
                    <><Loader2 className="animate-spin me-2" size={18}/> Processing...</>
                  ) : "Process Order"}
                </button>
              </form>
            </div>
          </div>
        </div>

        {/* DATA COLUMN */}
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm rounded-4 h-100">
            <div className="card-body p-4">
              <div className="d-flex justify-content-between align-items-center mb-4">
                <div className="d-flex align-items-center">
                  <div className="bg-primary bg-opacity-10 p-2 rounded-3 me-3 text-primary">
                    <TrendingUp size={24} />
                  </div>
                  <div>
                    <h4 className="fw-bold mb-0">Order History</h4>
                    <p className="text-muted small mb-0">Recent transactions from the last 30 days</p>
                  </div>
                </div>
                <span className="badge bg-primary bg-opacity-10 text-primary border border-primary border-opacity-25 px-3 py-2">
                  {salesData.length} Records
                </span>
              </div>

              {loading ? (
                <div className="text-center py-5">
                  <Loader2 className="animate-spin text-primary mx-auto" size={40} />
                  <p className="text-muted mt-2">Fetching records...</p>
                </div>
              ) : salesData.length > 0 ? (
                <div className="table-responsive">
                  <table className="table table-hover align-middle">
                    <thead className="table-light">
                      <tr className="small text-muted text-uppercase">
                        <th>ID</th>
                        <th>Product Name</th>
                        <th>Date</th>
                        <th className="text-center">Qty</th>
                        <th className="text-end">Total Price</th>
                      </tr>
                    </thead>
                    <tbody>
                      {salesData.map((sale) => (
                        <tr key={sale.id}>
                          <td className="text-muted font-monospace small">#{sale.id}</td>
                          <td>
                            <span className="fw-semibold text-dark">{sale.productName}</span>
                          </td>
                          <td className="small text-muted">
                            <Calendar size={14} className="me-1 mb-1" />
                            {formatBackendDate(sale.date)}
                          </td>
                          <td className="text-center">
                            <span className="badge bg-light text-dark fw-normal">{sale.quantity}</span>
                          </td>
                          <td className="text-end fw-bold text-success">
                            ₹{sale.totalPrice?.toLocaleString('en-IN')}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              ) : (
                <div className="text-center py-5 border rounded-4 bg-light border-dashed">
                  <AlertCircle className="text-muted mb-2" size={32} />
                  <p className="text-muted mb-0">No sales history found for this period.</p>
                </div>
              )}
            </div>
          </div>
        </div>

      </div>  
    </div>
  );
};

export default Orders;