package org.zavod.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zavod.model.AuthorEntity;
import org.zavod.model.Role;
import org.zavod.repository.AuthorRepository;
import org.zavod.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;


@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Cacheable("authorCache")
    public List<AuthorEntity> findAll() {
        return authorRepository.getAllByOrderByIdAsc();
    }

    @Override
    public AuthorEntity findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found author with id=" + id));
    }

    @Override
    @CacheEvict(value = "authorCache", allEntries = true)
    public AuthorEntity save(AuthorEntity author) {
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        author.setActive(true);
        author.setRoles(Collections.singleton(Role.ROLE_USER));
        return authorRepository.save(author);
    }

    @Override
    @CacheEvict(value = "authorCache", allEntries = true)
    public void delete(long id) {
        authorRepository.deleteById(id);
    }

    @Override
    public AuthorEntity findByUsername(String username) {
        return authorRepository.findAuthorByUsername(username);
    }

}
