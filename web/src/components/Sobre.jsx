import { farmacia } from '../data/info.js'

export default function Sobre() {
  return (
    <section className="section section--alt" id="sobre">
      <div className="container sobre">
        <div className="sobre__text">
          <h2 className="section__title">Sobre a {farmacia.nome}</h2>
          <p>
            Somos uma farmácia de bairro que acredita em atendimento de
            verdade. Aqui você não é só mais um número: a gente conhece os
            clientes pelo nome e cuida de cada pedido com atenção.
          </p>
          <p>
            Trabalhamos com medicamentos, genéricos, produtos de higiene,
            cosméticos, itens infantis e muito mais — sempre com preço justo e
            a orientação de quem entende do assunto.
          </p>

          <div className="sobre__stats">
            <div>
              <strong>+15</strong>
              <span>anos de história</span>
            </div>
            <div>
              <strong>+5 mil</strong>
              <span>clientes atendidos</span>
            </div>
            <div>
              <strong>100%</strong>
              <span>foco em você</span>
            </div>
          </div>
        </div>

        <ul className="sobre__list">
          <li>✅ Farmacêutico responsável presente</li>
          <li>✅ Entrega em domicílio no bairro</li>
          <li>✅ Programas de medicamentos contínuos</li>
          <li>✅ Aferição de pressão e serviços rápidos</li>
        </ul>
      </div>
    </section>
  )
}
