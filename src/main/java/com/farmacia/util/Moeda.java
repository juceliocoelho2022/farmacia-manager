package com.farmacia.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Utilitarios para formatacao e conversao de valores monetarios em Real (BRL).
 */
public final class Moeda {

    private static final Locale BRASIL = Locale.forLanguageTag("pt-BR");

    private Moeda() {
    }

    /** Formata um valor como moeda brasileira, ex.: {@code R$ 12,50}. */
    public static String formatar(BigDecimal valor) {
        BigDecimal seguro = valor == null ? BigDecimal.ZERO : valor;
        NumberFormat formato = NumberFormat.getCurrencyInstance(BRASIL);
        return formato.format(seguro);
    }

    /**
     * Converte um texto digitado pelo usuario em {@link BigDecimal}.
     * Aceita virgula ou ponto como separador decimal e ignora o simbolo "R$".
     *
     * @throws NumberFormatException se o texto nao representar um numero
     */
    public static BigDecimal converter(String texto) {
        if (texto == null || texto.isBlank()) {
            return BigDecimal.ZERO;
        }
        String limpo = texto
                .replace("R$", "")
                .replace(".", "")
                .replace(",", ".")
                .trim();
        return new BigDecimal(limpo).setScale(2, RoundingMode.HALF_UP);
    }
}
