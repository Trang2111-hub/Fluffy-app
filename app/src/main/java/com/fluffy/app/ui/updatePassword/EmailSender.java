package com.fluffy.app.ui.updatePassword;

import android.se.omapi.Session;

import java.net.PasswordAuthentication;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String recipientEmail, String otp) {
        String host = "smtp.gmail.com";
        final String user = "your-email@gmail.com";  // Địa chỉ email của bạn
        final String password = "your-email-password";  // Mật khẩu của email

        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Mã OTP của bạn");
            message.setText("Mã OTP của bạn là: " + otp);

            Transport.send(message);
            System.out.println("OTP đã được gửi qua email!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
