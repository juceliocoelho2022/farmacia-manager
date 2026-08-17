import { farmacia } from '../data/info.js'

export default function HorarioLocal() {
  const enderecoCompleto = `${farmacia.endereco}, ${farmacia.cidade}`
  const mapaSrc = `https://www.google.com/maps?q=${encodeURIComponent(
    enderecoCompleto,
  )}&output=embed`

  return (
    <section className="section" id="horarios">
      <div className="container">
        <div className="section__head">
          <h2 className="section__title">Horários e Localização</h2>
          <p className="section__subtitle">
            Passe aqui ou peça pelo WhatsApp — estamos pertinho de você.
          </p>
        </div>

        <div className="horarios">
          <div className="horarios__info">
            <h3>🕒 Funcionamento</h3>
            <ul className="horarios__lista">
              {farmacia.horarios.map((h) => (
                <li key={h.dia}>
                  <span>{h.dia}</span>
                  <strong>{h.hora}</strong>
                </li>
              ))}
            </ul>

            <h3 className="horarios__endtitulo">📍 Endereço</h3>
            <p className="horarios__endereco">
              {farmacia.endereco}
              <br />
              {farmacia.cidade} — CEP {farmacia.cep}
            </p>
          </div>

          <div className="horarios__mapa">
            <iframe
              title="Mapa da farmácia"
              src={mapaSrc}
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
            ></iframe>
          </div>
        </div>
      </div>
    </section>
  )
}
