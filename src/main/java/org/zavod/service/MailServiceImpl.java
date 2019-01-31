package org.zavod.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.repository.AuthorRepository;
import org.zavod.repository.MailRepository;

import java.util.List;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    public MailRepository mailRepository;

    @Autowired
    public AuthorRepository authorRepository;

    @Override
    @Cacheable("mailCache")
    public List<MailEntity> getAll() {
        return mailRepository.getAll();
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void save(MailEntity newMail) {
        mailRepository.save(newMail);
    }

    @Override
    public MailEntity findById(Long id) {
        return mailRepository.getById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mailCache", allEntries = true)
    public void update(MailEntity mail, long authorId, boolean isUser) {
        MailEntity existing = mailRepository.getById(mail.getId());
        if (existing != null) {
            AuthorEntity author = existing.getAuthor();
            if (isUser && (author.getId() != authorId || existing.isAccept())) {
                throw new AccessDeniedException("Access Denied for USER");
            }
            mail.setVersion(existing.getVersion() + 1);
            mail.setId(existing.getId());
            mail.setAuthor(author);
            mailRepository.save(mail);
        }
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void delete(Long id) {
        mailRepository.deleteById(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "mailCache", allEntries = true)
    public void accept(Long id, boolean accept) {
        MailEntity existing = mailRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setAccept(accept);
            mailRepository.save(existing);
        }
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void changeAuthor(MailEntity mail, long authorId) {
        AuthorEntity authorEntity = authorRepository.getOne(authorId);
        mail.setAuthor(authorEntity);
        mailRepository.save(mail);
    }
}
