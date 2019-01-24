package org.zavod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


//https://stackoverflow.com/questions/38747548/spring-boot-disable-error-mapping
@SpringBootApplication (exclude = {ErrorMvcAutoConfiguration.class })
public class MailApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }
}

