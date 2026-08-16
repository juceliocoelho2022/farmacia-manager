package com.farmacia.model;

/**
 * Categorias de produtos disponiveis na farmacia.
 *
 * O rotulo ({@link #getRotulo()}) e usado para exibicao na interface,
 * enquanto o proprio nome do enum e usado para persistencia.
 */
public enum Categoria {

    MEDICAMENTO("Medicamento"),
    GENERICO("Generico"),
    ANTIBIOTICO("Antibiotico"),
    CONTROLADO("Controlado"),
    HIGIENE("Higiene Pessoal"),
    COSMETICO("Cosmetico"),
    SUPLEMENTO("Suplemento / Vitamina"),
    INFANTIL("Infantil"),
    PRIMEIROS_SOCORROS("Primeiros Socorros"),
    OUTROS("Outros");

    private final String rotulo;

    Categoria(String rotulo) {
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
