package com.farmacia.ui.tabela;

import com.farmacia.model.ItemVenda;
import com.farmacia.util.Moeda;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Modelo de tabela do carrinho do PDV, com os itens da venda em andamento.
 */
public class ItemVendaTableModel extends AbstractTableModel {

    private static final String[] COLUNAS = {
            "Produto", "Qtd", "Preco Un.", "Subtotal"
    };

    private final List<ItemVenda> itens = new ArrayList<>();

    public void adicionar(ItemVenda item) {
        itens.add(item);
        fireTableDataChanged();
    }

    public void remover(int linha) {
        itens.remove(linha);
        fireTableDataChanged();
    }

    public void substituir(int linha, ItemVenda item) {
        itens.set(linha, item);
        fireTableDataChanged();
    }

    public ItemVenda getItem(int linha) {
        return itens.get(linha);
    }

    public void limpar() {
        itens.clear();
        fireTableDataChanged();
    }

    public boolean isVazio() {
        return itens.isEmpty();
    }

    public List<ItemVenda> getItens() {
        return new ArrayList<>(itens);
    }

    public BigDecimal getTotal() {
        BigDecimal total = BigDecimal.ZERO;
        for (ItemVenda item : itens) {
            total = total.add(item.getSubtotal());
        }
        return total;
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
        ItemVenda item = itens.get(linha);
        return switch (coluna) {
            case 0 -> item.getDescricao();
            case 1 -> item.getQuantidade();
            case 2 -> Moeda.formatar(item.getPrecoUnitario());
            case 3 -> Moeda.formatar(item.getSubtotal());
            default -> "";
        };
    }
}
