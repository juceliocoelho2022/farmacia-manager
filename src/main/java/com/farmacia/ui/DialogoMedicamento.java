package com.farmacia.ui;

import com.farmacia.model.Categoria;
import com.farmacia.model.Medicamento;
import com.farmacia.service.EstoqueService;
import com.farmacia.util.Moeda;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Janela modal para cadastrar um novo produto ou editar um existente.
 */
public class DialogoMedicamento extends JDialog {

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EstoqueService estoqueService;
    private final Medicamento medicamento;
    private boolean salvo = false;

    private final JTextField campoNome = new JTextField(24);
    private final JComboBox<Categoria> campoCategoria =
            new JComboBox<>(Categoria.values());
    private final JTextField campoFabricante = new JTextField(24);
    private final JTextField campoCodigo = new JTextField(24);
    private final JTextField campoPrecoCusto = new JTextField(10);
    private final JTextField campoPrecoVenda = new JTextField(10);
    private final JTextField campoQuantidade = new JTextField(10);
    private final JTextField campoMinimo = new JTextField(10);
    private final JTextField campoValidade = new JTextField(10);
    private final JCheckBox campoReceita = new JCheckBox("Exige receita medica");

    public DialogoMedicamento(Frame dono,
                              EstoqueService estoqueService,
                              Medicamento medicamento) {
        super(dono, medicamento == null
                ? "Novo produto" : "Editar produto", true);
        this.estoqueService = estoqueService;
        this.medicamento = medicamento == null
                ? new Medicamento() : medicamento;

        construir();
        preencher();

        pack();
        setMinimumSize(new Dimension(480, getHeight()));
        setLocationRelativeTo(dono);
    }

    public boolean foiSalvo() {
        return salvo;
    }

    private void construir() {
        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Estilo.BRANCO);
        formulario.setBorder(Estilo.espacamento(20));

        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(6, 6, 6, 6);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        int linha = 0;
        linha = adicionarLinha(formulario, g, linha, "Nome *", campoNome);
        linha = adicionarLinha(formulario, g, linha, "Categoria", campoCategoria);
        linha = adicionarLinha(formulario, g, linha, "Fabricante", campoFabricante);
        linha = adicionarLinha(formulario, g, linha, "Codigo de barras", campoCodigo);
        linha = adicionarLinha(formulario, g, linha, "Preco de custo", campoPrecoCusto);
        linha = adicionarLinha(formulario, g, linha, "Preco de venda *", campoPrecoVenda);
        linha = adicionarLinha(formulario, g, linha, "Qtd. em estoque", campoQuantidade);
        linha = adicionarLinha(formulario, g, linha, "Estoque minimo", campoMinimo);
        linha = adicionarLinha(formulario, g, linha, "Validade (dd/mm/aaaa)", campoValidade);

        g.gridx = 1;
        g.gridy = linha;
        campoReceita.setBackground(Estilo.BRANCO);
        formulario.add(campoReceita, g);

        JButton salvar = Estilo.botaoPrimario("Salvar");
        salvar.addActionListener(e -> aoSalvar());
        JButton cancelar = Estilo.botaoSecundario("Cancelar");
        cancelar.addActionListener(e -> dispose());

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rodape.setBackground(Estilo.FUNDO);
        rodape.add(cancelar);
        rodape.add(salvar);

        setLayout(new BorderLayout());
        add(formulario, BorderLayout.CENTER);
        add(rodape, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(salvar);
    }

    private int adicionarLinha(JPanel painel,
                               GridBagConstraints g,
                               int linha,
                               String rotulo,
                               JComponent campo) {
        JLabel label = new JLabel(rotulo);
        label.setFont(Estilo.FONTE_PEQUENA);
        label.setForeground(Estilo.CINZA);

        g.gridx = 0;
        g.gridy = linha;
        g.weightx = 0;
        painel.add(label, g);

        g.gridx = 1;
        g.weightx = 1;
        painel.add(campo, g);
        return linha + 1;
    }

    private void preencher() {
        if (medicamento.getNome() != null) {
            campoNome.setText(medicamento.getNome());
        }
        campoCategoria.setSelectedItem(medicamento.getCategoria());
        campoFabricante.setText(nuloParaVazio(medicamento.getFabricante()));
        campoCodigo.setText(nuloParaVazio(medicamento.getCodigoBarras()));
        campoPrecoCusto.setText(formatarValor(medicamento.getPrecoCusto()));
        campoPrecoVenda.setText(formatarValor(medicamento.getPrecoVenda()));
        campoQuantidade.setText(
                String.valueOf(medicamento.getQuantidadeEstoque()));
        campoMinimo.setText(String.valueOf(medicamento.getEstoqueMinimo()));
        if (medicamento.getDataValidade() != null) {
            campoValidade.setText(medicamento.getDataValidade().format(DATA));
        }
        campoReceita.setSelected(medicamento.isRequerReceita());
    }

    private void aoSalvar() {
        try {
            medicamento.setNome(campoNome.getText().trim());
            medicamento.setCategoria(
                    (Categoria) campoCategoria.getSelectedItem());
            medicamento.setFabricante(campoFabricante.getText().trim());
            medicamento.setCodigoBarras(campoCodigo.getText().trim());
            medicamento.setPrecoCusto(Moeda.converter(campoPrecoCusto.getText()));
            medicamento.setPrecoVenda(Moeda.converter(campoPrecoVenda.getText()));
            medicamento.setQuantidadeEstoque(
                    inteiro(campoQuantidade.getText(), "Quantidade em estoque"));
            medicamento.setEstoqueMinimo(
                    inteiro(campoMinimo.getText(), "Estoque minimo"));
            medicamento.setDataValidade(lerData(campoValidade.getText()));
            medicamento.setRequerReceita(campoReceita.isSelected());

            estoqueService.salvar(medicamento);
            salvo = true;
            dispose();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Nao foi possivel salvar",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private static int inteiro(String texto, String campo) {
        String limpo = texto == null ? "" : texto.trim();
        if (limpo.isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(limpo);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Valor invalido em '" + campo + "'");
        }
    }

    private static LocalDate lerData(String texto) {
        String limpo = texto == null ? "" : texto.trim();
        if (limpo.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(limpo, DATA);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Data de validade invalida. Use o formato dd/mm/aaaa");
        }
    }

    private static String formatarValor(BigDecimal valor) {
        return valor == null ? "0,00"
                : valor.toPlainString().replace('.', ',');
    }

    private static String nuloParaVazio(String s) {
        return s == null ? "" : s;
    }
}
