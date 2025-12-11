package com.reservation.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un prestataire de services
 */
public class Prestataire extends User {
    private List<String> serviceIds;
    private double rating;
    private int totalReviews;

    public Prestataire(String id, String name, String email, String phone) {
        super(id, name, email, phone);
        this.serviceIds = new ArrayList<>();
        this.rating = 0.0;
        this.totalReviews = 0;
    }

    public List<String> getServiceIds() {
        return serviceIds;
    }

    public void addService(String serviceId) {
        if (!serviceIds.contains(serviceId)) {
            serviceIds.add(serviceId);
        }
    }

    public double getRating() {
        return rating;
    }

    public void updateRating(double newRating) {
        totalReviews++;
        rating = ((rating * (totalReviews - 1)) + newRating) / totalReviews;
    }

    public int getTotalReviews() {
        return totalReviews;
    }

    @Override
    public String getRole() {
        return "PRESTATAIRE";
    }
}

