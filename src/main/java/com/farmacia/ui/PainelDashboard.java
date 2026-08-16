package com.farmacia.ui;

import com.farmacia.model.ResumoDashboard;
import com.farmacia.service.DashboardService;
import com.farmacia.util.Moeda;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

/**
 * Tela inicial com um panorama do negocio: produtos, alertas e vendas do dia.
 */
public class PainelDashboard extends JPanel {

    private final DashboardService dashboardService;
    private final JPanel grade = new JPanel(new GridLayout(2, 3, 16, 16));

    public PainelDashboard(DashboardService dashboardService) {
        this.dashboardService = dashboardService;

        setLayout(new BorderLayout(0, 16));
        setBackground(Estilo.FUNDO);
        setBorder(Estilo.espacamento(20));

        add(construirTopo(), BorderLayout.NORTH);

        grade.setBackground(Estilo.FUNDO);
        JPanel envelope = new JPanel(new BorderLayout());
        envelope.setBackground(Estilo.FUNDO);
        envelope.add(grade, BorderLayout.NORTH);
        add(envelope, BorderLayout.CENTER);

        recarregar();
    }

    private JPanel construirTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Estilo.FUNDO);

        JPanel titulos = new JPanel();
        titulos.setLayout(new BoxLayout(titulos, BoxLayout.Y_AXIS));
        titulos.setBackground(Estilo.FUNDO);
        JLabel titulo = Estilo.titulo("Bem-vindo(a) ao Sistema Farmacia");
        JLabel subtitulo = new JLabel(
                "Resumo do dia e alertas do estoque");
        subtitulo.setFont(Estilo.FONTE_NORMAL);
        subtitulo.setForeground(Estilo.CINZA);
        titulos.add(titulo);
        titulos.add(subtitulo);
        topo.add(titulos, BorderLayout.WEST);

        JButton atualizar = Estilo.botaoSecundario("Atualizar");
        atualizar.addActionListener(e -> recarregar());
        JPanel dir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        dir.setBackground(Estilo.FUNDO);
        dir.add(atualizar);
        topo.add(dir, BorderLayout.EAST);
        return topo;
    }

    /** Recalcula os indicadores e redesenha os cartoes. */
    public void recarregar() {
        ResumoDashboard r = dashboardService.gerarResumo();
        grade.removeAll();

        grade.add(cartao("Vendas hoje",
                String.valueOf(r.vendasHoje()),
                "Faturamento: " + Moeda.formatar(r.faturamentoHoje()),
                Estilo.VERDE));
        grade.add(cartao("Produtos cadastrados",
                String.valueOf(r.totalProdutos()),
                "Valor em estoque: " + Moeda.formatar(r.valorTotalEstoque()),
                Estilo.VERDE_ESCURO));
        grade.add(cartao("Faturamento do dia",
                Moeda.formatar(r.faturamentoHoje()),
                "Total das vendas de hoje",
                Estilo.VERDE));
        grade.add(cartao("Estoque baixo",
                String.valueOf(r.produtosEstoqueBaixo()),
                "Produtos no minimo ou abaixo",
                r.produtosEstoqueBaixo() > 0 ? Estilo.AMBAR : Estilo.CINZA));
        grade.add(cartao("Vencendo em breve",
                String.valueOf(r.produtosVencendo()),
                "Proximos 30 dias",
                r.produtosVencendo() > 0 ? Estilo.AMBAR : Estilo.CINZA));
        grade.add(cartao("Produtos vencidos",
                String.valueOf(r.produtosVencidos()),
                "Retire da prateleira",
                r.produtosVencidos() > 0 ? Estilo.VERMELHO : Estilo.CINZA));

        grade.revalidate();
        grade.repaint();
    }

    private JPanel cartao(String titulo, String valor,
                          String rodape, Color destaque) {
        JPanel cartao = new JPanel(new BorderLayout(0, 6));
        cartao.setBackground(Estilo.BRANCO);
        cartao.setPreferredSize(new Dimension(220, 130));
        cartao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 5, 0, 0, destaque),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Estilo.CINZA_CLARO, 1, true),
                        Estilo.espacamento(16))));

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Estilo.FONTE_NORMAL);
        lblTitulo.setForeground(Estilo.CINZA);

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(Estilo.FONTE_GRANDE_NUMERO);
        lblValor.setForeground(destaque);

        JLabel lblRodape = new JLabel(rodape);
        lblRodape.setFont(Estilo.FONTE_PEQUENA);
        lblRodape.setForeground(Estilo.CINZA);

        cartao.add(lblTitulo, BorderLayout.NORTH);
        cartao.add(lblValor, BorderLayout.CENTER);
        cartao.add(lblRodape, BorderLayout.SOUTH);
        cartao.setAlignmentX(Component.LEFT_ALIGNMENT);
        return cartao;
    }
}
