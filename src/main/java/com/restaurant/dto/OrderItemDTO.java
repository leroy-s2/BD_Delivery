package com.restaurant.dto;

import java.math.BigDecimal;

public class OrderItemDTO {
    private Long id;
    private Long orderId;
    private Long menuItemId;
    private String menuItemName; // 👈 importante para mostrar en Android
    private Integer quantity;
    private BigDecimal price;
    private String specialInstructions;

    public OrderItemDTO() {}

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }
    public void setMenuItemId(Long menuItemId) {
        this.menuItemId = menuItemId;
    }

    public String getMenuItemName() {
        return menuItemName;
    }
    public void setMenuItemName(String menuItemName) {
        this.menuItemName = menuItemName;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }
    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}
