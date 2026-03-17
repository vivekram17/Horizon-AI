import React, { useState, useEffect } from "react";
import { productApi } from "../api/api";
import { PackagePlus, ClipboardList, Loader2, Tag, Truck, AlertTriangle } from "lucide-react";

const Products = () => {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [isAdding, setIsAdding] = useState(false);

  const [formData, setFormData] = useState({
    name: "",
    sku: "",
    price: "",
    quantity: "",
    minThreshold: "5",
    categoryId: "",
    supplierId: ""
  });

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      setLoading(true);
      const data = await productApi.getAll();
      setProducts(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Failed to load products:", err);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsAdding(true);

    try {
      // CONSTRUCTING NESTED PAYLOAD
      const payload = {
        name: formData.name,
        sku: formData.sku,
        price: parseFloat(formData.price),
        quantity: parseInt(formData.quantity),
        minThreshold: parseInt(formData.minThreshold),
        category: { id: parseInt(formData.categoryId) },
        supplier: { id: parseInt(formData.supplierId) }
      };

      await productApi.create(payload);
      
      alert("🎉 Product saved successfully!");
      setFormData({ name: "", sku: "", price: "", quantity: "", minThreshold: "5", categoryId: "", supplierId: "" });
      loadProducts();
    } catch (err) {
      alert(`Error: ${err.message || "Could not save. Check if Category/Supplier IDs exist."}`);
    } finally {
      setIsAdding(false);
    }
  };

  return (
    <div className="container-fluid py-4 bg-light min-vh-100">
      <div className="row g-4">
        
        {/* ADD PRODUCT FORM */}
        <div className="col-lg-4">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4">
              <div className="d-flex align-items-center mb-4 text-primary">
                <PackagePlus className="me-2" />
                <h4 className="fw-bold mb-0">Add Product</h4>
              </div>

              <form onSubmit={handleSubmit}>
                <div className="mb-2">
                  <label className="small fw-bold text-muted">NAME & SKU</label>
                  <input name="name" className="form-control mb-2" placeholder="Product Name" value={formData.name} onChange={handleChange} required />
                  <input name="sku" className="form-control" placeholder="SKU Code" value={formData.sku} onChange={handleChange} required />
                </div>

                <div className="row g-2 mb-2">
                  <div className="col-6">
                    <label className="small fw-bold text-muted">PRICE (₹)</label>
                    <input name="price" type="number" className="form-control" placeholder="0.00" value={formData.price} onChange={handleChange} required />
                  </div>
                  <div className="col-6">
                    <label className="small fw-bold text-muted">STOCK QTY</label>
                    <input name="quantity" type="number" className="form-control" placeholder="0" value={formData.quantity} onChange={handleChange} required />
                  </div>
                </div>

                <div className="mb-3">
                  <label className="small fw-bold text-muted">MIN THRESHOLD</label>
                  <input name="minThreshold" type="number" className="form-control" value={formData.minThreshold} onChange={handleChange} required />
                </div>

                <div className="row g-2 mb-4 p-3 bg-light rounded-3 border">
                  <div className="col-6 text-center">
                    <label className="small fw-bold"><Tag size={12}/> CATEGORY ID</label>
                    <input name="categoryId" type="number" className="form-control text-center" placeholder="ID" value={formData.categoryId} onChange={handleChange} required />
                  </div>
                  <div className="col-6 text-center">
                    <label className="small fw-bold"><Truck size={12}/> SUPPLIER ID</label>
                    <input name="supplierId" type="number" className="form-control text-center" placeholder="ID" value={formData.supplierId} onChange={handleChange} required />
                  </div>
                </div>

                <button className="btn btn-primary w-100 py-2 fw-bold shadow-sm" disabled={isAdding}>
                  {isAdding ? <Loader2 className="animate-spin mx-auto" size={20}/> : "Register Product"}
                </button>
              </form>
            </div>
          </div>
        </div>

        {/* INVENTORY LIST */}
        <div className="col-lg-8">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4">
              <div className="d-flex align-items-center mb-4 text-secondary">
                <ClipboardList className="me-2" />
                <h4 className="fw-bold mb-0">Current Inventory</h4>
              </div>

              {loading ? (
                <div className="text-center py-5"><Loader2 className="animate-spin text-primary mx-auto" size={40}/></div>
              ) : (
                <div className="table-responsive">
                  <table className="table table-hover align-middle">
                    <thead className="table-light">
                      <tr className="small text-muted">
                        <th>SKU</th>
                        <th>NAME</th>
                        <th>CATEGORY</th>
                        <th>PRICE</th>
                        <th>STOCK</th>
                      </tr>
                    </thead>
                    <tbody>
                      {products.map((p) => (
                        <tr key={p.id}>
                          <td><code>{p.sku}</code></td>
                          <td className="fw-medium">{p.name}</td>
                          <td><span className="badge bg-light text-dark border">ID: {p.category?.id || 'N/A'}</span></td>
                          <td>₹{p.price?.toLocaleString()}</td>
                          <td>
                            <div className="d-flex align-items-center">
                              <span className={p.quantity <= p.minThreshold ? 'text-danger fw-bold' : 'text-success'}>
                                {p.quantity}
                              </span>
                              {p.quantity <= p.minThreshold && <AlertTriangle size={14} className="ms-1 text-danger" />}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              )}
            </div>
          </div>
        </div>

      </div>
    </div>
  );
};

export default Products;