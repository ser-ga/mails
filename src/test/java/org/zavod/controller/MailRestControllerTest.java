package org.zavod.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.zavod.model.MailEntity;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zavod.TestData.*;
import static org.zavod.TestUtil.assertMatch;


class MailRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MailRestController.REST_URL + "/";

    @Test
    void getAll() {
    }

    @Test
    void getById() throws Exception {

        Gson gson = getGson();

        mockMvc.perform(get(REST_URL + 10006)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(gson.fromJson(result.getResponse().getContentAsString(), MailEntity.class), MAIL_ENTITY_4, "author"))
                .andDo(print());
    }

    @Test
    void save() throws Exception {
    }

    @Test
    void delete() {
    }

    @Test
    void accept() {
    }
}