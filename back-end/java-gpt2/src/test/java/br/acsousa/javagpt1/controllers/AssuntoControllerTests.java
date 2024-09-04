package br.acsousa.javagpt1.controllers;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.factories.AssuntoFactory;
import br.acsousa.javagpt1.services.AssuntoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(AssuntoController.class)
public class AssuntoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssuntoService assuntoService;

    @Autowired
    ObjectMapper objectMapper;

    private static final String BASE_URL = "/assuntos";
    private long existingAssuntoId;
    private long nonExistingAssuntoId;
    private AssuntoDTO existingAssuntoDTO;
    private AssuntoDTO nonExistingAssuntoDTO;
    private PageImpl<AssuntoDTO> page;

    @BeforeEach
    void setUp() throws Exception {
        existingAssuntoId = 1L;
        nonExistingAssuntoId = 2L;
        existingAssuntoDTO = AssuntoFactory.createExistingAssuntoDTO();
        nonExistingAssuntoDTO = AssuntoFactory.createNonExistingAssuntoDTO();
        page = new PageImpl<>(List.of(existingAssuntoDTO));

        when(assuntoService.getAllByFilterPaged(any(), any(), any(), any())).thenReturn(page);

        when(assuntoService.getAll()).thenReturn(List.of(existingAssuntoDTO));

        when(assuntoService.create(existingAssuntoDTO)).thenThrow(EntityAlreadyExisting.class);
        when(assuntoService.create(nonExistingAssuntoDTO)).thenReturn(nonExistingAssuntoDTO);

        when(assuntoService.update(eq(nonExistingAssuntoId), any())).thenThrow(ResourceNotFoundException.class);
        when(assuntoService.update(eq(existingAssuntoId), any())).thenReturn(existingAssuntoDTO);

        doThrow(ResourceNotFoundException.class).when(assuntoService).delete(nonExistingAssuntoId);
        doNothing().when(assuntoService).delete(existingAssuntoId);
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
        result.andExpect(jsonPath("$[0].nome").value("Tipos primitivos"));
        result.andExpect(jsonPath("$[0].materia.id").value("1"));
        result.andExpect(jsonPath("$[0].materia.nome").value("Java"));
    }

    @Test
    public void createShouldReturnBadRequestWhenAssuntoDTOExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(existingAssuntoDTO);

        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void createShouldReturnObjectWhenAssuntoDTONonExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(nonExistingAssuntoDTO);

        ResultActions result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isCreated());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.nome").exists());
        result.andExpect(jsonPath("$.materia.id").exists());
        result.andExpect(jsonPath("$.materia.nome").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdNonExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(existingAssuntoDTO);

        ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", nonExistingAssuntoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnObjectWhenAssuntoIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(nonExistingAssuntoDTO);

        ResultActions result = mockMvc.perform(put(BASE_URL + "/{id}", existingAssuntoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        result.andExpect(status().isOk());
        result.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.nome").exists());
        result.andExpect(jsonPath("$.materia.id").exists());
        result.andExpect(jsonPath("$.materia.nome").exists());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdNonExists() throws Exception {
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", nonExistingAssuntoId));

        result.andExpect(status().isNotFound());
    }

    @Test
    public void deleteShouldReturnNotContentWhenIdExists() throws Exception {
        ResultActions result = mockMvc.perform(delete(BASE_URL + "/{id}", existingAssuntoId));

        result.andExpect(status().isNoContent());
    }
}
