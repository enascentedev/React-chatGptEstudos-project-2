package br.acsousa.javagpt1.repositories.custons;

import br.acsousa.javagpt1.entities.Materia;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MateriaCustomRepository {

    @Autowired
    private EntityManager entityManager;

    public Page<Materia> findByFilter(Long id, String nome, Pageable pageable) {
        Sort sort = pageable.getSort();

        String orderBy = sort.stream()
                .map(order -> order.getProperty() + " " + order.getDirection().name())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT m FROM Materia m WHERE 1=1 ");

        if (id != null) {
            sql.append("AND m.id = :id ");
        }

        if (nome != null) {
            sql.append("AND LOWER(m.nome) LIKE :nome ");
        }

        if (!orderBy.isEmpty()) {
            sql.append("ORDER BY m.").append(orderBy);
        }

        var query = entityManager.createQuery(sql.toString(), Materia.class);

        if (id != null) {
            query.setParameter("id", id);
        }

        if (nome != null) {
            query.setParameter("nome", "%" + nome + "%");
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Materia> materias = query.getResultList();
        long total = totalMaterias();
        return new PageImpl<>(materias, pageable, total);
    }

    private long totalMaterias() {
        String sql = "SELECT COUNT(m) FROM Materia m";

        var query = entityManager.createQuery(sql, Long.class);

        return (Long) query.getSingleResult();
    }
}
