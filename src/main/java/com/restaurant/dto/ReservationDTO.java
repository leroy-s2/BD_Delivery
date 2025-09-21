package com.restaurant.dto;

import java.time.LocalDateTime;

public class ReservationDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime reservationDateTime;
    private Integer partySize;
    private String specialRequests;
    private String status;
    private LocalDateTime createdAt;

    // Constructors
    public ReservationDTO() {}

    public ReservationDTO(Long userId, Long restaurantId, LocalDateTime reservationDateTime, Integer partySize) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.reservationDateTime = reservationDateTime;
        this.partySize = partySize;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }

    public LocalDateTime getReservationDateTime() { return reservationDateTime; }
    public void setReservationDateTime(LocalDateTime reservationDateTime) { this.reservationDateTime = reservationDateTime; }

    public Integer getPartySize() { return partySize; }
    public void setPartySize(Integer partySize) { this.partySize = partySize; }

    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}