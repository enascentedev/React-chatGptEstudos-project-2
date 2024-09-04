package br.acsousa.javagpt1.controllers;

import java.util.List;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.services.AssuntoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping(value = "/assuntos")
public class AssuntoController {

	@Autowired
	private AssuntoService assuntoService;

	@GetMapping
	public ResponseEntity<List<AssuntoDTO>> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(assuntoService.getAll());
	}

	@GetMapping(value = "/{materiaId}")
	public ResponseEntity<List<AssuntoDTO>> getAssuntoByMateriaId(@PathVariable Long materiaId){
		return ResponseEntity.status(HttpStatus.OK).body(assuntoService.getAssuntoByMateriaId(materiaId));
	}
	
	@PostMapping
	public ResponseEntity<AssuntoDTO> create(@RequestBody AssuntoDTO assuntoDTO){
		return ResponseEntity.status(HttpStatus.CREATED).body(assuntoService.create(assuntoDTO));
	}

	@GetMapping(value = "/page")
	public ResponseEntity<Page<AssuntoDTO>> getAllByFilterPaged(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "nome", defaultValue = "", required = false) String nome,
			@RequestParam(value = "materiaId", required = false) String materiaId,
			@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
	) {
		Page<AssuntoDTO> materias = assuntoService.getAllByFilterPaged(id, nome.trim(), materiaId, pageable);

		return ResponseEntity.ok().body(materias);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<AssuntoDTO> update(@PathVariable Long id, @RequestBody AssuntoDTO assuntoDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(assuntoService.update(id, assuntoDTO));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<AssuntoDTO> delete(@PathVariable Long id) {
		assuntoService.delete(id);
		return ResponseEntity.noContent().build();
	}
	
}
