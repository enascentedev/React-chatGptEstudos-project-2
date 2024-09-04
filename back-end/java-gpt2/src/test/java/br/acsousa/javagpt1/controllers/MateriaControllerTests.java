package br.acsousa.javagpt1.controllers;

import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.factories.MateriaFactory;
import br.acsousa.javagpt1.services.MateriaService;
import br.acsousa.javagpt1.services.exceptions.DataBaseException;
import br.acsousa.javagpt1.services.exceptions.EntityAlreadyExisting;
import br.acsousa.javagpt1.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MateriaController.class)
public class MateriaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MateriaService materiaService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String BASE_URL = "/materias";
    private long existingMateriaId;
    private long nonExistingMateriaId;
    private long dependentId;
    private MateriaDTO existingMateriaDTO;
    private MateriaDTO nonExistingMateriaDTO;
    private PageImpl<MateriaDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingMateriaId = 1L;
        nonExistingMateriaId = 2L;
        dependentId = 3L;
        existingMateriaDTO = MateriaFactory.createExistingMateriaDTO();
        nonExistingMateriaDTO = MateriaFactory.createNonExistingMateriaDTO();
        page = new PageImpl<>(List.of(existingMateriaDTO));

        when(materiaService.getAllByFilterPaged(any(), any(), any())).thenReturn(page);

        when(materiaService.getAll()).thenReturn(List.of(existingMateriaDTO));

        when(materiaService.create(existingMateriaDTO)).thenThrow(EntityAlreadyExisting.class);
        when(materiaService.create(nonExistingMateriaDTO)).thenReturn(nonExistingMateriaDTO);

        when(materiaService.update(eq(nonExistingMateriaId), any())).thenThrow(ResourceNotFoundException.class);
        when(materiaService.update(eq(existingMateriaId), any())).thenReturn(existingMateriaDTO);

        doThrow(DataBaseException.class).when(materiaService).delete(dependentId);
        doThrow(ResourceNotFoundException.class).when(materiaService).delete(nonExistingMateriaId);
        doNothing().when(materiaService).delete(existingMateriaId);
    }

    @Test
    public void getAllByFilterPagedShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(get(BASE_URL + "/page").accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllShouldReturnList() throws Exception {
        ResultActions result = mockMvc.perform(get(BASE_URL));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$[0].id").value("1"));
        result.andExpect(jsonPath("$[0].nome").value("Java"));
    }

    @Test
    public void createShouldReturnBadRequestWhenMateriaDTOExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(existingMateriaDTO);

        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createShouldReturnObjectWhenMateriaDTONonExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(nonExistingMateriaDTO);

        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isCreated());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.nome").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdNonExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(existingMateriaDTO);

        ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", nonExistingMateriaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnObjectWhenMateriaIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(nonExistingMateriaDTO);

        ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", existingMateriaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.nome").exists());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenDependentId() throws Exception {
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", dependentId));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdNonExists() throws Exception {
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", nonExistingMateriaId));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNotContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", existingMateriaId));

        result.andExpect(status().isNoContent());
    }

}
