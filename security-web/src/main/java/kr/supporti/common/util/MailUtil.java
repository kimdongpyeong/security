package kr.supporti.common.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import kr.supporti.api.app.dto.MailDto;

@Component
public class MailUtil {

    @Value("${spring.mail.username}")
    private String FROM_EMAIL;

    @Value("${spring.mail.password}")
    private String FROM_PASSWORD;

    public void sendMail(MailDto mailDto) throws MessagingException, IOException {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.smtp.host", "smtp.gmail.com");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.port", "587");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
            }
        });
        DataHandler dataHandler = null;
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setFrom(mailDto.getFrom());
        List<String> toList = mailDto.getToList();
        if (toList != null) {
            for (int i = 0; i < toList.size(); i++) {
                mimeMessage.addRecipients(RecipientType.TO, toList.get(i));
            }
        }
        List<String> ccList = mailDto.getCcList();
        if (ccList != null) {
            for (int i = 0; i < ccList.size(); i++) {
                mimeMessage.addRecipients(RecipientType.CC, ccList.get(i));
            }
        }
        mimeMessage.setSubject(mailDto.getSubject());
        Multipart multipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mailDto.getText(), "text/html;charset=UTF-8");
        multipart.addBodyPart(mimeBodyPart);
//        mimeBodyPart = new MimeBodyPart();
//        mimeBodyPart.setDataHandler(new DataHandler(imageFile));
//        mimeBodyPart.setHeader("Content-ID", "<image>");
//        multipart.addBodyPart(mimeBodyPart);
        if (mailDto.getFileData() != null && mailDto.getFileName() != null) {
            dataHandler = new DataHandler(mailDto.getFileData());
            mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setDataHandler(dataHandler);
            mimeBodyPart.setDisposition(Part.ATTACHMENT);
            mimeBodyPart.setFileName(mailDto.getFileName());
            multipart.addBodyPart(mimeBodyPart);
        }
        mimeMessage.setContent(multipart);
        Transport.send(mimeMessage);
    }

}
