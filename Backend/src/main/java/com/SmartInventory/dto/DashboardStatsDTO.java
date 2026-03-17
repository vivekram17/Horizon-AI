package com.SmartInventory.dto;

public class DashboardStatsDTO {

    private long totalProducts;
    private long lowStockProducts;
    private long totalOrders;
    private double todaySales;

    public DashboardStatsDTO(long totalProducts, long lowStockProducts,
                              long totalOrders, double todaySales) {
        this.totalProducts = totalProducts;
        this.lowStockProducts = lowStockProducts;
        this.totalOrders = totalOrders;
        this.todaySales = todaySales;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public long getLowStockProducts() {
        return lowStockProducts;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public double getTodaySales() {
        return todaySales;
    }
}