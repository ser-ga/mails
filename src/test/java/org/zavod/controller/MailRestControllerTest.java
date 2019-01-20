package org.zavod.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.zavod.model.MailEntity;
import org.zavod.service.MailService;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.zavod.TestData.*;
import static org.zavod.TestUtil.assertMatch;


class MailRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MailRestController.REST_URL + "/";

    @Autowired
    private MailService mailService;

    @Test
    void getAll() throws Exception {

        Gson gson = getGson();
        Type listType = new TypeToken<List<MailEntity>>() {
        }.getType();

        mockMvc.perform(get(REST_URL)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(gson.fromJson(result.getResponse().getContentAsString(), listType), getList(), "id", "author.id"))
                .andDo(print());
    }

    @Test
    void getById() throws Exception {

        Gson gson = getGson();

        mockMvc.perform(get(REST_URL + MAIL_ENTITY_4_ID)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertMatch(gson.fromJson(result.getResponse().getContentAsString(), MailEntity.class), MAIL_ENTITY_4, "author"))
                .andDo(print());

    }

    @Test
    void testSave() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("mailNumber", "17");
        params.add("createDate", "2019-01-15T23:29:23");
        params.add("mailRecipient", "recipient3");
        params.add("mailSubject", "subject6");
        params.add("mailText", "title6\\ntext6");

        mockMvc.perform(post(REST_URL).params(params)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isOk());

        MailEntity expected = getNew();
        expected.setId(MAIL_ENTITY_6_ID);
        MailEntity actual = mailService.findById(MAIL_ENTITY_6_ID);
        assertMatch(actual, expected, "updateDateTime","author");

    }

    @Test
    void testDelete() throws Exception {

        mockMvc.perform(delete(REST_URL + MAIL_ENTITY_4_ID)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNoContent());

        List<MailEntity> expectList = Lists.newArrayList(MAIL_ENTITY_1, MAIL_ENTITY_2, MAIL_ENTITY_3, MAIL_ENTITY_5);
        assertMatch(mailService.getAll(), expectList, "id", "author.id");

    }

    @Test
    void accept() throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("accept", "false");

        mockMvc.perform(post(REST_URL + MAIL_ENTITY_4_ID).params(params)
                .contentType(MediaType.APPLICATION_JSON)
                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD)))
                .andExpect(status().isNoContent());

        MailEntity mailEntity = mailService.findById(MAIL_ENTITY_4_ID);
        assertFalse(mailEntity.isAccept());

    }
}