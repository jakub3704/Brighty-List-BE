package com.brightywe.brightylist.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private String sendgridKey = System.getenv("SENDGRID_API_KEY");

    public EmailDto send(EmailDto mailDto) throws IOException {
        final Email from = new Email(mailDto.getFrom());
        final Email to = new Email(mailDto.getTo());
        final String subject = mailDto.getSubject();
        final Content content = new Content("text/plain", mailDto.getContent());

        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
            return mailDto;
        } catch (IOException ex) {
            throw ex;
        }
    }
}