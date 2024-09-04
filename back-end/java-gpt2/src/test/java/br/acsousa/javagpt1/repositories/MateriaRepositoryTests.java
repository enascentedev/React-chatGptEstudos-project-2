package br.acsousa.javagpt1.repositories;

import br.acsousa.javagpt1.entities.Materia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MateriaRepositoryTests {

    @Autowired
    private MateriaRepository materiaRepository;

    private String existingNome;

    @BeforeEach
    void setUp() throws Exception {
        existingNome = "Java";
    }

    @Test
    public void findByNomeShouldFindObjectWhenNomeExists() {

        Materia materia = materiaRepository.findByNome(existingNome);

        Assertions.assertNotNull(materia.getId());
    }
}
