package br.acsousa.javagpt1.services;

import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.entities.Materia;
import br.acsousa.javagpt1.factories.MateriaFactory;
import br.acsousa.javagpt1.repositories.MateriaRepository;
import br.acsousa.javagpt1.repositories.custons.MateriaCustomRepository;
import br.acsousa.javagpt1.services.exceptions.DataBaseException;
import br.acsousa.javagpt1.services.exceptions.EntityAlreadyExisting;
import br.acsousa.javagpt1.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
public class MateriaServiceTests {

    @InjectMocks
    private MateriaService materiaService;

    @Mock
    private MateriaRepository materiaRepository;

    @Mock
    private MateriaCustomRepository materiaCustomRepository;

    @Mock
    private ModelMapper modelMapper;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Materia materia;
    private MateriaDTO existingMateriaDTO;
    private MateriaDTO nonExistingMateriaDTO;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        materia = MateriaFactory.createMateria();
        existingMateriaDTO = MateriaFactory.createExistingMateriaDTO();
        nonExistingMateriaDTO = MateriaFactory.createNonExistingMateriaDTO();
        PageImpl<Materia> page = new PageImpl<>(List.of(materia));

        Mockito.when(materiaCustomRepository.findByFilter(null, null, PageRequest.of(0, 12))).thenReturn(page);

        Mockito.when(materiaRepository.findAll()).thenReturn(List.of(materia));

        Mockito.when(materiaRepository.findByNome(existingMateriaDTO.getNome())).thenReturn(materia);
        Mockito.when(materiaRepository.findByNome(nonExistingMateriaDTO.getNome())).thenReturn(null);

        Mockito.when(materiaRepository.findById(existingId)).thenReturn(Optional.of(materia));

        Mockito.when(materiaRepository.save(ArgumentMatchers.any())).thenReturn(materia);

        Mockito.when(materiaRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(materiaRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(materiaRepository.existsById(dependentId)).thenReturn(true);

        Mockito.doNothing().when(materiaRepository).deleteById(existingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(materiaRepository).deleteById(dependentId);

        Mockito.when(modelMapper.map(nonExistingMateriaDTO, Materia.class)).thenReturn(materia);
    }

    @Test
    public void getAllByFilterPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 12);

        Page<MateriaDTO> page = materiaService.getAllByFilterPaged(null, null, pageable);

        Assertions.assertNotNull(page);
    }

    @Test
    public void getAllShouldReturnList() {
        List<MateriaDTO> materiasDTO = materiaService.getAll();

        Assertions.assertNotNull(materiasDTO);
    }

    @Test
    public void createShouldThrowEntityAlreadyExistingWhenNomeExists() {
        Assertions.assertThrows(EntityAlreadyExisting.class, () -> {
            materiaService.create(existingMateriaDTO);
        });

        Mockito.verify(materiaRepository).findByNome(existingMateriaDTO.getNome());
    }

    @Test
    public void createShouldSaveWhenNomeNonExists() {
        Assertions.assertDoesNotThrow(() -> {
            materiaService.create(nonExistingMateriaDTO);
        });

        Mockito.verify(materiaRepository).save(materia);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNonExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            materiaService.update(nonExistingId, existingMateriaDTO);
        });
    }

    @Test
    public void updateShouldThrowEntityAlreadyExistingWhenNomeExists() {
        Assertions.assertThrows(EntityAlreadyExisting.class, () -> {
            materiaService.update(existingId, existingMateriaDTO);
        });
    }

    @Test
    public void updateShouldSaveMateriaWhenIdExistsAndNomeNonExists() {
        Assertions.assertDoesNotThrow(() -> {
            materiaService.update(existingId, nonExistingMateriaDTO);
        });

        Mockito.verify(materiaRepository).save(materia);
    }

    @Test
    public void deleteShouldDoNothingWhenExistsId() {
        Assertions.assertDoesNotThrow(() -> {
            materiaService.delete(existingId);
        });

        Mockito.verify(materiaRepository).deleteById(existingId);
    }

    @Test
    public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DataBaseException.class, () -> {
            materiaService.delete(dependentId);
        });

        Mockito.verify(materiaRepository, times(1)).deleteById(dependentId);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNonExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            materiaService.delete(nonExistingId);
        });
    }
}
