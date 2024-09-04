package br.acsousa.javagpt1.repositories;

import br.acsousa.javagpt1.entities.Assunto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class AssuntoRepositoryTests {

    @Autowired
    private AssuntoRepository assuntoRepository;

    private Long existingMateriaId;
    private Long nonExistingMateriaId;
    private String existingAssuntoNome;
    private String nonExistingAssuntoNome;

    @BeforeEach
    void setUp() throws Exception {
        existingMateriaId = 1L;
        nonExistingMateriaId = Long.MAX_VALUE;
        existingAssuntoNome = "Tipos primitivos";
        nonExistingAssuntoNome = "Tangamandapio";
    }

    @Test
    public void findByMateriaIdShouldReturnAnAssuntoListWhenMateriaIdExists() {
        List<Assunto> assuntiList = assuntoRepository.findByMateriaId(existingMateriaId);

        Assertions.assertFalse(assuntiList.isEmpty());
    }

    @Test
    public void findByMateriaIdShouldNotReturnAnAssuntoListWhenMateriaIdNonExists() {
        List<Assunto> assuntiList = assuntoRepository.findByMateriaId(nonExistingMateriaId);

        Assertions.assertTrue(assuntiList.isEmpty());
    }

    @Test
    public void findByMateriaIdAndNomeShouldReturnAssuntoWhenDatasExists() {
        Assunto assunto = assuntoRepository.findByMateriaIdAndNome(existingMateriaId, existingAssuntoNome);

        Assertions.assertNotNull(assunto.getId());
    }

    @Test
    public void findByMateriaIdAndNomeShouldNotReturnAssuntoWhenMateriaIdNonExists() {
        Assunto assunto = assuntoRepository.findByMateriaIdAndNome(nonExistingMateriaId, existingAssuntoNome);

        Assertions.assertNull(assunto);
    }

    @Test
    public void findByMateriaIdAndNomeShouldNotReturnAssuntoWhenNomeNonExists() {
        Assunto assunto = assuntoRepository.findByMateriaIdAndNome(existingMateriaId, nonExistingAssuntoNome);

        Assertions.assertNull(assunto);
    }
}
