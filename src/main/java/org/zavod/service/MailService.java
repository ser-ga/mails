package org.zavod.service;

import org.zavod.AuthorizedUser;
import org.zavod.model.MailEntity;

import java.util.List;

public interface MailService {

    List<MailEntity> getAll();

    void save(MailEntity mail, long authorId);

    MailEntity findById(Long id);

    void update(MailEntity mail, AuthorizedUser authUser);

    void delete(Long id);

    void accept(Long id, boolean accept);

    void changeAuthor(MailEntity mail, long authorId);

}
