import { linkWhatsApp } from '../data/info.js'

export default function BotaoWhatsApp() {
  return (
    <a
      className="whatsapp-float"
      href={linkWhatsApp}
      target="_blank"
      rel="noopener noreferrer"
      aria-label="Falar no WhatsApp"
    >
      <span className="whatsapp-float__icon">💬</span>
      <span className="whatsapp-float__text">WhatsApp</span>
    </a>
  )
}
