// =====================================================================
//  DADOS DA FARMACIA  (edite aqui para personalizar o site)
// =====================================================================

export const farmacia = {
  nome: 'Farmácia Saúde & Vida',
  slogan: 'A farmácia do seu bairro, pertinho de você',
  descricao:
    'Há mais de 15 anos cuidando da saúde da nossa vizinhança, com ' +
    'atendimento humano, preços justos e entrega rápida na sua casa.',

  // Contato
  telefone: '(11) 99999-0000',
  // Somente números, com DDI 55, para o link do WhatsApp:
  whatsapp: '5511999990000',
  email: 'contato@farmaciasaudevida.com.br',

  // Endereço
  endereco: 'Rua das Flores, 100 — Centro',
  cidade: 'São Paulo / SP',
  cep: '01000-000',

  // Horário de funcionamento
  horarios: [
    { dia: 'Segunda a Sexta', hora: '08h00 — 20h00' },
    { dia: 'Sábado', hora: '08h00 — 14h00' },
    { dia: 'Domingo e feriados', hora: '09h00 — 13h00' },
  ],
}

// Mensagem pré-preenchida ao abrir o WhatsApp
export const mensagemWhatsApp = encodeURIComponent(
  'Olá! Vim pelo site e gostaria de fazer um pedido.',
)

export const linkWhatsApp = `https://wa.me/${farmacia.whatsapp}?text=${mensagemWhatsApp}`
