import React, { useEffect, useState, useMemo } from "react";
import { Cpu, Search, Zap, Loader2, Calendar, Target, RefreshCw, AlertTriangle } from "lucide-react";
import { forecastApi, aiForecastApi } from "../api/api";
import {
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
  ResponsiveContainer,
  AreaChart,
  Area
} from "recharts";

const Forecast = () => {
  const [forecasts, setForecasts] = useState([]);
  const [search, setSearch] = useState("");
  const [productIdInput, setProductIdInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [actionLoading, setActionLoading] = useState(false);

  useEffect(() => {
    loadForecasts();
  }, []);

  const loadForecasts = async () => {
    try {
      setLoading(true);
      const data = await forecastApi.getAll();
      if (!Array.isArray(data)) return;

      const parsed = data.map((item) => {
        let forecastMonths = [];
        try {
          // Check both field names commonly used in the backend
          const rawData = item.aiForecast || item.forecast;
          forecastMonths = typeof rawData === 'string' ? JSON.parse(rawData) : rawData;
        } catch (e) { 
          forecastMonths = []; 
        }

        // Convert Spring LocalDateTime array [YYYY, MM, DD, HH, mm, ss] to JS Date
        const d = item.generatedAt; 
        const jsDate = (Array.isArray(d) && d.length >= 3) 
          ? new Date(d[0], d[1] - 1, d[2], d[3] || 0, d[4] || 0, d[5] || 0) 
          : new Date(0);

        return {
          ...item,
          jsDate: jsDate,
          formattedDate: jsDate.getTime() > 0 ? jsDate.toLocaleString() : "N/A",
          months: Array.isArray(forecastMonths) ? forecastMonths : [],
          displayName: (forecastMonths && forecastMonths[0]?.productName) || item.product?.name || `Product ${item.productId}`
        };
      });

      // SORT: Highest timestamp (newest) first
      const sorted = parsed.sort((a, b) => b.jsDate.getTime() - a.jsDate.getTime());
      setForecasts(sorted);
    } catch (err) {
      console.error("Fetch failed", err);
    } finally {
      setLoading(false);
    }
  };

  const handleSingleForecast = async () => {
    if (!productIdInput) return alert("Please enter a Product ID");
    try {
      setActionLoading(true);
      await aiForecastApi.getByProduct(productIdInput);
      await loadForecasts(); // Refresh triggers the re-sort and date update
      setProductIdInput(""); 
    } catch (err) {
      console.error("Single forecast failed", err);
      alert("AI limit reached. Wait 1 minute.");
    } finally {
      setActionLoading(false);
    }
  };

  const handleRunAll = async () => {
    if(!window.confirm("Run AI for all products? This will take time.")) return;
    try {
      setActionLoading(true);
      await aiForecastApi.runAll();
      await loadForecasts();
    } catch (err) {
      console.error("Run all failed", err);
    } finally {
      setActionLoading(false);
    }
  };

  const filtered = useMemo(() => {
    const searchFiltered = forecasts.filter((f) =>
      f.productId.toString().includes(search) ||
      f.displayName.toLowerCase().includes(search.toLowerCase())
    );
    
    const uniqueMap = new Map();
    searchFiltered.forEach(item => {
      // Since 'forecasts' is sorted newest first, we keep the first occurrence
      if (!uniqueMap.has(item.productId)) {
        uniqueMap.set(item.productId, item);
      }
    });
    return Array.from(uniqueMap.values());
  }, [forecasts, search]);

  return (
    <div className="container-fluid p-4 bg-light min-vh-100">
      {/* HEADER SECTION */}
      <div className="card border-0 shadow-sm mb-4">
        <div className="card-body p-4">
          <div className="row g-3 align-items-center">
            <div className="col-lg-4">
              <div className="d-flex align-items-center">
                <div className="bg-primary bg-opacity-10 p-3 rounded-3 me-3">
                  <Cpu size={32} className="text-primary" />
                </div>
                <div>
                  <h4 className="fw-bold mb-0">AI Forecasts</h4>
                  <small className="text-muted">Dynamic Inventory Prediction</small>
                </div>
              </div>
            </div>

            <div className="col-lg-4">
              <div className="input-group">
                <input
                  type="number"
                  className="form-control"
                  placeholder="Target Product ID..."
                  value={productIdInput}
                  onChange={(e) => setProductIdInput(e.target.value)}
                />
                <button className="btn btn-primary d-flex align-items-center" onClick={handleSingleForecast} disabled={actionLoading}>
                  {actionLoading ? <Loader2 size={16} className="animate-spin me-2"/> : <Target size={16} className="me-2" />}
                  Run AI
                </button>
              </div>
            </div>

            <div className="col-lg-4 text-lg-end">
              <button className="btn btn-dark rounded-3 px-4 shadow-sm" onClick={handleRunAll} disabled={actionLoading}>
                {actionLoading ? <Loader2 className="animate-spin" size={18} /> : 
                <><Zap size={18} className="me-2 text-warning" /> Process All</>}
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="d-flex justify-content-between mb-4">
        <div className="position-relative w-25">
          <Search className="position-absolute top-50 start-0 translate-middle-y ms-3 text-muted" size={16} />
          <input
            type="text"
            className="form-control ps-5 rounded-pill border-0 shadow-sm"
            placeholder="Search products..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
        </div>
        <button className="btn btn-link text-muted" onClick={loadForecasts}>
          <RefreshCw size={16} className={loading ? "animate-spin" : ""} />
        </button>
      </div>

      <div className="row g-4">
        {loading && forecasts.length === 0 ? (
          <div className="col-12 text-center py-5"><Loader2 className="animate-spin text-primary" size={40} /></div>
        ) : filtered.length === 0 ? (
          <div className="col-12 text-center py-5">
             <AlertTriangle className="text-muted mb-2" size={40} />
             <p className="text-muted">No forecasts found.</p>
          </div>
        ) : filtered.map((item) => (
          <div className="col-xl-4 col-md-6" key={item.productId}>
            <div className="card border-0 shadow-sm h-100">
              <div className="card-body">
                <div className="d-flex justify-content-between mb-3">
                  <span className="badge bg-primary bg-opacity-10 text-primary">PID: {item.productId}</span>
                  <span className="text-muted small d-flex align-items-center">
                    <Calendar size={14} className="me-1"/> {item.formattedDate}
                  </span>
                </div>
                <h5 className="fw-bold">{item.displayName}</h5>
                
                <div style={{ height: 160 }} className="mt-3">
                  <ResponsiveContainer width="100%" height="100%">
                    <AreaChart data={item.months}>
                      <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f0f0f0" />
                      <XAxis dataKey="month" hide />
                      <YAxis tick={{fontSize: 10}} />
                      <Tooltip />
                      <Area type="monotone" dataKey="predictedDemand" stroke="#6366f1" fill="#6366f1" fillOpacity={0.1} strokeWidth={2} />
                    </AreaChart>
                  </ResponsiveContainer>
                </div>
                
                <div className="mt-3 pt-3 border-top d-flex justify-content-between align-items-center">
                  <small className="text-muted">Total Prediction:</small>
                  <span className="fw-bold text-success">
                    {item.months.reduce((acc, m) => acc + (m.predictedDemand || 0), 0)} Units
                  </span>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Forecast;