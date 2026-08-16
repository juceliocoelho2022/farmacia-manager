package com.farmacia.ui.tabela;

import com.farmacia.model.Venda;
import com.farmacia.util.Moeda;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabela para o historico de vendas.
 */
public class VendaTableModel extends AbstractTableModel {

    private static final DateTimeFormatter DATA_HORA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final String[] COLUNAS = {
            "Pedido", "Data / Hora", "Cliente", "Itens", "Pagamento", "Total"
    };

    private List<Venda> itens = new ArrayList<>();

    public void setItens(List<Venda> itens) {
        this.itens = new ArrayList<>(itens);
        fireTableDataChanged();
    }

    public Venda getItem(int linha) {
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
        Venda v = itens.get(linha);
        return switch (coluna) {
            case 0 -> "#" + v.getId();
            case 1 -> v.getDataHora() == null
                    ? "-" : v.getDataHora().format(DATA_HORA);
            case 2 -> v.getClienteNome() == null
                    || v.getClienteNome().isBlank()
                    ? "-" : v.getClienteNome();
            case 3 -> v.getQuantidadeTotalItens();
            case 4 -> v.getFormaPagamento().getRotulo();
            case 5 -> Moeda.formatar(v.getTotal());
            default -> "";
        };
    }
}
