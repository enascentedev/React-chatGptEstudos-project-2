package br.acsousa.javagpt1.factories;

import br.acsousa.javagpt1.dtos.AssuntoDTO;
import br.acsousa.javagpt1.entities.Assunto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class AssuntoFactory {

    @Autowired
    private static ModelMapper modelMapper;

    public static Assunto createAssunto() {
        return new Assunto(1L, "Tipos primitivos", MateriaFactory.createMateria());
    }

    public static AssuntoDTO createExistingAssuntoDTO() {
        return new AssuntoDTO(1L, "Tipos primitivos", MateriaFactory.createMateria());
    }

    public static AssuntoDTO createNonExistingAssuntoDTO() {
        return new AssuntoDTO(2L, "Orientação a objeto", MateriaFactory.createMateria());
    }
}
