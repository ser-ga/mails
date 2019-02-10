package org.zavod;


import org.zavod.model.AuthorEntity;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    private static final long serialVersionUID = 1L;

    private AuthorEntity author;

    public AuthorizedUser(AuthorEntity author) {
        super(author.getUsername(), author.getPassword(), author.isActive(), true, true, true, author.getRoles());
        this.author = author;
    }

    public long getId() {
        return author.getId();
    }

    public void update(AuthorEntity author) {
        this.author = author;
    }

    public AuthorEntity getUser() {
        return author;
    }

    @Override
    public String toString() {
        return author.toString();
    }
}