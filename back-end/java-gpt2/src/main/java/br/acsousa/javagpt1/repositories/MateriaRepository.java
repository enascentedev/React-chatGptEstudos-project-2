package br.acsousa.javagpt1.repositories;

import br.acsousa.javagpt1.entities.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MateriaRepository extends JpaRepository<Materia, Long>{

    Materia findByNome(String nome);
}
