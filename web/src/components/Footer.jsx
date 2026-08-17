import { farmacia } from '../data/info.js'

export default function Footer() {
  const ano = new Date().getFullYear()

  return (
    <footer className="footer">
      <div className="container footer__inner">
        <div className="footer__brand">
          <span className="footer__logo">➕ {farmacia.nome}</span>
          <p>{farmacia.slogan}</p>
        </div>

        <nav className="footer__links">
          <a href="#produtos">Produtos</a>
          <a href="#sobre">Sobre</a>
          <a href="#horarios">Horários</a>
          <a href="#contato">Contato</a>
        </nav>

        <div className="footer__contact">
          <p>📱 {farmacia.telefone}</p>
          <p>📍 {farmacia.endereco}</p>
          <p>{farmacia.cidade}</p>
        </div>
      </div>

      <div className="footer__bottom">
        <p>
          © {ano} {farmacia.nome}. Todos os direitos reservados.
        </p>
        <p className="footer__note">
          Site institucional. Este conteúdo não substitui a orientação de um
          profissional de saúde.
        </p>
      </div>
    </footer>
  )
}
