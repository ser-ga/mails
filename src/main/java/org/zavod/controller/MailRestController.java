package org.zavod.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zavod.AuthorizedUser;
import org.zavod.model.MailEntity;
import org.zavod.service.MailService;

import java.util.List;

import static org.zavod.controller.MailRestController.REST_URL;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
@RequestMapping(value = REST_URL)
public class MailRestController {

    public static final String REST_URL = "/rest/mails";

    private final MailService mailService;

    public MailRestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MailEntity> getAll() {
        return mailService.getAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MailEntity getById(@PathVariable("id") Long id) {
        return mailService.findById(id);
    }

    @PostMapping
    public void save(@Validated MailEntity mailEntity, @AuthenticationPrincipal AuthorizedUser authUser) {
        if (mailEntity.getId() == null) {
            mailService.save(mailEntity, authUser.getId());
        } else {
            mailService.update(mailEntity, authUser);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        mailService.delete(id);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void accept(@PathVariable("id") Long id, @RequestParam("accept") boolean accept) {
        mailService.accept(id, accept);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/{id}/author")
    public void changeAuthor(@PathVariable("id") long mailId, @RequestParam("authorId") long authorId) {
        MailEntity mail = mailService.findById(mailId);
        if (!mail.isAccept() && mail.getAuthor().getId() != authorId) {
            mailService.changeAuthor(mail, authorId);
        } else throw new AccessDeniedException("Don't changed Entity with id=" + mailId);
    }
}
