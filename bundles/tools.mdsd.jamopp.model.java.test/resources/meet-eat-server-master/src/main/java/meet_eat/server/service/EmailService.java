package meet_eat.server.service;

import meet_eat.data.entity.user.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Represents a service class providing email sending functionality.
 */
@Service
public class EmailService {

    private final JavaMailSender emailSender;

    /**
     * Constructs a new instance of {@link EmailService}.
     *
     * @param emailSender the mail sender used for email delivery
     */
    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /**
     * Sends a simple email containing subject and text to a recipient.
     *
     * @param sender    the sender of the email
     * @param recipient the recipient of the email
     * @param subject   the subject of the email
     * @param text      the text of the email
     */
    public void sendEmail(Email sender, Email recipient, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender.toString());
        message.setTo(recipient.toString());
        message.setSubject(Objects.requireNonNull(subject));
        message.setText(Objects.requireNonNull(text));
        emailSender.send(message);
    }
}
