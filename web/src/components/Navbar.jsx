import { farmacia, linkWhatsApp } from '../data/info.js'

export default function Navbar() {
  return (
    <header className="navbar">
      <div className="container navbar__inner">
        <a href="#topo" className="navbar__logo">
          <span className="navbar__logo-icon">➕</span>
          <span>{farmacia.nome}</span>
        </a>

        <nav className="navbar__links">
          <a href="#produtos">Produtos</a>
          <a href="#sobre">Sobre</a>
          <a href="#horarios">Horários</a>
          <a href="#contato">Contato</a>
        </nav>

        <a
          className="btn btn--primary navbar__cta"
          href={linkWhatsApp}
          target="_blank"
          rel="noopener noreferrer"
        >
          Pedir no WhatsApp
        </a>
      </div>
    </header>
  )
}
