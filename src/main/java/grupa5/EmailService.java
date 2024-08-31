package grupa5;

import java.util.List;
import grupa5.baza_podataka.Dogadjaj;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    // Konfiguracija za email server
    private final String host = "smtp.gmail.com"; // SMTP server
    private final String from = "ticketio798@gmail.com"; // Vaša email adresa
    private final String fromName = "Ticketio";
    private final String username = "ticketio798@gmail.com"; // Vaš korisnički naziv za SMTP server
    private final String password = "suza vuqo qpaz yvtn"; // Vaša lozinka za SMTP server

    public void sendEmail(String to, String subject, String body) {
        // Postavljanje svojstava za SMTP server
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Kreiranje sesije sa autentifikacijom
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Kreiranje poruke
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(from, fromName));
            } catch(Exception e) {
                e.printStackTrace();
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Slanje poruke
            Transport.send(message);

            System.out.println("Email poslat na: " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Greška pri slanju emaila: " + e.getMessage());
        }
    }

    public void obavjestiKorisnikeZaOtkazivanjeDogadjaja(Dogadjaj dogadjaj, List<String> userEmails) {
        String subject = "Otkazan događaj: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nObaveštavamo vas da je događaj '" + dogadjaj.getNaziv() +
                      "' koji je trebalo da se održi " + dogadjaj.getPocetakDogadjaja() +
                      " otkazan. Ukoliko ste kupili kartu ili napravili rezervaciju, izvršiće se automatska refundacija.\n\nS poštovanjem,\nVaš tim";
        
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }
}
