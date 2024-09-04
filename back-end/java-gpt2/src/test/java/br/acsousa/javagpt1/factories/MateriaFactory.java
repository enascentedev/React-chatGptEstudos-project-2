package br.acsousa.javagpt1.factories;

import br.acsousa.javagpt1.dtos.MateriaDTO;
import br.acsousa.javagpt1.entities.Materia;

public class MateriaFactory {

    public static Materia createMateria(){
       return new Materia(1L, "Java");
    }

    public static MateriaDTO createExistingMateriaDTO(){
        return new MateriaDTO(1L, "Java");
    }

    public static MateriaDTO createNonExistingMateriaDTO(){
        return new MateriaDTO(2L, "JavaScript");
    }
}
