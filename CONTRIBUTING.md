# Guia de Contribuição

Obrigado pelo interesse em contribuir com o **Sistema Farmácia**! Este guia
resume como preparar o ambiente, rodar os testes e enviar mudanças.

## 🧰 Requisitos

- **JDK 17 ou superior** (o projeto é compilado e testado nas versões 17 e 21).
- Nenhuma dependência externa em produção — apenas a biblioteca padrão do Java.
  Os testes usam o **JUnit 5** (baixado automaticamente pelo script de teste).

Verifique sua instalação:

```bash
java -version
```

## ▶️ Compilar e executar

```bash
./build.sh      # gera dist/SistemaFarmacia.jar   (Windows: build.bat)
./run.sh        # abre o aplicativo                (Windows: java -jar dist\SistemaFarmacia.jar)
```

## ✅ Rodar os testes

```bash
./test.sh
```

O script compila o código, baixa o JUnit Console Standalone (na primeira vez)
e executa a suíte. **Toda contribuição deve manter os testes passando** — e,
de preferência, incluir testes para o comportamento novo ou corrigido.

## 🗂️ Estrutura do projeto

```
src/main/java/com/farmacia
├─ model/        Modelos de domínio (Medicamento, Venda, ItemVenda, ...)
├─ repository/   Contratos de persistência (memoria/ e arquivo/)
├─ service/      Regras de negócio (EstoqueService, VendaService, Dashboard)
├─ ui/           Interface gráfica Swing
├─ ContextoAplicacao / SeedDados / Main
src/test/java/com/farmacia   Testes de unidade (JUnit 5)
```

Princípio de arquitetura: a **UI** fala apenas com a **camada de serviço**, que
aplica as regras usando **repositórios** por meio de interfaces. Regras de
negócio novas devem ficar na camada de serviço (com testes), não na UI.

## 🎨 Estilo de código

- Java com indentação de **4 espaços** (veja o `.editorconfig`).
- Nomes e comentários em **português**, seguindo o padrão do código existente.
- Prefira métodos curtos e coesos; mantenha a UI livre de regra de negócio.

## 🌿 Branches e commits

- Crie uma branch a partir da `main`: `git checkout -b feat/nome-curto`.
- Use mensagens no estilo **Conventional Commits**:
  - `feat: ...` nova funcionalidade
  - `fix: ...` correção de bug
  - `docs: ...` documentação
  - `refactor: ...`, `test: ...`, `chore: ...`
- Faça commits pequenos e descritivos.

## 🔀 Pull Requests

1. Garanta que `./test.sh` passa localmente.
2. Descreva **o que** mudou e **por quê**; anexe capturas de tela se mexer na UI.
3. Vincule issues relacionadas (ex.: `Closes #12`).
4. O workflow de **CI** (GitHub Actions) roda os testes automaticamente — o PR
   só deve ser mesclado com o CI verde.

## 🐞 Reportando bugs / sugerindo melhorias

Abra uma **issue** descrevendo:

- passos para reproduzir (no caso de bug) e o comportamento esperado;
- versão do Java e sistema operacional;
- prints, se ajudarem a ilustrar.

Contribuições de todos os tamanhos são bem-vindas. Obrigado! 💊
