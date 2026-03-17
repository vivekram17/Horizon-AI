import React, { useEffect, useState } from "react";
import { Package, AlertTriangle, TrendingUp, IndianRupee, Loader2 } from "lucide-react";
import { productApi, forecastApi } from "../api/api";
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from "recharts";

const Dashboard = () => {
  const [stats, setStats] = useState({
    totalProducts: 0,
    lowStockCount: 0,
    totalValue: 0,
    chartData: [],
    forecastCount: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      // Fetch data from both APIs
      const [products, forecasts] = await Promise.all([
        productApi.getAll(),
        forecastApi.getAll()
      ]);

      // Calculate totals
      const safeProducts = Array.isArray(products) ? products : [];
      const lowStock = safeProducts.filter(p => p.quantity <= (p.minThreshold || 0)).length;
      const totalVal = safeProducts.reduce((acc, p) => acc + (p.price * p.quantity), 0);
      
      // Prepare chart data (Top 8 products)
      const chartData = [...safeProducts]
        .sort((a, b) => b.quantity - a.quantity)
        .slice(0, 8)
        .map(p => ({ 
          name: p.name || `ID: ${p.id}`, 
          stock: p.quantity || 0, 
          limit: p.minThreshold || 0 
        }));

      setStats({
        totalProducts: safeProducts.length,
        lowStockCount: lowStock,
        totalValue: totalVal,
        chartData: chartData,
        forecastCount: Array.isArray(forecasts) ? forecasts.length : 0
      });
    } catch (err) {
      console.error("Dashboard data fetch failed:", err);
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center min-vh-100 bg-light">
        <Loader2 className="animate-spin text-primary" size={48} />
      </div>
    );
  }

  return (
    <div className="container-fluid p-4 bg-light min-vh-100">
      <div className="mb-4">
        <h3 className="fw-bold text-dark">Inventory Overview</h3>
        <p className="text-muted">Real-time status and stock valuation dashboard.</p>
      </div>

      {/* STAT CARDS */}
      <div className="row g-4 mb-4">
        <StatCard title="Total Products" value={stats.totalProducts} icon={<Package className="text-primary" />} color="primary" />
        <StatCard title="Low Stock Alerts" value={stats.lowStockCount} icon={<AlertTriangle className="text-danger" />} color="danger" isAlert={stats.lowStockCount > 0} />
        <StatCard title="Inventory Value" value={`₹${stats.totalValue.toLocaleString('en-IN')}`} icon={<IndianRupee className="text-success" />} color="success" />
        <StatCard title="Total Forecasts" value={stats.forecastCount} icon={<TrendingUp className="text-info" />} color="info" />
      </div>

      {/* CHART SECTION */}
      <div className="row">
        <div className="col-12">
          <div className="card border-0 shadow-sm rounded-4">
            <div className="card-body p-4">
              <h5 className="fw-bold mb-4">Stock Levels (Top Products)</h5>
              {/* Fix: Added min-height to ensure ResponsiveContainer doesn't throw warnings */}
              <div style={{ width: "100%", height: "400px", minHeight: "300px" }}>
                <ResponsiveContainer width="100%" height="100%">
                  <BarChart data={stats.chartData} margin={{ top: 10, right: 30, left: 0, bottom: 0 }}>
                    <CartesianGrid strokeDasharray="3 3" vertical={false} opacity={0.2} />
                    <XAxis dataKey="name" tick={{ fontSize: 12, fill: '#6c757d' }} axisLine={false} tickLine={false} />
                    <YAxis tick={{ fontSize: 12, fill: '#6c757d' }} axisLine={false} tickLine={false} />
                    <Tooltip 
                      cursor={{fill: '#f8f9fa'}} 
                      contentStyle={{ borderRadius: '8px', border: 'none', boxShadow: '0 4px 12px rgba(0,0,0,0.1)' }}
                    />
                    <Bar dataKey="stock" radius={[6, 6, 0, 0]} barSize={50}>
                      {stats.chartData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={entry.stock <= entry.limit ? "#dc3545" : "#4f46e5"} />
                      ))}
                    </Bar>
                  </BarChart>
                </ResponsiveContainer>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

// Sub-component for individual Stat Cards
const StatCard = ({ title, value, icon, color, isAlert }) => (
  <div className="col-md-6 col-xl-3">
    <div className={`card border-0 shadow-sm h-100 rounded-4 transition-all ${isAlert ? 'border-start border-danger border-4' : ''}`}>
      <div className="card-body p-4">
        <div className="d-flex justify-content-between align-items-center mb-2">
          <span className="text-muted small fw-bold text-uppercase tracking-wider">{title}</span>
          <div className={`bg-${color} bg-opacity-10 p-2 rounded-3`}>
            {icon}
          </div>
        </div>
        <h2 className="mb-0 fw-bold">{value}</h2>
      </div>
    </div>
  </div>
);

export default Dashboard;