package org.zavod.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.zavod.AuthorizedUser;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.repository.AuthorRepository;
import org.zavod.repository.MailRepository;
import org.zavod.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.zavod.util.ValidationUtil.checkAccessUpdate;

@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailServiceImpl.class);

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
    public void save(MailEntity mail, long authorId) {
        AuthorEntity author = authorRepository.getOne(authorId);
        mail.setAuthor(author);
        mailRepository.save(mail);
        LOG.info("Save new MailEntity, authorId={}", authorId);
    }

    @Override
    public MailEntity findById(Long id) {
        return mailRepository.getById(id);
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void update(MailEntity mail, AuthorizedUser authUser) {
        MailEntity existing = mailRepository.findById(mail.getId()).orElseThrow(() -> new NotFoundException("Not found for update"));
        checkAccessUpdate(existing, authUser);
        mail.setAccept(false);
        mail.setUpdateDateTime(LocalDateTime.now());
        mail.setAuthor(existing.getAuthor());
        mailRepository.save(mail);
        LOG.info("Update MailEntity with id={}, authorId={}", mail.getId(), authUser.getId());
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void delete(Long id) {
        mailRepository.deleteById(id);
        LOG.info("Delete MailEntity with id={}", id);
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void accept(Long id, boolean accept) {
        MailEntity existing = mailRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found for update"));
        existing.setAccept(accept);
        existing.setUpdateDateTime(LocalDateTime.now());
        mailRepository.save(existing);
        LOG.info("Accept status {} for MailEntity with id={}", accept, id);
    }

    @Override
    @CacheEvict(value = "mailCache", allEntries = true)
    public void changeAuthor(MailEntity mail, long authorId) {
        AuthorEntity authorEntity = authorRepository.getOne(authorId);
        mail.setAuthor(authorEntity);
        mail.setUpdateDateTime(LocalDateTime.now());
        mailRepository.save(mail);
        LOG.info("Change author for MailEntity with id={}", mail.getId());
    }
}
