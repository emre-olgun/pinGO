package org.oem.pinggo;

import org.junit.jupiter.api.Test;
import org.oem.pinggo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;
import java.util.ResourceBundle;

@SpringBootTest
public class Denemeler {
    @Autowired
    private MailService mailService;

    @Test
    public void notNull(){

        Department d=new Department(
1L,"namm","ress");
        System.out.println(d);

    }


    @Test
    public void ss(){

        ResourceBundle bundle
                = ResourceBundle.getBundle("messages", Locale.forLanguageTag("en-US-x-lvariant-POSIX"));
        System.out.println(Locale.forLanguageTag("ja-JP-x-lvariant-JP").getDisplayLanguage());

        System.out.println("<<"+bundle.getLocale().getCountry()+">>");

        String message = bundle.getString("hello");
        System.out.println(message);

    }


    @Test
    public void sender(){

//mailService.sendEmail("oertugrulmuhit@gmail.com","...","...");

    }

}
