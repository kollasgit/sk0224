package com.toolrental.model;

public class Tool {
    private String code;
    private String brand;
    private ToolType toolType;

    public Tool(String code, String brand, ToolType toolType) {
        this.code = code;
        this.brand = brand;
        this.toolType = toolType;
    }

    public String getCode() {
        return code;
    }

    public double getRentalPrice() {
        return this.getToolType().getDailyCharge();
    }

    public ToolType getToolType() {
        return toolType;
    }

    public String getBrand() {
        return brand;
    }

}