package org.zavod.service;

import org.zavod.model.AuthorEntity;
import org.zavod.model.Role;
import org.zavod.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<AuthorEntity> findAll() {
        return authorRepository.getAllByOrderByIdAsc();
    }

    @Override
    public AuthorEntity findById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public AuthorEntity save(AuthorEntity author) {
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        author.setActive(true);
        author.setRoles(Collections.singleton(Role.ROLE_USER));
        return authorRepository.save(author);
    }

    @Override
    public void delete(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorEntity findByUsername(String username) {
        return authorRepository.findAuthorByUsername(username);
    }


}
