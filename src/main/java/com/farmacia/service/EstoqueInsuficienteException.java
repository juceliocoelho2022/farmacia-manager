package com.farmacia.service;

/**
 * Lancada quando uma venda tenta retirar do estoque uma quantidade maior
 * do que a disponivel.
 */
public class EstoqueInsuficienteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
