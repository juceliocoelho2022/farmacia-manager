package com.farmacia.ui.tabela;

import com.farmacia.model.Medicamento;
import com.farmacia.util.Moeda;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabela para exibir a lista de produtos no estoque.
 */
public class MedicamentoTableModel extends AbstractTableModel {

    private static final DateTimeFormatter DATA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final String[] COLUNAS = {
            "Produto", "Categoria", "Estoque", "Min.",
            "Preco", "Validade", "Situacao"
    };

    private List<Medicamento> itens = new ArrayList<>();

    public void setItens(List<Medicamento> itens) {
        this.itens = new ArrayList<>(itens);
        fireTableDataChanged();
    }

    public Medicamento getItem(int linha) {
        return itens.get(linha);
    }

    @Override
    public int getRowCount() {
        return itens.size();
    }

    @Override
    public int getColumnCount() {
        return COLUNAS.length;
    }

    @Override
    public String getColumnName(int coluna) {
        return COLUNAS[coluna];
    }

    @Override
    public Object getValueAt(int linha, int coluna) {
        Medicamento m = itens.get(linha);
        return switch (coluna) {
            case 0 -> m.getNome();
            case 1 -> m.getCategoria().getRotulo();
            case 2 -> m.getQuantidadeEstoque();
            case 3 -> m.getEstoqueMinimo();
            case 4 -> Moeda.formatar(m.getPrecoVenda());
            case 5 -> m.getDataValidade() == null
                    ? "-" : m.getDataValidade().format(DATA);
            case 6 -> situacao(m);
            default -> "";
        };
    }

    private String situacao(Medicamento m) {
        LocalDate hoje = LocalDate.now();
        if (m.isVencido(hoje)) {
            return "VENCIDO";
        }
        if (m.isEstoqueBaixo()) {
            return "Estoque baixo";
        }
        if (m.isProximoDoVencimento(hoje, 30)) {
            return "Vence em breve";
        }
        return "OK";
    }
}
