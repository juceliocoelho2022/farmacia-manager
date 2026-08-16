package com.farmacia.service;

import com.farmacia.model.Categoria;
import com.farmacia.model.FormaPagamento;
import com.farmacia.model.ItemVenda;
import com.farmacia.model.Medicamento;
import com.farmacia.model.Venda;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.VendaRepository;
import com.farmacia.repository.memoria.MedicamentoRepositoryMemoria;
import com.farmacia.repository.memoria.VendaRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VendaServiceTest {

    private MedicamentoRepository medicamentoRepository;
    private VendaRepository vendaRepository;
    private EstoqueService estoqueService;
    private VendaService vendaService;

    @BeforeEach
    void setUp() {
        medicamentoRepository = new MedicamentoRepositoryMemoria();
        vendaRepository = new VendaRepositoryMemoria();
        estoqueService = new EstoqueService(medicamentoRepository);
        vendaService = new VendaService(vendaRepository, estoqueService);
    }

    private Medicamento cadastrar(String nome, String preco, int qtd) {
        Medicamento m = new Medicamento(
                nome, Categoria.MEDICAMENTO, new BigDecimal(preco), qtd);
        return estoqueService.salvar(m);
    }

    @Test
    void registrarVendaDaBaixaNoEstoqueECalculaTotal() {
        Medicamento a = cadastrar("Dipirona", "8.90", 10);
        Medicamento b = cadastrar("Paracetamol", "11.50", 10);

        Venda venda = new Venda();
        venda.setFormaPagamento(FormaPagamento.PIX);
        venda.adicionarItem(new ItemVenda(
                a.getId(), a.getNome(), a.getPrecoVenda(), 2));
        venda.adicionarItem(new ItemVenda(
                b.getId(), b.getNome(), b.getPrecoVenda(), 1));

        Venda registrada = vendaService.registrarVenda(venda);

        // 2 * 8.90 + 1 * 11.50 = 29.30
        assertEquals(new BigDecimal("29.30"), registrada.getTotal());
        assertTrue(registrada.getId() > 0);
        assertEquals(8, estoqueService.buscarPorId(a.getId())
                .orElseThrow().getQuantidadeEstoque());
        assertEquals(9, estoqueService.buscarPorId(b.getId())
                .orElseThrow().getQuantidadeEstoque());
        assertEquals(1, vendaService.listarTodas().size());
    }

    @Test
    void descontoReduzOTotal() {
        Medicamento a = cadastrar("Dipirona", "10.00", 10);

        Venda venda = new Venda();
        venda.adicionarItem(new ItemVenda(
                a.getId(), a.getNome(), a.getPrecoVenda(), 3));
        venda.setDesconto(new BigDecimal("5.00"));

        Venda registrada = vendaService.registrarVenda(venda);

        assertEquals(new BigDecimal("25.00"), registrada.getTotal());
    }

    @Test
    void vendaSemItensFalha() {
        Venda venda = new Venda();

        assertThrows(IllegalArgumentException.class,
                () -> vendaService.registrarVenda(venda));
    }

    @Test
    void vendaAcimaDoEstoqueFalhaSemAlterarEstoque() {
        Medicamento a = cadastrar("Dipirona", "8.90", 3);

        Venda venda = new Venda();
        venda.adicionarItem(new ItemVenda(
                a.getId(), a.getNome(), a.getPrecoVenda(), 5));

        assertThrows(EstoqueInsuficienteException.class,
                () -> vendaService.registrarVenda(venda));

        // Estoque permanece intacto.
        assertEquals(3, estoqueService.buscarPorId(a.getId())
                .orElseThrow().getQuantidadeEstoque());
        assertTrue(vendaService.listarTodas().isEmpty());
    }

    @Test
    void itemComProdutoRepetidoSomaAsQuantidades() {
        Medicamento a = cadastrar("Dipirona", "8.90", 5);

        Venda venda = new Venda();
        venda.adicionarItem(new ItemVenda(
                a.getId(), a.getNome(), a.getPrecoVenda(), 2));
        venda.adicionarItem(new ItemVenda(
                a.getId(), a.getNome(), a.getPrecoVenda(), 2));

        vendaService.registrarVenda(venda);

        assertEquals(1, estoqueService.buscarPorId(a.getId())
                .orElseThrow().getQuantidadeEstoque());
    }
}
