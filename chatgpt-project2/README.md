# Chat Estudos — front-end

Interface React deste projeto. A documentação completa (arquitetura, back-end, configuração e estado atual) está no [README da raiz do repositório](../README.md).

## Rodar localmente

```bash
npm install
npm start
```

A aplicação abre em `http://localhost:3000`.

**Não é necessária chave de API neste diretório.** A chave da OpenAI é lida apenas pelo back-end Java, a partir da variável de ambiente `CHAT_GPT_API_KEY` — chaves de provedores de IA não devem ficar no front-end, porque tudo que entra no bundle do navegador é público.

O front-end depende da API Java rodando. As URLs estão fixas em `src/pages/` apontando para o serviço hospedado no Render, que está **fora do ar**; para uso local, ajuste-as para `http://localhost:8080`.

## Build

```bash
npm run build
```

O arquivo `netlify.toml` já contém o redirecionamento de SPA necessário para deploy no Netlify.
