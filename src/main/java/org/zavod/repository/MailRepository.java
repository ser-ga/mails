package org.zavod.repository;

import org.springframework.data.jpa.repository.Query;
import org.zavod.model.MailEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<MailEntity, Long> {

    @Override
    void deleteById(Long aLong);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    MailEntity getById(Long id);

    @Query("SELECT m FROM MailEntity m JOIN FETCH m.author")
    List<MailEntity> getAll();
}
