package com.reservation.service;

import com.reservation.model.*;
import com.reservation.payment.PaymentStrategy;
import com.reservation.repository.PaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service de gestion des paiements
 * Principe SOLID: SRP (Single Responsibility Principle) - Responsabilité unique de gestion des paiements
 * Principe SOLID: DIP (Dependency Inversion Principle) - Dépend de l'abstraction PaymentStrategy
 */
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private PaymentStrategy paymentStrategy;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Définit la stratégie de paiement
     * Design Pattern: Strategy
     */
    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    /**
     * Traite un paiement
     */
    public Payment processPayment(String reservationId, BigDecimal amount, 
                                  PaymentMethod method, String paymentDetails) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Stratégie de paiement non définie");
        }

        Payment payment = new Payment(
            UUID.randomUUID().toString(),
            reservationId,
            amount,
            method
        );

        boolean success = paymentStrategy.processPayment(amount, paymentDetails);
        
        if (success) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return paymentRepository.save(payment);
    }

    /**
     * Rembourse un paiement
     */
    public void refundPayment(String paymentId) {
        paymentRepository.findById(paymentId).ifPresent(payment -> {
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);
        });
    }

    public PaymentRepository getPaymentRepository() {
        return paymentRepository;
    }
}

