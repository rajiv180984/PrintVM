package com.first.test.notification;
import com.sendgrid.*;
import java.io.IOException;

public class Example {
  public static void main(String[] args) throws IOException {
    Email from = new Email("rathor.rajeev@gmail.com");
    String subject = "Sending with SendGrid is Fun";
    Email to = new Email("rathor.rajeev@gmail.com");
    Content content = new Content("text/plain", "and easy to do anywhere, even with Java");
    Mail mail = new Mail(from, subject, to, content);

    SendGrid sg = new SendGrid("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");
    Request request = new Request();
    try {
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      Response response = sg.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
  }
}