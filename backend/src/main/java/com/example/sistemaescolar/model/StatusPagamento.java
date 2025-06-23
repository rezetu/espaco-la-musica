package com.example.sistemaescolar.model;

/**
 * Enumeração para representar o status do pagamento de uma matrícula.
 */
public enum StatusPagamento {
    PENDENTE, // O pagamento ainda não foi realizado.
    PAGO,     // O pagamento foi confirmado.
    ATRASADO  // O pagamento está vencido.
}
