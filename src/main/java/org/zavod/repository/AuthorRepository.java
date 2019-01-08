package org.zavod.repository;

import org.zavod.model.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {

    AuthorEntity findAuthorByUsername(String username);

    List<AuthorEntity> getAllByOrderByIdAsc();

    @Override
    void deleteById(Long aLong);

}

