package com.farmacia;

import com.farmacia.model.Categoria;
import com.farmacia.model.Medicamento;
import com.farmacia.service.EstoqueService;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Cadastra um catalogo de exemplo na primeira execucao, para que a farmacia
 * ja abra o sistema com produtos e possa testar o PDV imediatamente.
 */
final class SeedDados {

    private SeedDados() {
    }

    static void popular(EstoqueService estoque) {
        LocalDate hoje = LocalDate.now();

        criar(estoque, "Dipirona 500mg 10cp", Categoria.MEDICAMENTO,
                "Neo Quimica", "7891058001019",
                "3.20", "8.90", 120, 20,
                hoje.plusMonths(14), false);

        criar(estoque, "Paracetamol 750mg 20cp", Categoria.MEDICAMENTO,
                "EMS", "7896004703021",
                "4.10", "11.50", 80, 15,
                hoje.plusMonths(10), false);

        criar(estoque, "Amoxicilina 500mg 21cp", Categoria.ANTIBIOTICO,
                "Medley", "7896422500012",
                "9.80", "24.90", 12, 10,
                hoje.plusMonths(8), true);

        criar(estoque, "Ibuprofeno 400mg 10cp", Categoria.GENERICO,
                "Germed", "7896714201014",
                "3.90", "9.70", 60, 15,
                hoje.plusMonths(16), false);

        criar(estoque, "Omeprazol 20mg 28cp", Categoria.GENERICO,
                "Eurofarma", "7896026303011",
                "6.50", "15.80", 45, 12,
                hoje.plusMonths(20), false);

        criar(estoque, "Vitamina C 1g 10cp eferv.", Categoria.SUPLEMENTO,
                "Redoxon", "7891142177018",
                "12.00", "28.50", 30, 10,
                hoje.plusMonths(11), false);

        criar(estoque, "Alcool em Gel 70% 500ml", Categoria.HIGIENE,
                "Antisseptico", "7898900112233",
                "5.20", "12.90", 8, 12,
                hoje.plusMonths(24), false);

        criar(estoque, "Soro Fisiologico 500ml", Categoria.PRIMEIROS_SOCORROS,
                "Farmax", "7896523201014",
                "3.00", "7.50", 40, 10,
                hoje.plusDays(20), false);

        criar(estoque, "Fralda Infantil M 30un", Categoria.INFANTIL,
                "Pompom", "7896007540019",
                "22.00", "39.90", 18, 8,
                null, false);

        criar(estoque, "Protetor Solar FPS 50", Categoria.COSMETICO,
                "Sundown", "7509552810011",
                "18.90", "42.00", 5, 6,
                hoje.plusMonths(9), false);
    }

    private static void criar(EstoqueService estoque,
                              String nome,
                              Categoria categoria,
                              String fabricante,
                              String codigoBarras,
                              String precoCusto,
                              String precoVenda,
                              int quantidade,
                              int estoqueMinimo,
                              LocalDate validade,
                              boolean requerReceita) {
        Medicamento m = new Medicamento();
        m.setNome(nome);
        m.setCategoria(categoria);
        m.setFabricante(fabricante);
        m.setCodigoBarras(codigoBarras);
        m.setPrecoCusto(new BigDecimal(precoCusto));
        m.setPrecoVenda(new BigDecimal(precoVenda));
        m.setQuantidadeEstoque(quantidade);
        m.setEstoqueMinimo(estoqueMinimo);
        m.setDataValidade(validade);
        m.setRequerReceita(requerReceita);
        estoque.salvar(m);
    }
}
