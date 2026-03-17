import React from "react";
import { Link, useLocation } from "react-router-dom";
import { LayoutDashboard, Package, ShoppingCart, AlertTriangle, TrendingUp, Box } from "lucide-react";

const Sidebar = () => {
  const location = useLocation();

  const getLinkClass = (path) => 
    `nav-link-custom ${location.pathname === path ? "active shadow-sm" : ""}`;

  return (
    /* Added pt-4 for top breathing room */
    <div className="d-flex flex-column h-100 pt-4">
      
      {/* Branding - Increased mb-5 for more space below the logo */}
      <div className="d-flex align-items-center mb-5 px-3">
        <div className="stat-icon me-3" style={{ width: '40px', height: '40px' }}>
             <Box className="text-primary" size={22} strokeWidth={2.5} />
        </div>
        <h4 className="mb-0 fw-bold" style={{ color: '#1B2559', letterSpacing: '-0.5px' }}>
            Horizon AI
        </h4>
      </div>

      {/* Navigation */}
      <nav className="flex-grow-1 px-2">
        <Link to="/" className={getLinkClass("/")}>
          <LayoutDashboard size={20} className="me-3" /> Dashboard
        </Link>
        
        <Link to="/products" className={getLinkClass("/products")}>
          <Package size={20} className="me-3" /> Products
        </Link>
        
        <Link to="/orders" className={getLinkClass("/orders")}>
          <ShoppingCart size={20} className="me-3" /> Orders
        </Link>
        
        <Link to="/low-stock" className={getLinkClass("/low-stock")}>
          <AlertTriangle size={20} className="me-3" /> Low Stock
        </Link>
        
        <Link to="/forecast" className={getLinkClass("/forecast")}>
          <TrendingUp size={20} className="me-3" /> AI Forecast
        </Link>
      </nav>

      {/* Profile/Footer */}
      <div className="mt-auto p-3">
        <div className="d-flex align-items-center px-3 py-3 rounded-4" style={{ background: '#F4F7FE' }}>
          <div className="bg-white rounded-circle p-2 shadow-sm me-3">
            <Box size={16} className="text-primary" />
          </div>
          <div className="d-flex flex-column">
             <span className="small fw-bold" style={{ color: '#1B2559' }}>Admin Node</span>
             <span className="text-muted" style={{ fontSize: '0.7rem' }}>v1.0.4-stable</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Sidebar;