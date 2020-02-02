package org.zavod.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.zavod.AuthorizedUser;
import org.zavod.model.AuthorEntity;
import org.zavod.repository.AuthorRepository;

@Slf4j
@Component("userDetailService")
public class UserDetailsServiceCustom implements UserDetailsService {

    private final AuthorRepository authorRepository;

    public UserDetailsServiceCustom(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public AuthorizedUser loadUserByUsername(String username) {
        AuthorEntity author = authorRepository.findAuthorByUsername(username);
        if (author == null) {
            throw new UsernameNotFoundException(username);
        }
        log.info("{} is successfully login", username);
        return new AuthorizedUser(author);
    }
}
