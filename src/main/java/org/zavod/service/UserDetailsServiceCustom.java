package org.zavod.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.zavod.AuthorizedUser;
import org.zavod.model.AuthorEntity;
import org.zavod.repository.AuthorRepository;

@Component("userDetailService")
public class UserDetailsServiceCustom implements UserDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(UserDetailsServiceCustom.class);

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
        LOG.info("{} is successfully login", username);
        return new AuthorizedUser(author);
    }
}
