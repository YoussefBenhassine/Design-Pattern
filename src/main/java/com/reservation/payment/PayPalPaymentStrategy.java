package com.reservation.payment;

import java.math.BigDecimal;

/**
 * Implémentation Strategy pour le paiement PayPal
 */
public class PayPalPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(BigDecimal amount, String paymentDetails) {
        // Simulation du traitement de paiement PayPal
        System.out.println("Traitement du paiement PayPal: " + amount + "€");
        System.out.println("Email PayPal: " + paymentDetails);
        // Simulation: toujours réussi pour la démo
        return true;
    }

    @Override
    public String getMethodName() {
        return "PayPal";
    }
}

