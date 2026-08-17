import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// base './' -> caminhos relativos, para o site funcionar tanto em
// hospedagem na raiz quanto em subpasta (ex.: GitHub Pages).
export default defineConfig({
  base: './',
  plugins: [react()],
})
