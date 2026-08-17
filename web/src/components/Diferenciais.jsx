import { diferenciais } from '../data/produtos.js'

export default function Diferenciais() {
  return (
    <section className="section diferenciais">
      <div className="container">
        <div className="grid grid--4">
          {diferenciais.map((d) => (
            <div className="card-plain" key={d.titulo}>
              <div className="card-plain__icon">{d.icone}</div>
              <h3 className="card-plain__title">{d.titulo}</h3>
              <p className="card-plain__text">{d.texto}</p>
            </div>
          ))}
        </div>
      </div>
    </section>
  )
}
