package org.zavod.controller;


import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.zavod.model.AuthorEntity;
import org.zavod.model.Role;
import org.zavod.service.AuthorService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collections;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AuthorService authorService;

    public AdminController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping
    public ModelAndView admin(AuthorEntity authorEntity) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("author", authorEntity);
        modelAndView.addObject("authors", authorService.findAll());
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @PostMapping
    public ModelAndView create(@Valid @ModelAttribute("authorEntity") AuthorEntity authorEntity, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        AuthorEntity authorExists = authorService.findByUsername(authorEntity.getUsername());
        modelAndView.setViewName("admin");
//        modelAndView.addObject("authorEntity", authorEntity);

        if (authorExists != null) {
            bindingResult.rejectValue("username", "error.newAuthor", "Пользователь существует!");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errorMessage", "Ошибка регистрации!");
        } else {
            authorEntity.setRoles(Collections.singleton(Role.ROLE_USER));
            AuthorEntity createdAuthor = authorService.save(authorEntity);
            modelAndView.addObject("successMessage", "Пользователь успешно зарегистрирован!");
            modelAndView.addObject("createdAuthor", createdAuthor);
        }

        modelAndView.addObject("authors", authorService.findAll());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public String delete(@PathVariable("id") int id, @RequestParam("action") @NotNull String action) {
        if(action.equals("delete")) authorService.delete(id);
        return "redirect:/admin";
    }
}
