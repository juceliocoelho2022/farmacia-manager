import { farmacia, linkWhatsApp } from '../data/info.js'

export default function Hero() {
  return (
    <section className="hero" id="topo">
      <div className="container hero__inner">
        <div className="hero__content">
          <span className="hero__badge">💊 Farmácia de bairro</span>
          <h1 className="hero__title">{farmacia.slogan}</h1>
          <p className="hero__text">{farmacia.descricao}</p>

          <div className="hero__actions">
            <a
              className="btn btn--primary btn--lg"
              href={linkWhatsApp}
              target="_blank"
              rel="noopener noreferrer"
            >
              Fazer pedido no WhatsApp
            </a>
            <a className="btn btn--ghost btn--lg" href="#produtos">
              Ver produtos
            </a>
          </div>

          <div className="hero__highlights">
            <span>🛵 Entrega rápida</span>
            <span>💳 Pix e cartão</span>
            <span>🧑‍⚕️ Atendimento humano</span>
          </div>
        </div>

        <div className="hero__art" aria-hidden="true">
          <div className="hero__card">
            <div className="hero__cross">➕</div>
            <p className="hero__card-name">{farmacia.nome}</p>
            <p className="hero__card-sub">Cuidando de você e da sua família</p>
          </div>
        </div>
      </div>
    </section>
  )
}
