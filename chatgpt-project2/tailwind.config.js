module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
  },
  plugins: [
    require("daisyui"),
  ],
  daisyui: {
    themes: [
      'light', 'dark', 'cupcake', 'bumblebee', 'emerald',
      'corporate', 'synthwave', 'retro', 'cyberpunk', 'valentine',
      'halloween', 'garden', 'forest', 'aqua', 'lofi', 'pastel',
      'fantasy', 'wireframe', 'black', 'luxury', 'dracula',
      'cmyk', 'autumn', 'business', 'acid', 'lemonade', 'night',
      'coffee', 'winter'
    ],
    darkTheme: "dark", // nome de um dos temas inclusos para o modo escuro
    base: true, // aplica cor de fundo e cor de primeiro plano para o elemento raiz por padrão
    styled: true, // inclui cores do DaisyUI e decisões de design para todos os componentes
    utils: true, // adiciona classes de utilitários responsivos e modificador
    prefix: "", // prefixo para nomes de classe do DaisyUI (componentes, modificadores e nomes de classe responsivos. Não cores)
    logs: true, // Mostra informações sobre a versão do DaisyUI e configuração usada no console ao construir seu CSS
    themeRoot: ":root", // O elemento que recebe variáveis de cores CSS dos temas
  },
};
