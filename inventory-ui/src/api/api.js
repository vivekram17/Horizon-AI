import axios from "axios";

// Create the centralized Axios instance
const API = axios.create({
  baseURL: import.meta.env.VITE_API_URL || "http://localhost:6868/api",
  headers: { "Content-Type": "application/json" },
  timeout: 10000 // Default 10s timeout
});

// Response interceptor to simplify data access and handle errors
API.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error("API Error Context:", error.response?.data || error.message);
    return Promise.reject(error.response?.data || "Network error");
  }
);

/**
 * Product Controller Endpoints
 */
export const productApi = {
  getAll: () => API.get("/products"),
  getById: (id) => API.get(`/products/${id}`),
  getLowStock: () => API.get("/products/low-stock"),
  create: (productData) => API.post("/products", productData)
};

/**
 * Order Controller Endpoints
 */
export const orderApi = {
  // Matches POST /api/orders with Query Params: ?productId=X&quantity=Y
  create: (orderData) => 
    API.post("/orders", null, {
      params: {
        productId: orderData.productId,
        quantity: orderData.quantity
      }
    }),

  // Matches GET /api/orders/sales/last30days
  getRecentSales: () => API.get("/orders/sales/last30days")
};

/**
 * Forecast Controller Endpoints (Standard Reports)
 */
export const forecastApi = {
  getAll: () => API.get("/forecast/all"),
  getByProduct: (productId) => API.get(`/forecast/product/${productId}`),
  getSupplierForecast: () => API.get("/forecast/supplier")
};

/**
 * Demand Forecast Controller Endpoints (AI-Driven)
 */
export const aiForecastApi = {
  // Triggered by "Run AI" for a specific product
  getByProduct: (productId) => API.get(`/ai-forecast/${productId}`, {
    timeout: 60000 // Extended to 1 min for AI generation
  }),

  // Global AI refresh
  runAll: () => API.get("/ai-forecast/run", {
    timeout: 300000 // Extended to 5 mins for bulk processing
  })
};

export default API;