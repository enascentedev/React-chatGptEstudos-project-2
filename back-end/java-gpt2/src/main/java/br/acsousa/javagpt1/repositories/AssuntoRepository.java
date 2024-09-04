package br.acsousa.javagpt1.repositories;

import br.acsousa.javagpt1.entities.Assunto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssuntoRepository extends JpaRepository<Assunto, Long>{

    List<Assunto> findByMateriaId(Long id);

    Assunto findByMateriaIdAndNome(Long id, String nome);
}
