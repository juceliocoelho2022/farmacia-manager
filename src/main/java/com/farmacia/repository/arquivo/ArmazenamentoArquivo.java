package com.farmacia.repository.arquivo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Persistencia simples baseada em serializacao Java de uma lista de objetos.
 *
 * Toda a colecao e gravada de uma vez, o que e adequado para o volume de
 * dados de uma unica farmacia e evita dependencias externas (banco de dados),
 * facilitando o empacotamento do instalador.
 *
 * @param <T> tipo dos elementos armazenados (deve ser Serializable)
 */
public class ArmazenamentoArquivo<T> {

    private final Path arquivo;

    public ArmazenamentoArquivo(Path arquivo) {
        this.arquivo = arquivo;
    }

    /** Le a lista do disco; devolve lista vazia se o arquivo ainda nao existe. */
    @SuppressWarnings("unchecked")
    public List<T> carregar() {
        if (!Files.exists(arquivo)) {
            return new ArrayList<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(arquivo)))) {
            Object lido = in.readObject();
            if (lido instanceof List) {
                return new ArrayList<>((List<T>) lido);
            }
            return new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new UncheckedIOException(
                    "Falha ao ler dados de " + arquivo,
                    e instanceof IOException
                            ? (IOException) e
                            : new IOException(e));
        }
    }

    /** Grava a lista inteira no disco, criando os diretorios necessarios. */
    public void salvar(List<T> dados) {
        try {
            Path pai = arquivo.getParent();
            if (pai != null) {
                Files.createDirectories(pai);
            }
            try (ObjectOutputStream out = new ObjectOutputStream(
                    new BufferedOutputStream(Files.newOutputStream(arquivo)))) {
                out.writeObject(new ArrayList<>(dados));
            }
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Falha ao gravar dados em " + arquivo, e);
        }
    }
}
