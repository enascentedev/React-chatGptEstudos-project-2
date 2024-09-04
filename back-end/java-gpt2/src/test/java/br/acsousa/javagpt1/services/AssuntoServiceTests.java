package br.acsousa.javagpt1.services;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.entities.Assunto;
import br.acsousa.javagpt1.entities.Materia;
import br.acsousa.javagpt1.factories.AssuntoFactory;
import br.acsousa.javagpt1.factories.MateriaFactory;
import br.acsousa.javagpt1.repositories.AssuntoRepository;
import br.acsousa.javagpt1.repositories.MateriaRepository;
import br.acsousa.javagpt1.repositories.custons.AssuntoCustomRepository;
import br.acsousa.javagpt1.services.exceptions.EntityAlreadyExisting;
import br.acsousa.javagpt1.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class AssuntoServiceTests {

    @InjectMocks
    private AssuntoService assuntoService;

    @Mock
    private AssuntoRepository assuntoRepository;

    @Mock
    private AssuntoCustomRepository assuntoCustomRepository;

    @Mock
    private MateriaRepository materiaRepository;

    @Mock
    private ModelMapper modelMapper;

    private long existingAssuntoId;
    private long existingMateriaId;
    private long nonExistingAssuntoId;
    private long nonExistingMateriaId;
    private String existingAssuntoNome;
    private String nonExistingAssuntoNome;
    private Assunto assunto;
    private AssuntoDTO assuntoDTO;
    private Materia materia;
    private PageImpl<Assunto> page;

    @BeforeEach
    void setUp() throws Exception {
        existingAssuntoId = 1L;
        existingMateriaId = 2L;
        nonExistingAssuntoId = 3L;
        nonExistingMateriaId = 4L;
        existingAssuntoNome = "String";
        nonExistingAssuntoNome = "Array";
        assunto = AssuntoFactory.createAssunto();
        assuntoDTO = AssuntoFactory.createExistingAssuntoDTO();
        materia = MateriaFactory.createMateria();
        page = new PageImpl<>(List.of(assunto));

        Mockito.when(assuntoRepository.findById(existingAssuntoId)).thenReturn(Optional.of(assunto));
        Mockito.doThrow(ResourceNotFoundException.class).when(assuntoRepository).findById(nonExistingAssuntoId);

        Mockito.when(modelMapper.map(assunto, AssuntoDTO.class)).thenReturn(assuntoDTO);
        Mockito.when(modelMapper.map(assuntoDTO, Assunto.class)).thenReturn(assunto);

        Mockito.when(assuntoCustomRepository.findByFilter(null, null, null, PageRequest.of(0, 12))).thenReturn(page);

        Mockito.when(assuntoRepository.findAll()).thenReturn(List.of(assunto));

        Mockito.when(assuntoRepository.findByMateriaId(existingAssuntoId)).thenReturn(List.of(assunto));

        Mockito.when(materiaRepository.findById(existingMateriaId)).thenReturn(Optional.of(materia));
        Mockito.doThrow(ResourceNotFoundException.class).when(materiaRepository).findById(nonExistingMateriaId);

        Mockito.doThrow(EntityAlreadyExisting.class).when(assuntoRepository).findByMateriaIdAndNome(existingMateriaId, existingAssuntoNome);

        Mockito.when(assuntoRepository.save(ArgumentMatchers.any())).thenReturn(assunto);

        Mockito.doThrow(ResourceNotFoundException.class).when(assuntoRepository).findById(nonExistingAssuntoId);

        Mockito.doThrow(ResourceNotFoundException.class).when(assuntoRepository).existsById(nonExistingAssuntoId);
        Mockito.when(assuntoRepository.existsById(existingAssuntoId)).thenReturn(true);

        Mockito.doNothing().when(assuntoRepository).deleteById(existingAssuntoId);
    }

    @Test
    public void findByIdShouldReturnAssuntoDTOWhenIdExists() {
        AssuntoDTO assuntoDTO = assuntoService.findById(existingAssuntoId);

        Assertions.assertNotNull(assuntoDTO);
        Mockito.verify(assuntoRepository).findById(existingAssuntoId);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNonExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            assuntoService.findById(nonExistingAssuntoId);
        });

        Mockito.verify(assuntoRepository).findById(nonExistingAssuntoId);
    }

    @Test
    public void getAllByFilterPagedShouldReturnPage() {
        Pageable pageable = PageRequest.of(0, 12);

        Page<AssuntoDTO> page = assuntoService.getAllByFilterPaged(null, null, null, pageable);

        Assertions.assertNotNull(page);
    }

    @Test
    public void getAllShouldReturnListOfAssuntoDTO() {
        List<AssuntoDTO> assuntoDTOList = assuntoService.getAll();

        Assertions.assertNotNull(assuntoDTOList);
        Mockito.verify(assuntoRepository).findAll();
    }

    @Test
    public void getAssuntoByMateriaIdShouldReturnListOfAssuntoDTO() {
        List<AssuntoDTO> assuntoDTOList = assuntoService.getAssuntoByMateriaId(existingAssuntoId);

        Assertions.assertNotNull(assuntoDTOList);
        Mockito.verify(assuntoRepository).findByMateriaId(existingAssuntoId);
    }

    @Test
    public void createShouldThrowResourceNotFoundExceptionWhenMateriaIdNonExists() {
        assuntoDTO.getMateria().setId(nonExistingMateriaId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            assuntoService.create(assuntoDTO);
        });
    }

    @Test
    public void createShouldThrowEntityAlreadyExistingWhenMateriaIdAndAssuntoNomeExists() {
        assuntoDTO.setNome(existingAssuntoNome);
        assuntoDTO.getMateria().setId(existingMateriaId);

        Assertions.assertThrows(EntityAlreadyExisting.class, () -> {
            assuntoService.create(assuntoDTO);
        });
    }

    @Test
    public void createShouldCreateAssuntoWhenDatasAreCorrect() {
        assuntoDTO.setNome(nonExistingAssuntoNome);
        assuntoDTO.getMateria().setId(existingMateriaId);

        Assertions.assertDoesNotThrow(() -> {
            assuntoDTO = assuntoService.create(assuntoDTO);
        });
        Assertions.assertNotNull(assuntoDTO);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenAssuntoIdNonExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            assuntoService.update(nonExistingAssuntoId, assuntoDTO);
        });
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenMateriaIdNonExists() {
        assuntoDTO.getMateria().setId(nonExistingMateriaId);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            assuntoService.update(existingMateriaId, assuntoDTO);
        });
    }

    @Test
    public void updateShouldThrowEntityAlreadyExistingWhenMateriaIdAndAssuntoNomeExists() {
        assuntoDTO.setNome(existingAssuntoNome);
        assuntoDTO.getMateria().setId(existingMateriaId);

        Assertions.assertThrows(EntityAlreadyExisting.class, () -> {
            assuntoService.update(existingAssuntoId, assuntoDTO);
        });
    }

    @Test
    public void updateShouldSaveAssuntoWhenDatasAreCorrect() {
        assuntoDTO.setNome(nonExistingAssuntoNome);
        assuntoDTO.getMateria().setId(existingMateriaId);

        Assertions.assertDoesNotThrow(() -> {
            assuntoDTO = assuntoService.update(existingAssuntoId, assuntoDTO);
        });
        Assertions.assertNotNull(assuntoDTO);
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNonExists() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            assuntoService.delete(nonExistingAssuntoId);
        });
    }

    @Test
    public void deleteShouldDoNothingWhenExistsId() {
        Assertions.assertDoesNotThrow(() -> {
            assuntoService.delete(existingAssuntoId);
        });

        Mockito.verify(assuntoRepository).deleteById(existingAssuntoId);
    }
}
