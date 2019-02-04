package org.zavod.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.zavod.model.AuthorEntity;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    AuthorEntity findAuthorByUsername(String username);

    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.LOAD)
    List<AuthorEntity> getAllByOrderByIdAsc();

    @Override
    void deleteById(Long aLong);

}

