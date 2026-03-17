import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import DashboardLayout from "./layout/DashboardLayout";
import Dashboard from "./pages/Dashboard";
import Products from "./pages/Products";
import Orders from "./pages/Orders";
import LowStock from "./pages/LowStock";
import Forecast from "./pages/Forecast";

function App() {

  return (
    <Router>

      <DashboardLayout>

        <Routes>

          <Route path="/" element={<Dashboard />} />

          <Route path="/products" element={<Products />} />

          <Route path="/orders" element={<Orders />} />

          <Route path="/low-stock" element={<LowStock />} />

          <Route path="/forecast" element={<Forecast />} />

          <Route
            path="*"
            element={<h2 className="text-center mt-5">Page Not Found</h2>}
          />

        </Routes>

      </DashboardLayout>

    </Router>
  );
}

export default App;