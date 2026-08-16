package com.farmacia.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

/**
 * Paleta de cores, fontes e componentes reutilizaveis, dando ao sistema uma
 * identidade visual consistente (tema verde, remetendo a farmacia/saude).
 */
public final class Estilo {

    public static final Color VERDE = new Color(27, 156, 93);
    public static final Color VERDE_ESCURO = new Color(16, 110, 66);
    public static final Color VERDE_CLARO = new Color(232, 247, 239);
    public static final Color FUNDO = new Color(244, 247, 245);
    public static final Color BRANCO = Color.WHITE;
    public static final Color TEXTO = new Color(33, 37, 41);
    public static final Color CINZA = new Color(108, 117, 125);
    public static final Color CINZA_CLARO = new Color(222, 226, 224);
    public static final Color VERMELHO = new Color(214, 69, 65);
    public static final Color AMBAR = new Color(201, 148, 0);
    public static final Color LARANJA = new Color(226, 110, 30);

    public static final Font FONTE_TITULO =
            new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONTE_SUBTITULO =
            new Font("SansSerif", Font.BOLD, 15);
    public static final Font FONTE_NORMAL =
            new Font("SansSerif", Font.PLAIN, 14);
    public static final Font FONTE_PEQUENA =
            new Font("SansSerif", Font.PLAIN, 12);
    public static final Font FONTE_GRANDE_NUMERO =
            new Font("SansSerif", Font.BOLD, 26);

    private Estilo() {
    }

    /** Botao de acao principal (fundo verde, texto branco). */
    public static JButton botaoPrimario(String texto) {
        return botao(texto, VERDE, BRANCO);
    }

    /** Botao de acao destrutiva (fundo vermelho). */
    public static JButton botaoPerigo(String texto) {
        return botao(texto, VERMELHO, BRANCO);
    }

    /** Botao secundario (contorno, fundo branco). */
    public static JButton botaoSecundario(String texto) {
        JButton b = botao(texto, BRANCO, VERDE_ESCURO);
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        return b;
    }

    private static JButton botao(String texto, Color fundo, Color frente) {
        JButton b = new JButton(texto);
        b.setBackground(fundo);
        b.setForeground(frente);
        b.setFont(FONTE_SUBTITULO);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        return b;
    }

    public static JLabel titulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONTE_TITULO);
        l.setForeground(TEXTO);
        return l;
    }

    /** Borda com preenchimento interno padrao para paineis. */
    public static Border espacamento(int px) {
        return BorderFactory.createEmptyBorder(px, px, px, px);
    }

    /**
     * Borda de secao com titulo em laranja e negrito, no estilo de um
     * "quadro" agrupando campos (ex.: "Dados do Cliente").
     */
    public static TitledBorder secao(String titulo) {
        TitledBorder borda = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
                "  " + titulo + "  ");
        borda.setTitleColor(LARANJA);
        borda.setTitleFont(FONTE_SUBTITULO);
        return borda;
    }

    /** Rotulo de formulario padronizado. */
    public static JLabel rotulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FONTE_NORMAL);
        l.setForeground(TEXTO);
        return l;
    }

    /** Aplica fundo branco e borda arredondada suave a um componente. */
    public static void comoCartao(JComponent c) {
        c.setBackground(BRANCO);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CINZA_CLARO, 1, true),
                espacamento(16)));
    }
}
