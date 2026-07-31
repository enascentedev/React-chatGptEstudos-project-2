# java-gpt1

Back-end Java/Spring do projeto de estudos.

## Configuração local

A credencial da OpenAI é lida somente da variável de ambiente
`CHAT_GPT_API_KEY`. O arquivo `.env.example` é apenas um modelo e não
contém uma credencial válida.

No PowerShell:

```powershell
$env:CHAT_GPT_API_KEY = "ADICIONE_SUA_CHAVE_AQUI"
./mvnw spring-boot:run
```

Substitua o placeholder apenas no ambiente local ou no gerenciador de segredos
do servidor. Nunca salve o valor real no código, no `.env.example` ou no Git.
