package com.farmacia.ui;

import com.farmacia.model.ItemVenda;
import com.farmacia.model.Venda;
import com.farmacia.service.VendaService;
import com.farmacia.ui.tabela.VendaTableModel;
import com.farmacia.util.Moeda;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

/**
 * Historico de vendas realizadas, com total faturado e detalhamento por venda.
 */
public class PainelVendas extends JPanel {

    private final VendaService vendaService;
    private final VendaTableModel modelo = new VendaTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JLabel labelResumo = new JLabel();

    public PainelVendas(VendaService vendaService) {
        this.vendaService = vendaService;

        setLayout(new BorderLayout(0, 12));
        setBackground(Estilo.FUNDO);
        setBorder(Estilo.espacamento(20));

        add(construirTopo(), BorderLayout.NORTH);
        add(construirTabela(), BorderLayout.CENTER);
        add(labelResumo, BorderLayout.SOUTH);

        recarregar();
    }

    private JPanel construirTopo() {
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(Estilo.FUNDO);
        topo.add(Estilo.titulo("Historico de vendas"), BorderLayout.WEST);

        JButton atualizar = Estilo.botaoSecundario("Atualizar");
        atualizar.addActionListener(e -> recarregar());
        JPanel dir = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        dir.setBackground(Estilo.FUNDO);
        dir.add(atualizar);
        topo.add(dir, BorderLayout.EAST);
        return topo;
    }

    private JScrollPane construirTabela() {
        tabela.setFont(Estilo.FONTE_NORMAL);
        tabela.setRowHeight(28);
        tabela.getTableHeader().setFont(Estilo.FONTE_SUBTITULO);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setGridColor(Estilo.CINZA_CLARO);
        tabela.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    detalharSelecionada();
                }
            }
        });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(
                Estilo.CINZA_CLARO, 1, true));
        scroll.getViewport().setBackground(Estilo.BRANCO);
        return scroll;
    }

    /** Recarrega a lista de vendas e recalcula o total faturado. */
    public void recarregar() {
        List<Venda> vendas = vendaService.listarTodas();
        modelo.setItens(vendas);

        BigDecimal total = BigDecimal.ZERO;
        for (Venda v : vendas) {
            total = total.add(v.getTotal());
        }
        labelResumo.setFont(Estilo.FONTE_SUBTITULO);
        labelResumo.setForeground(Estilo.VERDE_ESCURO);
        labelResumo.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 0));
        labelResumo.setText(vendas.size() + " venda(s)  |  Total faturado: "
                + Moeda.formatar(total));
    }

    private void detalharSelecionada() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            return;
        }
        Venda v = modelo.getItem(tabela.convertRowIndexToModel(linha));
        StringBuilder sb = new StringBuilder();
        sb.append("Pedido #").append(v.getId()).append('\n');
        if (v.getClienteNome() != null && !v.getClienteNome().isBlank()) {
            sb.append("Cliente: ").append(v.getClienteNome()).append('\n');
        }
        if (v.getClienteTelefone() != null
                && !v.getClienteTelefone().isBlank()) {
            sb.append("Telefone: ")
                    .append(v.getClienteTelefone()).append('\n');
        }
        if (v.getClienteEndereco() != null
                && !v.getClienteEndereco().isBlank()) {
            sb.append("Endereco: ")
                    .append(v.getClienteEndereco()).append('\n');
        }
        sb.append("Pagamento: ")
                .append(v.getFormaPagamento().getRotulo()).append("\n\n");
        for (ItemVenda item : v.getItens()) {
            sb.append(item.getQuantidade()).append("x ")
                    .append(item.getDescricao())
                    .append("  ")
                    .append(Moeda.formatar(item.getSubtotal()))
                    .append('\n');
        }
        sb.append("\nSubtotal: ").append(Moeda.formatar(v.getSubtotal()));
        sb.append("\nDesconto: ").append(Moeda.formatar(v.getDesconto()));
        sb.append("\nTaxa entrega: ")
                .append(Moeda.formatar(v.getTaxaEntrega()));
        sb.append("\nTotal: ").append(Moeda.formatar(v.getTotal()));

        JOptionPane.showMessageDialog(this, sb.toString(),
                "Detalhes da venda", JOptionPane.INFORMATION_MESSAGE);
    }
}
