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

	@Autowired
	private AssuntoService assuntoService;

	@Autowired
	private ModelMapper modelMapper;

	private static final String API_KEY = System.getenv("CHAT_GPT_API_KEY");;
	private static final String MODEL = "gpt-3.5-turbo";
	private static final String ROLE = "user";
	
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
		OpenAiService service = new OpenAiService(API_KEY);

		ChatMessage chat = new ChatMessage(ROLE, pergunta);

		ChatCompletionRequest request = ChatCompletionRequest.builder()
				.messages(List.of(chat))
				.n(null)
				.temperature(0.5)
				.model(MODEL)
				.build();

		return service.createChatCompletion(request).getChoices();
	}
}
