package com.brightywe.brightylist.email;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class EmailDto {
    
    @NotBlank
    @Email
    String from;
    
    @NotBlank
    @Email 
    String to;
    
    @NotBlank
    String subject;
    
    @NotBlank
    String content;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
