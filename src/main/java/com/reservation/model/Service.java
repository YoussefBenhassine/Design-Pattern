package com.reservation.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Représente un service proposé
 */
public class Service {
    private String id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int duration; // en minutes
    private String prestataireId;

    public Service(String id, String name, String description, String category, 
                   BigDecimal price, int duration, String prestataireId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.duration = duration;
        this.prestataireId = prestataireId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getPrestataireId() {
        return prestataireId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return Objects.equals(id, service.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

