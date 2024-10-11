package com.hana.hana_spring.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class EmailSender {
    public static void send_email(String to, String msg) throws EmailException {
        String host = "smtp.larksuite.com"; // 替换为飞书的SMTP服务器地址
        String from = "hana@fufu.moe";
        String username = "hana@fufu.moe";
        String password = "lw0lhfdtHuwK7dqk";

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
        log.info("邮箱发送成功");

    }

}
