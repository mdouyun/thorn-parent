/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.thorn.common.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送类.
 *
 * @author chenyun.chris
 * @since 1.0
 */
public class MailSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailSender.class);

    /** 发件人邮箱 */
    private Contacts sender;

    /** 邮箱账号 */
    private String userName;

    /** 邮箱密码 */
    private String password;

    /** 邮件服务器Host */
    private String mailHost;

    /** 是否需要服务器认证 */
    private boolean needAuth = true;

    /** 邮件发送器 */
    private Transport transport;

    /** 会话 */
    private Session session;

    /**
     * 初始化会话属性。
     */
    private void initSession() {

        if (this.session != null) {
            return;
        }

        // 系统属性
        Properties props = System.getProperties();
        String smtp_host = "mail.smtp.host";
        String smtp_auth = "mail.smtp.auth";

        // 设置SMTP主机
        props.put(smtp_host, this.mailHost);

        if (this.needAuth) {
            props.put(smtp_auth, "true");

            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };

            this.session = Session.getInstance(props, auth);
        } else {
            props.put(smtp_auth, "false");
            this.session = Session.getInstance(props);
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("初始化连接器会话Session完成，host[{}]，", props.get(smtp_host));
        }

    }

    /**
     * 初始化发送器，依赖于会话创建。
     * @throws MessagingException
     */
    private void initTransport() throws MessagingException {
        if (this.transport != null && this.transport.isConnected()) {
            return;
        }

        if(LOGGER.isDebugEnabled()) {
            LOGGER.debug("初始化邮件发送器，state[{}]", this.transport.isConnected());
        }

        this.initSession();
        this.transport = this.session.getTransport("smtp");
        this.transport.connect(this.mailHost, this.userName, this.password);
    }


    /**
     * 发送一封电子邮件，不开启调试模式。
     *
     * @param email  需要发送的邮件数据
     * @return  发送是否成功
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public boolean send(Email email) throws UnsupportedEncodingException, MessagingException {
        return this.send(email, false);
    }

    /**
     * 发送一封电子邮件。
     *
     * @param email  需要发送的邮件数据
     * @param isDebug  是否开启debug模式
     * @return  发送是否成功
     * @throws UnsupportedEncodingException
     * @throws MessagingException
     */
    public boolean send(Email email, boolean isDebug) throws MessagingException, UnsupportedEncodingException {

        if(isDebug) {
            LOGGER.info("开始发送邮件，address[{}]，subject[{}]", email.getAttAddress(), email.getSubject());
        }

        if(email.getReceivers() == null || email.getReceivers().size() == 0) {
            LOGGER.warn("邮件收件人为空, 发送失败。");
            return false;
        }

        this.initTransport();

        this.session.setDebug(isDebug);

        MimeMessage message = new MimeMessage(this.session);
        // 设置发件人
        message.setFrom(new InternetAddress(this.sender.getAddress(), this.sender.getName()));
        // 设置发件日期
        message.setSentDate(new Date());


        // 设置邮件接收人
        InternetAddress[] recipients = new InternetAddress[email.getReceivers().size()];
        if(isDebug) {
            LOGGER.info("设置邮件收件人, 收件人数量: {}", recipients.length);
        }
        for (int i = 0; i < recipients.length; i++) {
            Contacts contacts = email.getReceivers().get(i);
            recipients[i] = new InternetAddress(contacts.getAddress(), contacts.getName());
        }
        message.addRecipients(Message.RecipientType.TO, recipients);

        if(email.getCopiers() == null || email.getCopiers().size() == 0) {
            // 设置邮件抄送人
            InternetAddress[] copies = new InternetAddress[email.getCopiers().size()];

            if(isDebug) {
                LOGGER.info("设置邮件抄送人, 抄送人数量: {}", copies.length);
            }

            for (int i = 0; i < copies.length; i++) {
                Contacts contacts = email.getCopiers().get(i);
                copies[i] = new InternetAddress(contacts.getAddress(), contacts.getName());
            }
            message.addRecipients(Message.RecipientType.CC, copies);
        }

        // 设置邮件主题
        message.setSubject(email.getSubject());

        Multipart multipart = new MimeMultipart();
        // 设置邮件内容，为html语法格式
        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(email.getContent(), "text/html;charset=UTF-8");
        multipart.addBodyPart(textPart);


        if(email.getAttAddress() == null || email.getAttAddress().size() == 0) {

            if(isDebug) {
                LOGGER.info("设置邮件附件, 附件数量: {}", email.getAttAddress().size());
            }

            // 设置邮件附件
            for (File file : email.getAttAddress()) {
                MimeBodyPart contentPart = new MimeBodyPart();
                FileDataSource fds = new FileDataSource(file);

                contentPart.setDataHandler(new DataHandler(fds));
                contentPart.setFileName(fds.getName());

                multipart.addBodyPart(contentPart);
            }
        }

        message.setContent(multipart);
        message.saveChanges();
        // 发送邮件
        this.transport.sendMessage(message, message.getAllRecipients());

        if(isDebug) {
            LOGGER.info("邮件发送成功。");
        }
        return true;
    }

    /**
     * 关闭邮件发送器连接.
     */
    public void close() {
        if (this.transport != null && this.transport.isConnected()) {
            try {
                this.transport.close();
            } catch (MessagingException e) {
                LOGGER.error("关闭邮箱连接失败。", e);
            }
        }
    }

    public MailSender(Contacts sender, String userName, String password,
                      String mailHost) {
        this.sender = sender;
        this.userName = userName;
        this.password = password;
        this.mailHost = mailHost;
    }

    public MailSender(Contacts sender, String userName, String password,
                      String mailHost, boolean needAuth) {
        this(sender, userName, password, mailHost);
        this.needAuth = needAuth;
    }

}
