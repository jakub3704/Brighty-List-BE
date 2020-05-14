/****************************************************************************
 * Copyright 2020 Jakub Koczur
 *
 * Unauthorized copying of this project, via any medium is strictly prohibited.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES  
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,  
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE 
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * 
 *****************************************************************************/

package com.brightywe.brightylist.email.model.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * Class EmailQueue as database entity for storing reminder e-Mails to be sent via The Twilio SendGrid.
 */
@Entity(name = "email_queue")
public class EmailQueue implements EmailDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @NotBlank
    @Email
    @Column(name = "from_email")
    String fromEmail;

    @NotBlank
    @Email
    @Column(name = "to_email")
    String toEmail;

    @NotBlank
    @Column(name = "subject")
    String subject;

    @NotBlank
    @Column(name = "content")
    String content;

    @NotNull
    @Column(name = "sent")
    boolean sent;

    @NotNull
    @Column(name = "failure_count")
    Integer failureCount;

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public void addFailureCount() {
        ++this.failureCount;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "EmailQueue [id=" + id + ", from=" + fromEmail + ", to=" + toEmail + ", subject=" + subject
                + ", content=" + content + ", sent=" + sent + ", failureCount=" + failureCount + "]";
    }

}
