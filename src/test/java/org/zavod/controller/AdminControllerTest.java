package org.zavod.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.zavod.model.AuthorEntity;
import org.zavod.service.AuthorService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zavod.TestData.NEW_USER;
import static org.zavod.TestData.getNewUser;
import static org.zavod.TestUtil.assertMatch;

class AdminControllerTest extends AbstractControllerTest {

    @Autowired
    AuthorService authorService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin() throws Exception {
        mockMvc
                .perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"MANAGER", "USER"})
    void adminForbidden() throws Exception {
        mockMvc
                .perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("username", NEW_USER.getUsername());
        params.add("password", NEW_USER.getPassword());
        params.add("fullName", NEW_USER.getFullName());

        mockMvc.perform(post("/admin").params(params))
                .andExpect(status().isOk());

        AuthorEntity expected = getNewUser();
        AuthorEntity actual = authorService.findByUsername(NEW_USER.getUsername());
        expected.setId(actual.getId());
        assertMatch(actual, expected,"password");
    }

}