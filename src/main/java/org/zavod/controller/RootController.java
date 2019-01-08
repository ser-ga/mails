package org.zavod.controller;

import org.zavod.model.AuthorEntity;
import org.zavod.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@RequestMapping(value = "/")
public class RootController {

    private final AuthorService authorService;

    @Autowired
    public RootController(AuthorService authorService) {
        this.authorService = authorService;
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
}
