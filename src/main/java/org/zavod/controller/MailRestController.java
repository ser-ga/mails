package org.zavod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;
import org.zavod.service.AuthorService;
import org.zavod.service.MailService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static org.zavod.controller.MailRestController.REST_URL;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
@RequestMapping(value = REST_URL)
public class MailRestController {

    public static final String REST_URL = "/rest/mails";

    private final MailService mailService;

    private final AuthorService authorService;

    @Autowired
    public MailRestController(MailService mailService, AuthorService authorService) {
        this.mailService = mailService;
        this.authorService = authorService;
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
    public void save(@Validated MailEntity mailEntity, @AuthenticationPrincipal User user) {
        AuthorEntity author = authorService.findByUsername(user.getUsername());
        mailEntity.setAccept(false);
        mailEntity.setUpdateDateTime(LocalDateTime.now());
        if (mailEntity.getId() == null) {
            mailEntity.setAuthor(author);
            mailService.save(mailEntity);
        } else {
            boolean isUser = author.getRoles().contains(Role.ROLE_USER);
            mailService.update(mailEntity, author.getId(), isUser);
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
}
