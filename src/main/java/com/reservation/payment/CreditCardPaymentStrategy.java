package com.reservation.payment;

import java.math.BigDecimal;

/**
 * Implémentation Strategy pour le paiement par carte de crédit
 */
public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(BigDecimal amount, String paymentDetails) {
        // Simulation du traitement de paiement par carte
        System.out.println("Traitement du paiement par carte de crédit: " + amount + "€");
        System.out.println("Détails: " + paymentDetails);
        // Simulation: toujours réussi pour la démo
        return true;
    }

    @Override
    public String getMethodName() {
        return "Carte de crédit";
    }
}

