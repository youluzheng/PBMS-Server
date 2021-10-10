package org.pbms.pbmsserver.service.common;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 邮件服务
 *
 * @author 王俊
 * @date 2021/09/15
 * @since 0.3.0
 */
@Component
public class MailService {
    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Resource
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;
    @Autowired
    private TemplateEngine templateEngine;
    private final int retryTimes = 3;

    /**
     * 异步发送邮件，根据<code>retryTimes</code>重试，全部失败后不做处理
     *
     * @param subject  邮件主题
     * @param receiver 收件方
     * @param template 邮件模板路径（为目录<code>resources/templates</code>下文件）
     * @param context  模板内容
     */
    @Async
    public void sendMail(String subject, String receiver, String template, Context context) {
        String html = convertToHtml(template, context);
        for (int i = 0; i < retryTimes; i++) {
            try {
                sendHtmlMail(this.sender, subject, receiver, html);
                log.info("发送邮件至{}", receiver);
                break;
            } catch (MessagingException e) {
                log.error("邮件第{}次发送失败", i + 1, e);
            }
        }
    }

    private String convertToHtml(String template, Context context) {
        return this.templateEngine.process(template, context);
    }

    private void sendHtmlMail(String sender, String subject, String receiver, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(sender);
        helper.setTo(receiver);
        helper.setSubject(subject);
        helper.setText(content, true);
        javaMailSender.send(message);
        log.info("邮件发送成功");
    }
}
