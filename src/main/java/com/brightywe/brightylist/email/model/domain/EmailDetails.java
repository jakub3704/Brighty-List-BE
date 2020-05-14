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
/**
 * Interface EmailDetails for basic e-Mail entity in database.
 */
public interface EmailDetails {
    
    public Long getId();

    public String getFromEmail();

    public void setFromEmail(String fromEmail);

    public String getToEmail();

    public void setToEmail(String toEmail);

    public String getSubject();

    public void setSubject(String subject);

    public String getContent();

    public void setContent(String content);

    public boolean isSent();

    public void setSent(boolean sent);

    public Integer getFailureCount();

    public void setFailureCount(Integer failureCount);

    public void addFailureCount();

}
