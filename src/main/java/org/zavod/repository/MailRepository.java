package org.zavod.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zavod.model.MailEntity;

import java.util.List;

public interface MailRepository extends JpaRepository<MailEntity, Long> {

    @Override
    void deleteById(Long aLong);

    @EntityGraph(attributePaths = {"author", "signatory"}, type = EntityGraph.EntityGraphType.LOAD)
    MailEntity getById(Long id);

    @EntityGraph(attributePaths = {"author", "signatory"}, type = EntityGraph.EntityGraphType.LOAD)
    List<MailEntity> findAll();
}
