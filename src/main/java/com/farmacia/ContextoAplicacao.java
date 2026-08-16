package com.farmacia;

import com.farmacia.repository.MedicamentoRepository;
import com.farmacia.repository.VendaRepository;
import com.farmacia.repository.arquivo.MedicamentoRepositoryArquivo;
import com.farmacia.repository.arquivo.VendaRepositoryArquivo;
import com.farmacia.service.DashboardService;
import com.farmacia.service.EstoqueService;
import com.farmacia.service.VendaService;

import java.nio.file.Path;

/**
 * Monta e conecta os componentes da aplicacao (repositorios e servicos),
 * funcionando como um container de dependencias simples.
 *
 * Os dados sao gravados na pasta pessoal do usuario, de modo que o programa
 * funcione sem instalacao de banco de dados no PC do cliente.
 */
public class ContextoAplicacao {

    private final EstoqueService estoqueService;
    private final VendaService vendaService;
    private final DashboardService dashboardService;

    public ContextoAplicacao() {
        this(pastaDeDadosPadrao());
    }

    public ContextoAplicacao(Path pastaDados) {
        MedicamentoRepository medicamentoRepository =
                new MedicamentoRepositoryArquivo(
                        pastaDados.resolve("medicamentos.dat"));
        VendaRepository vendaRepository =
                new VendaRepositoryArquivo(
                        pastaDados.resolve("vendas.dat"));

        this.estoqueService = new EstoqueService(medicamentoRepository);
        this.vendaService = new VendaService(vendaRepository, estoqueService);
        this.dashboardService = new DashboardService(
                medicamentoRepository, vendaRepository);

        // Popula o catalogo com exemplos na primeira execucao.
        if (estoqueService.listarTodos().isEmpty()) {
            SeedDados.popular(estoqueService);
        }
    }

    /**
     * Define a pasta de dados de acordo com o sistema operacional:
     * usa %APPDATA% no Windows e ~/.sistema-farmacia nos demais.
     */
    public static Path pastaDeDadosPadrao() {
        String appData = System.getenv("APPDATA");
        if (appData != null && !appData.isBlank()) {
            return Path.of(appData, "SistemaFarmacia");
        }
        return Path.of(System.getProperty("user.home"), ".sistema-farmacia");
    }

    public EstoqueService getEstoqueService() {
        return estoqueService;
    }

    public VendaService getVendaService() {
        return vendaService;
    }

    public DashboardService getDashboardService() {
        return dashboardService;
    }
}
