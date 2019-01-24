package org.zavod.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zavod.model.AuthorEntity;
import org.zavod.service.AuthorService;

import java.util.List;

import static org.zavod.controller.AuthorRestController.REST_URL;

@RestController
@RequestMapping(value = REST_URL)
public class AuthorRestController {

    public static final String REST_URL = "/rest/authors";

    @Autowired
    private AuthorService authorService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AuthorEntity> getAll() {
        return authorService.findAll();
    }

}
