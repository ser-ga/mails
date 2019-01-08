package org.zavod.service;

import org.zavod.model.AuthorEntity;

import java.util.List;

public interface AuthorService {

    List<AuthorEntity> findAll();

    AuthorEntity findById(Long id);

    AuthorEntity save(AuthorEntity author);

    void delete(long id);

    AuthorEntity findByUsername(String username);

}
