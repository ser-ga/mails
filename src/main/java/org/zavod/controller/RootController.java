package org.zavod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;
import org.zavod.service.AuthorService;
import org.zavod.service.MailService;
import org.zavod.util.IPdfReport;

import java.time.LocalDate;

@Controller
@RequestMapping(value = "/")
public class RootController {

    private final AuthorService authorService;

    private final MailService mailService;

    private final IPdfReport pdfReport;

    @Autowired
    public RootController(AuthorService authorService, MailService mailService, IPdfReport pdfReport) {
        this.authorService = authorService;
        this.mailService = mailService;
        this.pdfReport = pdfReport;
    }

    @GetMapping
    public String index() {
        return "redirect:/login";

    }

    @GetMapping("/login")
    public ModelAndView login() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("auth", auth);
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @GetMapping("/home")
    public ModelAndView home(@AuthenticationPrincipal User user) {
        ModelAndView modelAndView = new ModelAndView();
        AuthorEntity currentAuthor = authorService.findByUsername(user.getUsername());
        modelAndView.addObject("authUser", user);
        modelAndView.addObject("fullName", currentAuthor.getFullName());
        modelAndView.addObject("authorId", currentAuthor.getId());
        modelAndView.setViewName("home");
        modelAndView.addObject("date", LocalDate.now());
        return modelAndView;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    @GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity pdf(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {

        MailEntity mail = mailService.findById(id);
        AuthorEntity author = authorService.findByUsername(user.getUsername());
        boolean isUser = author.getRoles().contains(Role.ROLE_USER);
        if (isUser && !mail.getAuthor().getId().equals(author.getId())) {
            throw new AccessDeniedException("Access level not lower than MANAGER is required");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=mail" + id + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfReport.create(mail));
    }
}
