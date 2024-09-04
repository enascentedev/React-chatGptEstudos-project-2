package br.acsousa.javagpt1.controllers;

import java.util.List;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.services.MateriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@CrossOrigin
@RestController
@RequestMapping(value = "/materias")
public class MateriaController {

	@Autowired
	private MateriaService materiaService;
	
	@PostMapping
	public ResponseEntity<MateriaDTO> create(@RequestBody MateriaDTO materiaDTO){
		return ResponseEntity.status(HttpStatus.CREATED).body(materiaService.create(materiaDTO));
	}

	@GetMapping(value = "/page")
	public ResponseEntity<Page<MateriaDTO>> getAllByFilterPaged(
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "nome", defaultValue = "", required = false) String nome,
			@PageableDefault(page = 0, size = 5, sort = "id") Pageable pageable
	) {
		Page<MateriaDTO> materias = materiaService.getAllByFilterPaged(id, nome.trim(), pageable);

		return ResponseEntity.ok().body(materias);
	}

	@GetMapping
	public ResponseEntity<List<MateriaDTO>> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(materiaService.getAll());
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<MateriaDTO> update(@PathVariable Long id, @RequestBody MateriaDTO materiaDTO) {
		return ResponseEntity.status(HttpStatus.OK).body(materiaService.update(id, materiaDTO));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<MateriaDTO> delete(@PathVariable Long id) {
		materiaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
