package com.farmacia.ui;

import com.farmacia.model.FormaPagamento;
import com.farmacia.model.ItemVenda;
import com.farmacia.model.Medicamento;
import com.farmacia.model.Venda;
import com.farmacia.service.EstoqueService;
import com.farmacia.service.VendaService;
import com.farmacia.ui.tabela.ItemVendaTableModel;
import com.farmacia.util.Moeda;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.List;

/**
 * Tela de Pedidos: dados do cliente, adicao de produtos, lista do pedido e
 * finalizacao com taxa de entrega. Ao salvar, da baixa no estoque.
 */
public class PainelPedidos extends JPanel {

    private final EstoqueService estoqueService;
    private final VendaService vendaService;
    private final Runnable aoSalvarPedido;

    // Dados do cliente
    private final JTextField campoNome = new JTextField(22);
    private final JTextField campoTelefone = new JTextField(16);
    private final JTextField campoEndereco = new JTextField(28);
    private final JTextField campoTaxa = new JTextField("0,00", 8);

    // Adicionar produtos
    private final JComboBox<Medicamento> comboProduto = new JComboBox<>();
    private final JSpinner spinnerQtd =
            new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

    // Itens do pedido
    private final ItemVendaTableModel itensModel = new ItemVendaTableModel();
    private final JTable tabelaItens = new JTable(itensModel);
    private final JComboBox<FormaPagamento> comboPagamento =
            new JComboBox<>(FormaPagamento.values());

    // Totais
    private final JLabel labelSubtotal = new JLabel();
    private final JLabel labelTotal = new JLabel();

    public PainelPedidos(EstoqueService estoqueService,
                         VendaService vendaService,
                         Runnable aoSalvarPedido) {
        this.estoqueService = estoqueService;
        this.vendaService = vendaService;
        this.aoSalvarPedido = aoSalvarPedido;

        setLayout(new BorderLayout(0, 12));
        setBackground(Estilo.FUNDO);
        setBorder(Estilo.espacamento(16));

        JPanel corpo = new JPanel(new BorderLayout(0, 12));
        corpo.setBackground(Estilo.FUNDO);
        corpo.add(construirDadosCliente(), BorderLayout.NORTH);
        corpo.add(construirAdicionarProdutos(), BorderLayout.CENTER);

        add(corpo, BorderLayout.CENTER);
        add(construirRodape(), BorderLayout.SOUTH);

        recarregarProdutos();
        atualizarTotais();
    }

    // ------------------------------------------------------------------
    // Dados do cliente
    // ------------------------------------------------------------------

    private JPanel construirDadosCliente() {
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(Estilo.BRANCO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                Estilo.secao("Dados do Cliente"),
                Estilo.espacamento(12)));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 8, 6, 8);
        g.anchor = GridBagConstraints.WEST;

        estilizar(campoNome);
        estilizar(campoTelefone);
        estilizar(campoEndereco);
        estilizar(campoTaxa);
        campoTaxa.getDocument().addDocumentListener(
                (SimpleDoc) e -> atualizarTotais());

        g.gridx = 0;
        g.gridy = 0;
        painel.add(Estilo.rotulo("Nome:"), g);
        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        painel.add(campoNome, g);

        g.gridx = 2;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        painel.add(Estilo.rotulo("Telefone:"), g);
        g.gridx = 3;
        painel.add(campoTelefone, g);

        g.gridx = 0;
        g.gridy = 1;
        painel.add(Estilo.rotulo("Endereco:"), g);
        g.gridx = 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        painel.add(campoEndereco, g);

        g.gridx = 2;
        g.weightx = 0;
        g.fill = GridBagConstraints.NONE;
        painel.add(Estilo.rotulo("Taxa Entrega (R$):"), g);
        g.gridx = 3;
        painel.add(campoTaxa, g);

        return painel;
    }

    // ------------------------------------------------------------------
    // Adicionar produtos + lista de itens
    // ------------------------------------------------------------------

    private JPanel construirAdicionarProdutos() {
        JPanel painel = new JPanel(new BorderLayout(0, 10));
        painel.setBackground(Estilo.BRANCO);
        painel.setBorder(BorderFactory.createCompoundBorder(
                Estilo.secao("Adicionar Produtos"),
                Estilo.espacamento(12)));

        // Linha de selecao de produto
        JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        linha.setBackground(Estilo.BRANCO);

        comboProduto.setFont(Estilo.FONTE_NORMAL);
        comboProduto.setPreferredSize(new Dimension(300, 32));
        comboProduto.setRenderer(new RenderizadorProduto());

        spinnerQtd.setPreferredSize(new Dimension(70, 32));

        JButton adicionar = Estilo.botaoPrimario("Adicionar");
        adicionar.addActionListener(e -> adicionarProduto());

        linha.add(Estilo.rotulo("Produto:"));
        linha.add(comboProduto);
        linha.add(Estilo.rotulo("Qtd:"));
        linha.add(spinnerQtd);
        linha.add(adicionar);

        // Tabela de itens do pedido
        tabelaItens.setFont(Estilo.FONTE_NORMAL);
        tabelaItens.setRowHeight(26);
        tabelaItens.getTableHeader().setFont(Estilo.FONTE_PEQUENA);
        tabelaItens.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaItens.setGridColor(Estilo.CINZA_CLARO);
        JScrollPane scroll = new JScrollPane(tabelaItens);
        scroll.setBorder(BorderFactory.createLineBorder(
                Estilo.CINZA_CLARO, 1, true));
        scroll.setPreferredSize(new Dimension(100, 220));

        // Botoes editar / remover
        JButton editar = Estilo.botaoSecundario("Editar Item Selecionado");
        editar.addActionListener(e -> editarItem());
        JButton remover = Estilo.botaoSecundario("Remover Item Selecionado");
        remover.addActionListener(e -> removerItem());

        JPanel acoesItens = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        acoesItens.setBackground(Estilo.BRANCO);
        acoesItens.add(editar);
        acoesItens.add(remover);

        painel.add(linha, BorderLayout.NORTH);
        painel.add(scroll, BorderLayout.CENTER);
        painel.add(acoesItens, BorderLayout.SOUTH);
        return painel;
    }

    // ------------------------------------------------------------------
    // Rodape: totais + pagamento + salvar/limpar
    // ------------------------------------------------------------------

    private JPanel construirRodape() {
        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setBackground(Estilo.FUNDO);
        painel.setBorder(BorderFactory.createEmptyBorder(4, 4, 0, 4));

        JPanel totais = new JPanel(new GridBagLayout());
        totais.setBackground(Estilo.FUNDO);
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(2, 4, 2, 24);
        g.anchor = GridBagConstraints.WEST;

        JLabel tSub = new JLabel("Subtotal:");
        tSub.setFont(Estilo.FONTE_SUBTITULO);
        labelSubtotal.setFont(Estilo.FONTE_SUBTITULO);

        JLabel tTot = new JLabel("Total (com taxa):");
        tTot.setFont(Estilo.FONTE_SUBTITULO);
        labelTotal.setFont(Estilo.FONTE_TITULO);
        labelTotal.setForeground(Estilo.LARANJA);

        g.gridx = 0;
        g.gridy = 0;
        totais.add(tSub, g);
        g.gridx = 1;
        totais.add(labelSubtotal, g);

        g.gridx = 0;
        g.gridy = 1;
        totais.add(tTot, g);
        g.gridx = 1;
        totais.add(labelTotal, g);

        JPanel acoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        acoes.setBackground(Estilo.FUNDO);
        comboPagamento.setFont(Estilo.FONTE_NORMAL);
        JButton salvar = Estilo.botaoPrimario("Salvar Pedido");
        salvar.addActionListener(e -> salvarPedido());
        JButton limpar = Estilo.botaoSecundario("Limpar");
        limpar.addActionListener(e -> limpar());
        acoes.add(Estilo.rotulo("Pagamento:"));
        acoes.add(comboPagamento);
        acoes.add(limpar);
        acoes.add(salvar);

        painel.add(totais, BorderLayout.WEST);
        painel.add(acoes, BorderLayout.EAST);
        return painel;
    }

    // ------------------------------------------------------------------
    // Acoes
    // ------------------------------------------------------------------

    /** Recarrega a lista de produtos do combo (apos vendas/alteracoes). */
    public void recarregarProdutos() {
        Medicamento selecionado = (Medicamento) comboProduto.getSelectedItem();
        List<Medicamento> produtos = estoqueService.listarTodos();
        DefaultComboBoxModel<Medicamento> modelo =
                new DefaultComboBoxModel<>();
        for (Medicamento m : produtos) {
            modelo.addElement(m);
        }
        comboProduto.setModel(modelo);
        if (selecionado != null) {
            comboProduto.setSelectedItem(selecionado);
        }
    }

    private void adicionarProduto() {
        Medicamento m = (Medicamento) comboProduto.getSelectedItem();
        if (m == null) {
            return;
        }
        int qtd = (Integer) spinnerQtd.getValue();
        int jaNoPedido = quantidadeNoPedido(m.getId());
        if (jaNoPedido + qtd > m.getQuantidadeEstoque()) {
            JOptionPane.showMessageDialog(this,
                    "Estoque insuficiente para '" + m.getNome()
                            + "'.\nDisponivel: " + m.getQuantidadeEstoque()
                            + " | Ja no pedido: " + jaNoPedido,
                    "Estoque insuficiente",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        itensModel.adicionar(new ItemVenda(
                m.getId(), m.getNome(), m.getPrecoVenda(), qtd));
        atualizarTotais();
    }

    private int quantidadeNoPedido(long medicamentoId) {
        int total = 0;
        for (ItemVenda item : itensModel.getItens()) {
            if (item.getMedicamentoId() == medicamentoId) {
                total += item.getQuantidade();
            }
        }
        return total;
    }

    private void editarItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um item na lista.",
                    "Nenhum item selecionado",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        ItemVenda atual = itensModel.getItem(linha);
        String resposta = JOptionPane.showInputDialog(this,
                "Nova quantidade para \"" + atual.getDescricao() + "\":",
                atual.getQuantidade());
        if (resposta == null) {
            return;
        }
        int nova;
        try {
            nova = Integer.parseInt(resposta.trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Quantidade invalida.",
                    "Valor invalido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (nova <= 0) {
            itensModel.remover(linha);
            atualizarTotais();
            return;
        }
        int outros = quantidadeNoPedido(atual.getMedicamentoId())
                - atual.getQuantidade();
        int disponivel = estoqueService.buscarPorId(atual.getMedicamentoId())
                .map(Medicamento::getQuantidadeEstoque).orElse(0);
        if (outros + nova > disponivel) {
            JOptionPane.showMessageDialog(this,
                    "Estoque insuficiente. Disponivel: " + disponivel,
                    "Estoque insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }
        itensModel.substituir(linha, new ItemVenda(
                atual.getMedicamentoId(), atual.getDescricao(),
                atual.getPrecoUnitario(), nova));
        atualizarTotais();
    }

    private void removerItem() {
        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            return;
        }
        itensModel.remover(linha);
        atualizarTotais();
    }

    private BigDecimal lerTaxa() {
        try {
            return Moeda.converter(campoTaxa.getText());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private void atualizarTotais() {
        BigDecimal subtotal = itensModel.getTotal();
        BigDecimal total = subtotal.add(lerTaxa());
        labelSubtotal.setText(Moeda.formatar(subtotal));
        labelTotal.setText(Moeda.formatar(total));
    }

    private void salvarPedido() {
        if (itensModel.isVazio()) {
            JOptionPane.showMessageDialog(this,
                    "Adicione ao menos um produto ao pedido.",
                    "Pedido vazio", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            Venda venda = new Venda();
            venda.setClienteNome(campoNome.getText().trim());
            venda.setClienteTelefone(campoTelefone.getText().trim());
            venda.setClienteEndereco(campoEndereco.getText().trim());
            venda.setTaxaEntrega(lerTaxa());
            venda.setFormaPagamento(
                    (FormaPagamento) comboPagamento.getSelectedItem());
            for (ItemVenda item : itensModel.getItens()) {
                venda.adicionarItem(item);
            }

            Venda registrada = vendaService.registrarVenda(venda);

            JOptionPane.showMessageDialog(this,
                    "Pedido #" + registrada.getId() + " salvo!\n"
                            + "Total: " + Moeda.formatar(registrada.getTotal())
                            + "\nPagamento: "
                            + registrada.getFormaPagamento().getRotulo(),
                    "Pedido salvo", JOptionPane.INFORMATION_MESSAGE);

            limpar();
            recarregarProdutos();
            if (aoSalvarPedido != null) {
                aoSalvarPedido.run();
            }
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Nao foi possivel salvar o pedido",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpar() {
        campoNome.setText("");
        campoTelefone.setText("");
        campoEndereco.setText("");
        campoTaxa.setText("0,00");
        itensModel.limpar();
        atualizarTotais();
    }

    private static void estilizar(JTextField campo) {
        campo.setFont(Estilo.FONTE_NORMAL);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.CINZA_CLARO, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)));
    }

    /** Mostra "nome (R$ preco) - estoque: n" em cada item do combo. */
    private static class RenderizadorProduto
            extends javax.swing.DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> lista, Object valor, int indice,
                boolean selecionado, boolean foco) {
            super.getListCellRendererComponent(
                    lista, valor, indice, selecionado, foco);
            if (valor instanceof Medicamento m) {
                setText(m.getNome() + "  ("
                        + Moeda.formatar(m.getPrecoVenda())
                        + ")  - estoque: " + m.getQuantidadeEstoque());
            }
            return this;
        }
    }

    /** Adaptador funcional para DocumentListener (evita repetir 3 metodos). */
    @FunctionalInterface
    private interface SimpleDoc extends DocumentListener {
        void update(DocumentEvent e);

        @Override
        default void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        default void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }
}
