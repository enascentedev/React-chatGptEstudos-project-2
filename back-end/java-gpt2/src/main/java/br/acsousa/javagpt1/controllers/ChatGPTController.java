package br.acsousa.javagpt1.controllers;

import br.acsousa.javagpt1.dtos.PerguntaDTO;
import br.acsousa.javagpt1.dtos.RespostaDTO;
import br.acsousa.javagpt1.services.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping(value = "/chat")
public class ChatGPTController {

	@Autowired
	private ChatGPTService chatGPTService;

	@GetMapping(value = "/{idAssunto}")
	public RespostaDTO criarPerguntaPorAssunto(@PathVariable Long idAssunto){
		return chatGPTService.criarPerguntaPorAssunto(idAssunto);
	}

	@PostMapping
	public RespostaDTO getAnswers(@RequestBody PerguntaDTO pergunta){
		return chatGPTService.chatLivre(pergunta.getDescricao());
	}
}
