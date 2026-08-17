import { useState } from 'react'
import { farmacia } from '../data/info.js'

export default function Contato() {
  const [nome, setNome] = useState('')
  const [mensagem, setMensagem] = useState('')

  function enviarWhatsApp(e) {
    e.preventDefault()
    const texto = `Olá! Meu nome é ${nome || '...'}.%0A${encodeURIComponent(
      mensagem || 'Gostaria de mais informações.',
    )}`
    window.open(`https://wa.me/${farmacia.whatsapp}?text=${texto}`, '_blank')
  }

  return (
    <section className="section section--alt" id="contato">
      <div className="container contato">
        <div className="contato__info">
          <h2 className="section__title">Fale com a gente</h2>
          <p className="section__subtitle">
            Tire dúvidas, confira disponibilidade ou faça seu pedido.
          </p>

          <ul className="contato__lista">
            <li>
              <span className="contato__icon">📱</span>
              <div>
                <strong>WhatsApp / Telefone</strong>
                <p>{farmacia.telefone}</p>
              </div>
            </li>
            <li>
              <span className="contato__icon">✉️</span>
              <div>
                <strong>E-mail</strong>
                <p>{farmacia.email}</p>
              </div>
            </li>
            <li>
              <span className="contato__icon">📍</span>
              <div>
                <strong>Endereço</strong>
                <p>
                  {farmacia.endereco} — {farmacia.cidade}
                </p>
              </div>
            </li>
          </ul>
        </div>

        <form className="contato__form" onSubmit={enviarWhatsApp}>
          <label>
            Seu nome
            <input
              type="text"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              placeholder="Como podemos te chamar?"
            />
          </label>
          <label>
            Mensagem
            <textarea
              rows="4"
              value={mensagem}
              onChange={(e) => setMensagem(e.target.value)}
              placeholder="O que você precisa? Ex.: quero 2 caixas de Dipirona"
            ></textarea>
          </label>
          <button type="submit" className="btn btn--primary btn--lg">
            Enviar pelo WhatsApp
          </button>
          <small>Ao enviar, abrimos o WhatsApp com sua mensagem pronta.</small>
        </form>
      </div>
    </section>
  )
}
