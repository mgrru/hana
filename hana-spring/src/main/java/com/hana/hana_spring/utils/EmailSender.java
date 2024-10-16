package com.hana.hana_spring.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Data
@ConfigurationProperties("email")
@Primary
public class EmailSender {

    private String host;
    private String from;
    private String username;
    private String password;

    public void send_email(String to, String msg) throws EmailException {

        SimpleEmail email = new SimpleEmail();
        email.setHostName(host);
        email.setSmtpPort(587); // 使用TLS
        email.setAuthentication(username, password);
        email.setStartTLSRequired(true);
        email.setFrom(from);

        email.addTo(to);
        email.setSubject("修改密码验证码");
        email.setMsg(msg);
        email.send();
        log.info("Email successfully sent!");

    }

}
