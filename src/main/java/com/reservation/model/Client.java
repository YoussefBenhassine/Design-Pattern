package com.reservation.model;

/**
 * Représente un client utilisateur
 */
public class Client extends User {
    public Client(String id, String name, String email, String phone) {
        super(id, name, email, phone);
    }

    @Override
    public String getRole() {
        return "CLIENT";
    }
}

