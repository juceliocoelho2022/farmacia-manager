# 🌐 Site da Farmácia (React + Vite)

Site institucional (vitrine) da farmácia: apresenta a loja, produtos em
destaque, diferenciais, horários, localização e contato — com botão de
**WhatsApp** para o cliente fazer o pedido.

Feito com **React 18 + Vite**, sem back-end (site estático).

## ▶️ Rodar localmente

Requer **Node.js 18+**.

```bash
cd web
npm install      # instala as dependências (primeira vez)
npm run dev      # abre em http://localhost:5173
```

## 📦 Gerar a versão de produção

```bash
npm run build    # gera a pasta web/dist (pronta para hospedar)
npm run preview  # pré-visualiza o build localmente
```

## ✏️ Como personalizar

Quase tudo fica em dois arquivos, fáceis de editar:

- **`src/data/info.js`** — nome da farmácia, telefone/WhatsApp, e-mail,
  endereço e horários de funcionamento.
- **`src/data/produtos.js`** — produtos em destaque e a lista de diferenciais.

O visual (cores do tema) fica em **`src/index.css`**, nas variáveis do topo
(`--verde`, `--laranja`, etc.).

> 💡 Dica: para o botão do WhatsApp funcionar, ajuste o campo `whatsapp` em
> `info.js` com o número no formato internacional só com números
> (ex.: `5511999990000`).

## 🚀 Publicar (GitHub Pages)

O repositório já inclui um workflow (`.github/workflows/deploy-site.yml`) que
compila e publica o site no **GitHub Pages** a cada push na `main`.

Para ativar: no GitHub, vá em **Settings → Pages → Build and deployment →
Source: GitHub Actions**. Depois do primeiro deploy, o site fica disponível em
`https://juceliocoelho2022.github.io/farmacia-manager/`.

## 🗂️ Estrutura

```
web/
├─ index.html
├─ vite.config.js
├─ src/
│  ├─ main.jsx / App.jsx
│  ├─ index.css            (tema e estilos)
│  ├─ data/                (info.js, produtos.js)  ← edite aqui
│  └─ components/          (Navbar, Hero, Produtos, Contato, ...)
```
