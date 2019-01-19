package org.zavod.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zavod.TestData.ADMIN_PASSWORD;
import static org.zavod.TestData.ADMIN_USERNAME;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void loginAvailableForAll() throws Exception {
        mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    void adminCanLogin() throws Exception {
        mockMvc
                .perform(formLogin().user(ADMIN_USERNAME).password(ADMIN_PASSWORD))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/home"))
                .andExpect(authenticated().withUsername(ADMIN_USERNAME))
                .andDo(print());

        mockMvc
                .perform(logout())
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void index() {
    }

    @Test
    void login() {
    }

    @Test
    void home() {
    }

    @Test
    void pdf() {
    }
}