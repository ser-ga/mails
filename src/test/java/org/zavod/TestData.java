package org.zavod;

import com.google.gson.*;
import org.zavod.model.AuthorEntity;
import org.zavod.model.MailEntity;
import org.zavod.model.Role;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collections;

public class TestData {

    private TestData() {
    }

    public static final long GLOBAL_SEQ = 10000;

    public static final String ADMIN_USERNAME = "admin";
    public static final String ADMIN_PASSWORD = "passw";


    public static final AuthorEntity ADMIN = new AuthorEntity(GLOBAL_SEQ, "Global administrator", ADMIN_USERNAME, ADMIN_PASSWORD,  true, Collections.singleton(Role.ROLE_ADMIN), null);

    public static final MailEntity MAIL_ENTITY_4 = new MailEntity(
            GLOBAL_SEQ + 6,
            LocalDateTime.of(2018, 12, 15, 13, 0),
            LocalDateTime.of(2018, 12, 15, 13, 0),
            15,
            "recipient2",
            "subject4",
            "text4",
            true,
            1,
            ADMIN);

    public static Gson getGson() {
        return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                return LocalDateTime.parse(json.getAsString());
            }
        }).create();
    }

}
