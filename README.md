# Chat Estudos — gerador de perguntas com IA (React + Java)

> **Projeto legado (2024).** Mantido como registro de portfólio, sem manutenção ativa. Front-end em Create React App (descontinuado) e back-end em Spring Boot — a arquitetura **não deve ser usada como referência atual**.

Aplicação full-stack que gera perguntas e respostas de estudo por matéria e assunto, usando a API da OpenAI. O usuário escolhe a matéria e o assunto; o back-end monta o prompt, chama o provedor de IA e devolve o conteúdo gerado.

**Este repositório contém as duas pontas:**

```text
chatgpt-project2/        → front-end React (Create React App, Tailwind, DaisyUI)
back-end/java-gpt2/      → API REST em Java 17 + Spring Boot
```

## Vídeo demonstrativo

<div align="center">
  <img src="./chatgpt-project2/public/chatGpt-2.gif" alt="Demonstração do projeto" width="600">
</div>

## Demonstração online — atenção

O front-end continua publicado em [chat-estudos.netlify.app](https://chat-estudos.netlify.app/), mas **o back-end hospedado no Render está fora do ar**. A página carrega, e as chamadas à API falham. Para ver o sistema funcionando é preciso rodar as duas pontas localmente, conforme as instruções abaixo.

## Onde fica a chave da OpenAI

A chave é lida **exclusivamente pelo back-end**, a partir da variável de ambiente `CHAT_GPT_API_KEY` (`ChatGPTService.java`). O front-end nunca vê a chave: ele só conversa com a API Java.

```text
Navegador  →  API Java/Spring (lê CHAT_GPT_API_KEY)  →  API da OpenAI
```

**Chaves de provedores de IA não devem ficar no front-end.** Tudo que entra no bundle do navegador é público — em Create React App, qualquer variável `REACT_APP_*` é embutida no JavaScript entregue ao usuário e pode ser extraída por qualquer visitante. Por isso o front-end deste projeto **não usa `.env` nem requer chave para rodar**.

> Versões antigas deste projeto (abril/2024) chamavam a OpenAI diretamente do navegador com a chave embutida no bundle. Essa abordagem foi abandonada em favor do back-end Java. Se você reaproveitar código desse período, não repita o padrão.

## Estado atual

| Funcionalidade | Estado | Evidência |
|---|---|---|
| CRUD de matérias e assuntos | Implementado | `back-end/.../controllers/MateriaController.java`, `AssuntoController.java` |
| Paginação | Implementado | `MateriaController` — `Pageable` |
| Geração de perguntas por IA | Implementado | `back-end/.../services/ChatGPTService.java` |
| Testes unitários do back-end | Implementado | `back-end/java-gpt2/src/test/` — services, controllers e repositories |
| Front-end com rotas e listagens | Implementado | `chatgpt-project2/src/pages/` |
| Banco de dados | Parcial | H2 **em memória**: os dados somem ao reiniciar a aplicação |
| Seletor de temas | Planejado | DaisyUI está instalado, mas não há troca de tema na interface |
| Testes do front-end | Planejado | dependências presentes, nenhum teste escrito |
| Deploy funcional | Planejado | back-end fora do ar; ver a seção de demonstração |

**Legenda** — *Implementado*: funciona ponta a ponta. *Parcial*: funciona com limitações declaradas. *Planejado*: não implementado.

## Modelo de IA utilizado

`gpt-3.5-turbo`, definido em `ChatGPTService.java`. Versões anteriores deste README mencionavam GPT-4 — o código nunca usou essa versão.

## Tecnologias

**Front-end:** React 18 (Create React App), React Router 6, Tailwind CSS 3, DaisyUI
**Back-end:** Java 17, Spring Boot, Spring Data JPA, H2 (em memória), ModelMapper, biblioteca `openai-java` (`com.theokanning`)

## Como rodar

### Back-end

Pré-requisitos: JDK 17 e uma chave de API da OpenAI.

```bash
cd back-end/java-gpt2

# Defina a chave no ambiente do servidor (nunca no código nem no front-end)
export CHAT_GPT_API_KEY="sua-chave-aqui"     # Windows PowerShell: $env:CHAT_GPT_API_KEY="sua-chave-aqui"

./mvnw spring-boot:run
```

A API sobe em `http://localhost:8080`.

### Front-end

```bash
cd chatgpt-project2
npm install
npm start
```

A interface abre em `http://localhost:3000`. As URLs da API estão fixas no código apontando para o serviço do Render (fora do ar) — para uso local, ajuste-as em `src/pages/` para `http://localhost:8080`.

## Segurança e configuração

- **Console H2 exposto:** `application-test.properties` habilita o console do banco com `spring.h2.console.settings.web-allow-others=true`, e o perfil padrão é `test` (`spring.profiles.active=${APP_PROFILE:test}`). Ao publicar em qualquer ambiente acessível pela rede, **defina `APP_PROFILE` explicitamente** e desabilite o console — caso contrário o banco fica navegável por terceiros.
- **Banco em memória:** a configuração `test` usa H2 em memória, sem persistência entre execuções.
- Não há arquivo `.env` neste repositório, e nenhum é necessário.

## Limitações conhecidas

- URLs do back-end fixas no código do front-end, sem configuração por ambiente.
- Sem persistência real de dados (H2 em memória).
- Sem testes no front-end.
- Deploy do back-end indisponível.

## Licença

MIT — veja o arquivo [LICENSE](LICENSE).

## Contato

Emanuel Nascente — [emanuelnascente@gmail.com](mailto:emanuelnascente@gmail.com)
