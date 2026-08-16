package com.farmacia;

import com.farmacia.ui.JanelaPrincipal;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do Sistema Farmacia (aplicativo desktop).
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        JanelaPrincipal.aplicarTema();

        SwingUtilities.invokeLater(() -> {
            try {
                ContextoAplicacao contexto = new ContextoAplicacao();
                new JanelaPrincipal(contexto).setVisible(true);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null,
                        "Falha ao iniciar o sistema:\n" + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
                throw e;
            }
        });
    }
}
