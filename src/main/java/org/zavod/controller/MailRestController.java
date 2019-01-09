package org.zavod.controller;

import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;
import org.zavod.service.AuthorService;
import org.zavod.service.MailService;
import org.zavod.util.GeneratePdfReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
@RequestMapping(value = "/rest/mails")
public class MailRestController {

    private final MailService mailService;

    private final AuthorService authorService;

    @Autowired
    public MailRestController(MailService mailService, AuthorService authorService) {
        this.mailService = mailService;
        this.authorService = authorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MailEntity> getAll() {
        return mailService.findAll();
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public MailEntity getById(@PathVariable("id") Long id) {
        return mailService.findById(id);
    }

    @PostMapping
    public void save(@Validated MailEntity mailEntity, @AuthenticationPrincipal User user, HttpServletRequest request) {
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

    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> pdf(@PathVariable("id") Long id, @AuthenticationPrincipal User user) throws IOException {

        MailEntity mail = mailService.findById(id);
        AuthorEntity author = authorService.findByUsername(user.getUsername());
        boolean isUser = author.getRoles().contains(Role.ROLE_USER);
        if(isUser && !mail.isAccept()) {
            throw new AccessDeniedException("Mail with id = " + id + " is not accepted by MANAGER");
        }

        ByteArrayInputStream bis = GeneratePdfReport.mailsReport(mail);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=mail"+ id +".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}
