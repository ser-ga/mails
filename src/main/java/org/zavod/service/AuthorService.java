package org.zavod.service;

import org.zavod.model.AuthorEntity;

import java.util.List;

public interface AuthorService {

    List<AuthorEntity> findAll();

    List<AuthorEntity> findSignatories();

    AuthorEntity findById(Long id);

    AuthorEntity save(AuthorEntity author);

    void delete(long id);

    AuthorEntity findByUsername(String username);

    void setSignatory(Long id);

}
