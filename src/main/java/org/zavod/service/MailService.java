package org.zavod.service;

import org.zavod.model.MailEntity;

import java.util.List;

public interface MailService {


    List<MailEntity> getAll();

    void save(MailEntity newMail);

    MailEntity findById(Long id);

    void update(MailEntity mail, long authorId, boolean isUser);

    void delete(Long id);

    void accept(Long id, boolean accept);

}
