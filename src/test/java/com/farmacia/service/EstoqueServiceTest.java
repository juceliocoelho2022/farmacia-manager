package com.farmacia.service;

import com.farmacia.model.Categoria;
import com.farmacia.model.Medicamento;
import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.memoria.MedicamentoRepositoryMemoria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EstoqueServiceTest {

    private MedicamentoRepository repository;
    private EstoqueService service;

    @BeforeEach
    void setUp() {
        repository = new MedicamentoRepositoryMemoria();
        service = new EstoqueService(repository);
    }

    private Medicamento novo(String nome, int qtd, int minimo) {
        Medicamento m = new Medicamento(
                nome, Categoria.MEDICAMENTO, new BigDecimal("10.00"), qtd);
        m.setEstoqueMinimo(minimo);
        return m;
    }

    @Test
    void salvarAtribuiIdESalvaProduto() {
        Medicamento salvo = service.salvar(novo("Dipirona", 10, 5));

        assertTrue(salvo.getId() > 0);
        assertEquals(1, service.listarTodos().size());
    }

    @Test
    void salvarSemNomeFalha() {
        Medicamento m = novo("", 10, 5);

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(m));
    }

    @Test
    void salvarComPrecoNegativoFalha() {
        Medicamento m = novo("Dipirona", 10, 5);
        m.setPrecoVenda(new BigDecimal("-1.00"));

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(m));
    }

    @Test
    void naoPermiteCodigoDeBarrasDuplicado() {
        Medicamento a = novo("Produto A", 10, 5);
        a.setCodigoBarras("123");
        service.salvar(a);

        Medicamento b = novo("Produto B", 5, 2);
        b.setCodigoBarras("123");

        assertThrows(IllegalArgumentException.class,
                () -> service.salvar(b));
    }

    @Test
    void ajustarEstoqueReduzQuantidade() {
        Medicamento salvo = service.salvar(novo("Dipirona", 10, 5));

        Medicamento atualizado = service.ajustarEstoque(salvo.getId(), -4);

        assertEquals(6, atualizado.getQuantidadeEstoque());
    }

    @Test
    void ajustarEstoqueAbaixoDeZeroFalha() {
        Medicamento salvo = service.salvar(novo("Dipirona", 3, 5));

        assertThrows(EstoqueInsuficienteException.class,
                () -> service.ajustarEstoque(salvo.getId(), -5));
    }

    @Test
    void listarEstoqueBaixoConsideraApenasAbaixoDoMinimo() {
        service.salvar(novo("Baixo", 5, 5));
        service.salvar(novo("Ok", 20, 5));

        var baixo = service.listarEstoqueBaixo();

        assertEquals(1, baixo.size());
        assertEquals("Baixo", baixo.get(0).getNome());
    }

    @Test
    void listarVencidosEProximosDoVencimento() {
        Medicamento vencido = novo("Vencido", 10, 5);
        vencido.setDataValidade(LocalDate.now().minusDays(1));
        service.salvar(vencido);

        Medicamento vencendo = novo("Vencendo", 10, 5);
        vencendo.setDataValidade(LocalDate.now().plusDays(10));
        service.salvar(vencendo);

        Medicamento tranquilo = novo("Tranquilo", 10, 5);
        tranquilo.setDataValidade(LocalDate.now().plusYears(1));
        service.salvar(tranquilo);

        assertEquals(1, service.listarVencidos().size());
        assertEquals(1, service.listarProximosDoVencimento().size());
        assertFalse(service.listarProximosDoVencimento().isEmpty());
    }
}
