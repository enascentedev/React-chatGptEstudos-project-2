package br.acsousa.javagpt1.repositories.custons;

import br.acsousa.javagpt1.entities.Assunto;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AssuntoCustomRepository {

    private final EntityManager entityManager;

    public AssuntoCustomRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<Assunto> findByFilter(Long id, String nome, Long materiaId, Pageable pageable) {
        Sort sort = pageable.getSort();

        String orderBy = sort.stream()
                .map(order -> order.getProperty() + " " + order.getDirection().name())
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse("");

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT a FROM Assunto a WHERE 1=1 ");

        if (id != null) {
            sql.append("AND a.id = :id ");
        }

        if (nome != null) {
            sql.append("AND LOWER(a.nome) LIKE :nome ");
        }

        if (materiaId != null) {
            sql.append("AND a.materia.id = :materiaId ");
        }

        if (!"".equals(orderBy)) {
            sql.append("ORDER BY a." + orderBy);
        }

        var query = entityManager.createQuery(sql.toString(), Assunto.class);

        if (id != null) {
            query.setParameter("id", id);
        }

        if (nome != null) {
            query.setParameter("nome", "%" + nome + "%");
        }

        if (materiaId != null) {
            query.setParameter("materiaId", materiaId);
        }

        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        List<Assunto> assuntos = query.getResultList();
        long total = totalAssuntos();
        return new PageImpl<>(assuntos, pageable, total);
    }

    private long totalAssuntos() {
        String sql = "SELECT COUNT(a) FROM Assunto a";

        var query = entityManager.createQuery(sql, Long.class);

        return (Long) query.getSingleResult();
    }
}
