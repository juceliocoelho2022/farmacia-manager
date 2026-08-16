package com.farmacia.ui;

import com.farmacia.model.Medicamento;
import com.farmacia.service.EstoqueService;
import com.farmacia.ui.tabela.MedicamentoTableModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

/**
 * Tela de gestao de estoque: lista, busca, cadastra, edita e remove produtos.
 */
public class PainelEstoque extends JPanel {

    private final JFrame janela;
    private final EstoqueService estoqueService;
    private final MedicamentoTableModel modelo = new MedicamentoTableModel();
    private final JTable tabela = new JTable(modelo);
    private final JTextField campoBusca = new JTextField(28);

    public PainelEstoque(JFrame janela, EstoqueService estoqueService) {
        this.janela = janela;
        this.estoqueService = estoqueService;

        setLayout(new BorderLayout(0, 12));
        setBackground(Estilo.FUNDO);
        setBorder(Estilo.espacamento(20));

        add(construirTopo(), BorderLayout.NORTH);
        add(construirTabela(), BorderLayout.CENTER);

        recarregar();
    }

    private JPanel construirTopo() {
        JPanel topo = new JPanel(new BorderLayout(12, 12));
        topo.setBackground(Estilo.FUNDO);

        topo.add(Estilo.titulo("Estoque de produtos"), BorderLayout.NORTH);

        JPanel acoes = new JPanel(new BorderLayout(12, 0));
        acoes.setBackground(Estilo.FUNDO);

        campoBusca.setFont(Estilo.FONTE_NORMAL);
        campoBusca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Estilo.CINZA_CLARO, 1, true),
                Estilo.espacamento(8)));
        campoBusca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                recarregar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                recarregar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                recarregar();
            }
        });

        JPanel busca = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        busca.setBackground(Estilo.FUNDO);
        busca.add(campoBusca);
        acoes.add(busca, BorderLayout.WEST);

        JButton novo = Estilo.botaoPrimario("+ Novo produto");
        novo.addActionListener(e -> abrirDialogo(null));
        JButton editar = Estilo.botaoSecundario("Editar");
        editar.addActionListener(e -> editarSelecionado());
        JButton remover = Estilo.botaoPerigo("Remover");
        remover.addActionListener(e -> removerSelecionado());

        JPanel botoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botoes.setBackground(Estilo.FUNDO);
        botoes.add(editar);
        botoes.add(remover);
        botoes.add(novo);
        acoes.add(botoes, BorderLayout.EAST);

        topo.add(acoes, BorderLayout.SOUTH);
        return topo;
    }

    private JScrollPane construirTabela() {
        tabela.setFont(Estilo.FONTE_NORMAL);
        tabela.setRowHeight(28);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getTableHeader().setFont(Estilo.FONTE_SUBTITULO);
        tabela.setGridColor(Estilo.CINZA_CLARO);

        // Realca a coluna "Situacao" conforme o estado do produto.
        tabela.getColumnModel().getColumn(6).setCellRenderer(
                new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable t, Object valor, boolean sel,
                            boolean foco, int linha, int col) {
                        Component c = super.getTableCellRendererComponent(
                                t, valor, sel, foco, linha, col);
                        String texto = String.valueOf(valor);
                        if (!sel) {
                            if ("VENCIDO".equals(texto)) {
                                c.setForeground(Estilo.VERMELHO);
                            } else if ("Estoque baixo".equals(texto)) {
                                c.setForeground(Estilo.AMBAR);
                            } else if ("Vence em breve".equals(texto)) {
                                c.setForeground(Estilo.AMBAR);
                            } else {
                                c.setForeground(Estilo.VERDE_ESCURO);
                            }
                        }
                        setHorizontalAlignment(SwingConstants.CENTER);
                        return c;
                    }
                });

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createLineBorder(
                Estilo.CINZA_CLARO, 1, true));
        scroll.getViewport().setBackground(Estilo.BRANCO);
        return scroll;
    }

    /** Recarrega a tabela aplicando o filtro de busca atual. */
    public void recarregar() {
        modelo.setItens(estoqueService.buscar(campoBusca.getText()));
    }

    private void abrirDialogo(Medicamento medicamento) {
        DialogoMedicamento dialogo = new DialogoMedicamento(
                janela, estoqueService, medicamento);
        dialogo.setVisible(true);
        if (dialogo.foiSalvo()) {
            recarregar();
        }
    }

    private Medicamento selecionado() {
        int linha = tabela.getSelectedRow();
        if (linha < 0) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um produto na lista.",
                    "Nenhum produto selecionado",
                    JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        return modelo.getItem(tabela.convertRowIndexToModel(linha));
    }

    private void editarSelecionado() {
        Medicamento m = selecionado();
        if (m != null) {
            abrirDialogo(m);
        }
    }

    private void removerSelecionado() {
        Medicamento m = selecionado();
        if (m == null) {
            return;
        }
        int opcao = JOptionPane.showConfirmDialog(this,
                "Remover o produto \"" + m.getNome() + "\"?",
                "Confirmar remocao",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (opcao == JOptionPane.YES_OPTION) {
            estoqueService.remover(m.getId());
            recarregar();
        }
    }
}
