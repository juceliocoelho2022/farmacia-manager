import { produtos } from '../data/produtos.js'
import { linkWhatsApp } from '../data/info.js'

export default function Produtos() {
  return (
    <section className="section" id="produtos">
      <div className="container">
        <div className="section__head">
          <h2 className="section__title">Produtos em destaque</h2>
          <p className="section__subtitle">
            Alguns dos itens que você encontra aqui. Consulte a disponibilidade
            e faça seu pedido pelo WhatsApp.
          </p>
        </div>

        <div className="grid grid--3">
          {produtos.map((p) => (
            <article className="produto" key={p.nome}>
              <div className="produto__icon">{p.icone}</div>
              <div className="produto__body">
                <span className="produto__cat">{p.categoria}</span>
                <h3 className="produto__nome">{p.nome}</h3>
                <p className="produto__preco">{p.preco}</p>
              </div>
              <a
                className="btn btn--primary btn--sm"
                href={linkWhatsApp}
                target="_blank"
                rel="noopener noreferrer"
              >
                Pedir
              </a>
            </article>
          ))}
        </div>

        <p className="produtos__nota">
          * Preços e disponibilidade podem variar. Temos muito mais no balcão —
          é só chamar!
        </p>
      </div>
    </section>
  )
}
