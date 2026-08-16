package com.farmacia.ui;

import com.farmacia.ContextoAplicacao;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import java.awt.Dimension;

/**
 * Janela principal do sistema, organizada em abas: Inicio, PDV, Estoque e
 * Vendas. Cada aba se atualiza quando uma venda e concluida.
 */
public class JanelaPrincipal extends JFrame {

    private final PainelPedidos painelPedidos;
    private final PainelDashboard painelDashboard;
    private final PainelEstoque painelEstoque;
    private final PainelVendas painelVendas;

    public JanelaPrincipal(ContextoAplicacao contexto) {
        super("Sistema Farmacia");

        this.painelDashboard =
                new PainelDashboard(contexto.getDashboardService());
        this.painelEstoque =
                new PainelEstoque(this, contexto.getEstoqueService());
        this.painelVendas =
                new PainelVendas(contexto.getVendaService());

        // Ao salvar um pedido, atualiza dashboard, estoque e financas.
        this.painelPedidos = new PainelPedidos(
                contexto.getEstoqueService(),
                contexto.getVendaService(),
                this::atualizarTudo);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(Estilo.FONTE_SUBTITULO);
        abas.setBackground(Estilo.FUNDO);
        abas.addTab("  Pedidos  ", painelPedidos);
        abas.addTab("  Admin  ", painelEstoque);
        abas.addTab("  Financas  ", painelVendas);
        abas.addTab("  Dashboard  ", painelDashboard);
        abas.setBorder(BorderFactory.createEmptyBorder());

        // Reforca a atualizacao ao navegar entre as abas.
        abas.addChangeListener(e -> atualizarTudo());

        setContentPane(abas);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1024, 640));
        setSize(1120, 720);
        setLocationRelativeTo(null);
    }

    private void atualizarTudo() {
        painelPedidos.recarregarProdutos();
        painelDashboard.recarregar();
        painelEstoque.recarregar();
        painelVendas.recarregar();
    }

    /** Ajustes globais de aparencia aplicados antes de criar a janela. */
    public static void aplicarTema() {
        try {
            UIManager.setLookAndFeel(
                    "javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Apenas a cor de selecao (tabelas/listas) recebe o verde do tema.
            // Evitamos mexer no "nimbusBase" para nao tingir as abas e barras.
            UIManager.put("nimbusSelectionBackground", Estilo.VERDE);
            UIManager.put("nimbusFocus", Estilo.VERDE);
            UIManager.put("Table.alternateRowColor", Estilo.VERDE_CLARO);
            UIManager.getLookAndFeelDefaults()
                    .put("TabbedPane.font", Estilo.FONTE_SUBTITULO);
        } catch (Exception e) {
            // Se o Nimbus nao estiver disponivel, mantem o tema padrao.
        }
    }
}
