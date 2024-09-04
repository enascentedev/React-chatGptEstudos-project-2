package br.acsousa.javagpt1.services;

import java.util.ArrayList;
import java.util.List;

import br.acsousa.javagpt1.entities.Materia;
import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.repositories.MateriaRepository;
import br.acsousa.javagpt1.repositories.custons.MateriaCustomRepository;
import br.acsousa.javagpt1.services.exceptions.DataBaseException;
import br.acsousa.javagpt1.services.exceptions.EntityAlreadyExisting;
import br.acsousa.javagpt1.services.exceptions.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MateriaService {

	@Autowired
	private MateriaRepository materiaRepository;

	@Autowired
	private MateriaCustomRepository materiaCustomRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	public Materia getById(Long id) {
		return materiaRepository.findById(id).get();
	}

	public Page<MateriaDTO> getAllByFilterPaged(String idString, String nome, Pageable pageable) {
		Long id = null;
		if(idString != null) id = Long.parseLong(idString);

		Page<Materia> page = materiaCustomRepository.findByFilter(id, nome, pageable);

		return page.map(materia -> modelMapper.map(materia, MateriaDTO.class));
	}
	
	public List<MateriaDTO> getAll(){
		List<Materia> materiaList = materiaRepository.findAll();
		List<MateriaDTO> materiaListDTO = new ArrayList<>();

		for (Materia materia : materiaList) {
			materiaListDTO.add(modelMapper.map(materia, MateriaDTO.class));
		}

		return materiaListDTO;
	}
	
	public MateriaDTO create(MateriaDTO materiaDTO) {
		if(materiaRepository.findByNome(materiaDTO.getNome()) != null) {
			throw new EntityAlreadyExisting("Já existe uma matéria com essa descrição (" + materiaDTO.getNome() + ")");
		}

		Materia materia = modelMapper.map(materiaDTO, Materia.class);

		materia = materiaRepository.save(materia);
		materiaDTO = modelMapper.map(materia, MateriaDTO.class);

		return materiaDTO;
	}

	public MateriaDTO update(Long id, MateriaDTO materiaDTO) {
		materiaRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Matéria id " + id + " não encontrada")
		);

		if(materiaRepository.findByNome(materiaDTO.getNome()) != null) {
			throw new EntityAlreadyExisting("Já existe uma matéria com essa descrição (" + materiaDTO.getNome() + ")");
		}

		Materia materia = modelMapper.map(materiaDTO, Materia.class);
		materia.setId(id);

		materia = materiaRepository.save(materia);

		return modelMapper.map(materia, MateriaDTO.class);
	}

	@Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
		if(!materiaRepository.existsById(id)){
			throw new ResourceNotFoundException("Matéria id " + id + " não encontrada");
		}
		try {
			materiaRepository.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataBaseException("Integridade violada, a matéria id " + id + " possui dependência");
		}
	}
}
