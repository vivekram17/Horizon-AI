# 📦 Smart Inventory Management System

An AI-powered inventory management system that helps businesses track products, monitor stock levels, analyze sales, and predict future demand.

---

## 🚀 Features

### 📊 Inventory Management
- Add, view, and manage products
- Track stock levels in real-time
- SKU-based product organization

### 🛒 Order Management
- Create and process orders
- Track recent sales (last 30 days)
- Automatic stock reduction on purchase

### ⚠️ Low Stock Alerts
- Detect products below threshold
- Highlight urgent restock items
- Supplier notification support (planned)

### 🤖 AI Demand Forecasting
- Predict future demand using past sales
- Generate monthly forecast data
- Store forecast reports in database
- Visualize using charts

### 🔁 Automatic Reorder (Upcoming / Optional)
- Suggest restock quantity
- Predict stockout days
- Smart inventory optimization

---

## 🧱 Tech Stack

### Backend
- Java
- Spring Boot
- Spring Data JPA
- MySQL

### Frontend
- React (Vite)
- Axios
- Bootstrap
- Recharts (for charts)

---

## 📁 Project Structure

```
smart-inventory/
│
├── backend/
│ ├── controller/
│ ├── service/
│ ├── repository/
│ ├── entity/
│ └── dto/
│
├── frontend/
│ ├── src/
│ │ ├── api/
│ │ ├── components/
│ │ ├── pages/
│ │ └── App.jsx
│
└── README.md
```

---

## ⚙️ Setup Instructions

### 🔧 Backend (Spring Boot)

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/smart-inventory.git
   ```

2. Configure MySQL in `application.properties`
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/inventory
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```

3. Run the application
   ```bash
   mvn spring-boot:run
   ```

### 💻 Frontend (React + Vite)

1. Navigate to frontend folder
   ```bash
   cd frontend
   ```

2. Install dependencies
   ```bash
   npm install
   ```

3. Create `.env` file
   ```
   VITE_API_URL=http://localhost:6868/api
   ```

4. Start development server
   ```bash
   npm run dev
   ```

---

## 📡 API Endpoints

### Products
- `GET /api/products`
- `POST /api/products`
- `GET /api/products/low-stock`

### Orders
- `POST /api/orders`
- `GET /api/orders/sales/last30days`

### Forecast
- `GET /api/forecast/{productId}`
- `GET /api/forecast/all`
- `POST /api/forecast/run`

---

## 📈 Sample Forecast Output

```json
[
  {
    "month": "April",
    "predictedDemand": 120,
    "remainingStock": 80,
    "recommendedRestock": 50
  }
]
```

---

## 📊 UI Screens

- Dashboard overview
- Product management table
- Order creation panel
- Low stock alerts
- AI Forecast charts

---

## 🧠 How It Works

- Collects last 90 days sales data
- Calculates average daily sales
- Sends data to AI model
- Generates future demand forecast
- Stores results in database
- Displays charts in UI

---

## 🔮 Future Improvements

- Advanced ML forecasting models
- Role-based authentication
- Export reports (PDF/Excel)

---

## 🤝 Contributing

Pull requests are welcome. For major changes, please open an issue first.
