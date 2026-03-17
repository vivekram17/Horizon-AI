package com.SmartInventory.dto;

public class SalesChartDTO {

    private String date;
    private double sales;

    public SalesChartDTO(String date, double sales) {
        this.date = date;
        this.sales = sales;
    }

    public String getDate() {
        return date;
    }

    public double getSales() {
        return sales;
    }
}