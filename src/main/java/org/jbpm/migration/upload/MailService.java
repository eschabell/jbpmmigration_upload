/**
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package org.jbpm.migration.upload;

import org.apache.log4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Service for sending mail messages, through a provider that offers this service by SMTP over SSL.
 */
@Stateless
public class MailService {
    private static final String MAIL_CONFIG_HOST = "mail.provider.host";

    private static final String MAIL_CONFIG_PORT = "mail.provider.port";

    private static final String MAIL_CONFIG_USER_NAME = "mail.userName";

    private static final String MAIL_CONFIG_PASSWORD = "mail.password";

    private static final String MAIL_CONFIG_FROM = "mail.from.address";

    private static final String MAIL_CONFIG_TO = "mail.to.address";

    private static final String MAIL_CONFIG_SUBJECT = "mail.subject";

    private static final String MAIL_CONFIG_BODY = "mail.body";

    @Inject
    @Resources.MailConfig
    private Properties mailConfig;

    @Inject
    private Logger log;

    public void sendMail(final String jpdl, final String bpmn) {
        log.info("Sending mail...");

        final Properties mailProps = new Properties();
        mailProps.put("mail.smtp.host", mailConfig.getProperty(MAIL_CONFIG_HOST));
        mailProps.put("mail.smtp.socketFactory.port", mailConfig.getProperty(MAIL_CONFIG_PORT));
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.port", mailConfig.getProperty(MAIL_CONFIG_PORT));

        final Session session = Session.getDefaultInstance(mailProps, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.getProperty(MAIL_CONFIG_USER_NAME), mailConfig
                        .getProperty(MAIL_CONFIG_PASSWORD));
            }
        });

        try {
            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailConfig.getProperty(MAIL_CONFIG_FROM)));
            message.setRecipients(Message.RecipientType.TO, InternetAddress
                    .parse(mailConfig.getProperty(MAIL_CONFIG_TO)));
            message.setSubject(mailConfig.getProperty(MAIL_CONFIG_SUBJECT));
            message.setText(MessageFormat.format(mailConfig.getProperty(MAIL_CONFIG_BODY), Resources
                    .convertToNLDate(new Date()), jpdl, bpmn));

            Transport.send(message);

            log.info("Mail sent successfully.");
        } catch (final MessagingException msgEx) {
            log.error("Unable to send mail !", msgEx);
        }
    }
}
