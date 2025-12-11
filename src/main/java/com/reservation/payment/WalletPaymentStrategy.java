package com.reservation.payment;

import java.math.BigDecimal;

/**
 * Implémentation Strategy pour le paiement par wallet
 */
public class WalletPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(BigDecimal amount, String paymentDetails) {
        // Simulation du traitement de paiement par wallet
        System.out.println("Traitement du paiement par wallet: " + amount + "€");
        System.out.println("ID Wallet: " + paymentDetails);
        // Simulation: toujours réussi pour la démo
        return true;
    }

    @Override
    public String getMethodName() {
        return "Wallet";
    }
}

