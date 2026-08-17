import Navbar from './components/Navbar.jsx'
import Hero from './components/Hero.jsx'
import Diferenciais from './components/Diferenciais.jsx'
import Produtos from './components/Produtos.jsx'
import Sobre from './components/Sobre.jsx'
import HorarioLocal from './components/HorarioLocal.jsx'
import Contato from './components/Contato.jsx'
import Footer from './components/Footer.jsx'
import BotaoWhatsApp from './components/BotaoWhatsApp.jsx'

export default function App() {
  return (
    <>
      <Navbar />
      <main>
        <Hero />
        <Diferenciais />
        <Produtos />
        <Sobre />
        <HorarioLocal />
        <Contato />
      </main>
      <Footer />
      <BotaoWhatsApp />
    </>
  )
}
