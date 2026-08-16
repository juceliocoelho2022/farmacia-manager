package com.farmacia.repository.memoria;

import com.farmacia.model.Medicamento;
import com.farmacia.repository.MedicamentoRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Implementacao em memoria do {@link MedicamentoRepository}.
 *
 * Serve de base para a versao persistida em arquivo e e usada diretamente
 * nos testes de unidade, evitando acesso a disco.
 */
public class MedicamentoRepositoryMemoria implements MedicamentoRepository {

    protected final List<Medicamento> dados = new ArrayList<>();
    private long proximoId = 1;

    @Override
    public synchronized Medicamento salvar(Medicamento medicamento) {
        if (medicamento.getId() == 0) {
            medicamento.setId(proximoId++);
            dados.add(medicamento);
        } else {
            int indice = indiceDoId(medicamento.getId());
            if (indice >= 0) {
                dados.set(indice, medicamento);
            } else {
                dados.add(medicamento);
            }
            proximoId = Math.max(proximoId, medicamento.getId() + 1);
        }
        return medicamento;
    }

    @Override
    public synchronized Optional<Medicamento> buscarPorId(long id) {
        return dados.stream()
                .filter(m -> m.getId() == id)
                .findFirst();
    }

    @Override
    public synchronized Optional<Medicamento> buscarPorCodigoBarras(
            String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            return Optional.empty();
        }
        return dados.stream()
                .filter(m -> codigoBarras.equals(m.getCodigoBarras()))
                .findFirst();
    }

    @Override
    public synchronized List<Medicamento> listarTodos() {
        List<Medicamento> copia = new ArrayList<>(dados);
        copia.sort(Comparator.comparing(
                Medicamento::getNome,
                Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
        return copia;
    }

    @Override
    public synchronized List<Medicamento> buscar(String termo) {
        if (termo == null || termo.isBlank()) {
            return listarTodos();
        }
        String alvo = termo.toLowerCase(Locale.ROOT).trim();
        List<Medicamento> resultado = new ArrayList<>();
        for (Medicamento m : listarTodos()) {
            if (contem(m.getNome(), alvo)
                    || contem(m.getFabricante(), alvo)
                    || contem(m.getCodigoBarras(), alvo)) {
                resultado.add(m);
            }
        }
        return resultado;
    }

    @Override
    public synchronized void remover(long id) {
        dados.removeIf(m -> m.getId() == id);
    }

    private int indiceDoId(long id) {
        for (int i = 0; i < dados.size(); i++) {
            if (dados.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private static boolean contem(String valor, String alvo) {
        return valor != null
                && valor.toLowerCase(Locale.ROOT).contains(alvo);
    }

    /** Recalcula o proximo id a partir dos dados carregados do disco. */
    protected synchronized void recalcularProximoId() {
        long maior = 0;
        for (Medicamento m : dados) {
            maior = Math.max(maior, m.getId());
        }
        proximoId = maior + 1;
    }
}
