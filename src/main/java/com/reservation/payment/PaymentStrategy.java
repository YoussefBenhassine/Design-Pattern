package com.reservation.payment;

import java.math.BigDecimal;

/**
 * Interface Strategy pour les méthodes de paiement
 * Design Pattern: Strategy
 * Principe SOLID: OCP (Open/Closed Principle) - On peut ajouter de nouvelles méthodes de paiement sans modifier le code existant
 */
public interface PaymentStrategy {
    /**
     * Traite un paiement
     * @param amount Montant à payer
     * @param paymentDetails Détails spécifiques à la méthode de paiement
     * @return true si le paiement est réussi
     */
    boolean processPayment(BigDecimal amount, String paymentDetails);
    
    /**
     * Retourne le nom de la méthode de paiement
     */
    String getMethodName();
}

