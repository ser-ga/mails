package org.zavod;

import com.google.gson.*;
import org.assertj.core.util.Lists;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class TestData {

    private TestData() {
    }

    public static final long GLOBAL_SEQ = 10000;

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "passw";


    public static final AuthorEntity ADMIN = new AuthorEntity(GLOBAL_SEQ, "Global administrator", ADMIN_USERNAME, ADMIN_PASSWORD, true, Collections.singleton(Role.ROLE_ADMIN));
    public static final AuthorEntity MNGR = new AuthorEntity(GLOBAL_SEQ + 1, "Office manager", "mngr", "passw", true, Collections.singleton(Role.ROLE_MANAGER));
    public static final AuthorEntity EMPL = new AuthorEntity(GLOBAL_SEQ + 2, "Employer", "empl", "passw", true, Collections.singleton(Role.ROLE_USER));

    public static final long MAIL_ENTITY_1_ID = GLOBAL_SEQ + 3;
    public static final long MAIL_ENTITY_2_ID = GLOBAL_SEQ + 4;
    public static final long MAIL_ENTITY_3_ID = GLOBAL_SEQ + 5;
    public static final long MAIL_ENTITY_4_ID = GLOBAL_SEQ + 6;
    public static final long MAIL_ENTITY_5_ID = GLOBAL_SEQ + 7;

    public static final MailEntity MAIL_ENTITY_1 = new MailEntity(
            MAIL_ENTITY_1_ID,
            LocalDate.of(2018, 12, 15),
            LocalDateTime.of(2018, 12, 15, 10, 0),
            12,
            "recipient1",
            "subject1",
            "text1",
            "text1",
            true,
            1,
            MNGR);
    public static final MailEntity MAIL_ENTITY_2 = new MailEntity(
            MAIL_ENTITY_2_ID,
            LocalDate.of(2018, 12, 15),
            LocalDateTime.of(2018, 12, 15, 11, 0),
            13,
            "recipient2",
            "subject2",
            "text2",
            "text2",
            true,
            1,
            EMPL);
    public static final MailEntity MAIL_ENTITY_3 = new MailEntity(
            MAIL_ENTITY_3_ID,
            LocalDate.of(2018, 12, 15),
            LocalDateTime.of(2018, 12, 15, 12, 0),
            14,
            "recipient3",
            "subject3",
            "text3",
            "text3",
            true,
            1,
            MNGR);
    public static final MailEntity MAIL_ENTITY_4 = new MailEntity(
            MAIL_ENTITY_4_ID,
            LocalDate.of(2018, 12, 15),
            LocalDateTime.of(2018, 12, 15, 13, 0),
            15,
            "recipient2",
            "subject4",
            "text4",
            "text4",
            true,
            1,
            ADMIN);
    public static final MailEntity MAIL_ENTITY_5 = new MailEntity(
            MAIL_ENTITY_5_ID,
            LocalDate.of(2018, 12, 15),
            LocalDateTime.of(2018, 12, 15, 14, 0),
            16,
            "recipient3",
            "subject5",
            "text5",
            "text5",
            true,
            1,
            MNGR);


    public static List<MailEntity> getList() {
        return Lists.newArrayList(MAIL_ENTITY_1, MAIL_ENTITY_2, MAIL_ENTITY_3, MAIL_ENTITY_4, MAIL_ENTITY_5);
    }

    public static final long MAIL_ENTITY_6_ID = GLOBAL_SEQ + 8;

    public static MailEntity getNew() {
        return new MailEntity(MAIL_ENTITY_6_ID, LocalDate.parse("2019-01-15"), null, 17, "recipient3", "subject6", "mailTitle", "mailText",false, 1, null);
    }

    public static final AuthorEntity NEW_USER = new AuthorEntity(null, "new User", "new", "passw", true, Collections.singleton(Role.ROLE_USER));

    public static AuthorEntity getNewUser() {
        return new AuthorEntity(NEW_USER.getId(), NEW_USER.getFullName(), NEW_USER.getUsername(), NEW_USER.getPassword(), NEW_USER.isActive(), NEW_USER.getRoles());
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString());
                    }
                })
                .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                    @Override
                    public LocalDate deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        return LocalDate.parse(json.getAsString());
                    }
                })
                .addDeserializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
//                        return f.getName().toLowerCase().contains("author");
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> aClass) {
                        return false;
                    }
                })
                .create();
    }

}
