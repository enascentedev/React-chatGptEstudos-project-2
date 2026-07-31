package br.acsousa.javagpt1.services;

import java.util.List;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.dtos.RespostaDTO;
import br.acsousa.javagpt1.entities.Resposta;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@Service
public class ChatGPTService {
	private static final String API_KEY_ENV_VAR = "CHAT_GPT_API_KEY";
	private static final String API_KEY_PLACEHOLDER = "ADICIONE_SUA_CHAVE_AQUI";
	private static final String MODEL = "gpt-3.5-turbo";
	private static final String ROLE = "user";

	@Autowired
	private AssuntoService assuntoService;

	@Autowired
	private ModelMapper modelMapper;
	
	public RespostaDTO criarPerguntaPorAssunto(Long idAssunto) {
		AssuntoDTO assuntoDTO = assuntoService.findById(idAssunto);

		String pergunta = "Me faça uma pergunta de " +
				assuntoDTO.getMateria().getNome() + " sobre " + assuntoDTO.getNome() +
				". O formato da resposta deve ser Pergunta: mais a descrição da pergunta e em seguida a resposta.";

		Resposta resposta = new Resposta(apiChatGPT(pergunta).get(0).getMessage().getContent());

		return modelMapper.map(resposta, RespostaDTO.class);
	}

	public RespostaDTO chatLivre(String pergunta) {
		Resposta resposta = new Resposta(apiChatGPT(pergunta).get(0).getMessage().getContent());

		return modelMapper.map(resposta, RespostaDTO.class);
	}

	private List<ChatCompletionChoice> apiChatGPT(String pergunta) {
		OpenAiService service = new OpenAiService(obterApiKey());

		ChatMessage chat = new ChatMessage(ROLE, pergunta);

		ChatCompletionRequest request = ChatCompletionRequest.builder()
				.messages(List.of(chat))
				.n(null)
				.temperature(0.5)
				.model(MODEL)
				.build();

		return service.createChatCompletion(request).getChoices();
	}

	private String obterApiKey() {
		String apiKey = System.getenv(API_KEY_ENV_VAR);

		if (apiKey == null || apiKey.isBlank() || API_KEY_PLACEHOLDER.equals(apiKey)) {
			throw new IllegalStateException(
					"Defina a variavel de ambiente " + API_KEY_ENV_VAR
							+ " no servidor. Use o arquivo .env.example apenas como modelo."
			);
		}

		return apiKey;
	}
}
