package com.farmacia.model;

/**
 * Formas de pagamento aceitas no PDV (ponto de venda).
 */
public enum FormaPagamento {

    DINHEIRO("Dinheiro"),
    PIX("Pix"),
    CARTAO_DEBITO("Cartao de Debito"),
    CARTAO_CREDITO("Cartao de Credito");

    private final String rotulo;

    FormaPagamento(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
