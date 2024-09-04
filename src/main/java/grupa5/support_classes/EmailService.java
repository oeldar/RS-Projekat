package grupa5.support_classes;

import java.time.LocalDateTime;
import java.util.List;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.Korisnik;
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
        String body = "Poštovani, \n\nObavještavamo Vas da je događaj '" + dogadjaj.getNaziv() +
                      "' koji je trebalo da se održi " + dogadjaj.getPocetakDogadjaja() +
                      " otkazan. Ukoliko ste kupili kartu ili napravili rezervaciju, izvršiće se automatska refundacija.\n\nS poštovanjem,\nVaš tim";
        
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }

    public void obavjestiOrganizatoraZaOdbijanjeDogadjaja(Dogadjaj dogadjaj, String email) {
        String subject = "Odbijen događaj: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nŽao nam je što Vas obavještavamo da je Vaš prijedlog za događaj '" + dogadjaj.getNaziv() +
                      "' koji je trebalo da se održi " + dogadjaj.getPocetakDogadjaja() + " odbijen.\n\n" +
                      "Razlog odbijanja: " + dogadjaj.getRazlogOdbijanja() + "\n\n" +
                      "Ukoliko imate bilo kakva pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim";
    
        sendEmail(email, subject, body);
    }    

    public void obavjestiOrganizatoraZaOdobravanjeDogadjaja(Dogadjaj dogadjaj, String email) {
        String subject = "Odobren događaj: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nSa zadovoljstvom Vas obavještavamo da je Vaš prijedlog za događaj '" + dogadjaj.getNaziv() +
                "' koji će se održati " + dogadjaj.getPocetakDogadjaja() + " odobren. Možete početi sa pripremama i promocijom događaja.\n\nS poštovanjem,\nVaš tim";

        sendEmail(email, subject, body);
    }

    public void obavjestiKorisnikaZaOdbijanjeNjihoveRegistracije(Korisnik korisnik) {
        String subject = "Odbijena registracija na Ticketio";
        String body = "Poštovani/a " + korisnik.getIme() + ", \n\nŽao nam je što Vas obavještavamo da je Vaša registracija na Ticketio odbijena. " +
                      "Ukoliko imate bilo kakva pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nTicketio tim";

        sendEmail(korisnik.getEmail(), subject, body);
    }

    public void obavjestiKorisnikaZaOdobravanjeNjihoveRegistracije(Korisnik korisnik) {
        String subject = "Odobrena registracija na Ticketio";
        String body = "Poštovani/a " + korisnik.getIme() + ", \n\nSa zadovoljstvom Vas obavještavamo da je Vaša registracija na Ticketio odobrena. " +
                      "Sada možete pristupiti svom nalogu i koristiti sve pogodnosti koje naš sistem nudi.\n\nS poštovanjem,\nTicketio tim";

        sendEmail(korisnik.getEmail(), subject, body);
    }

    public void obavjestiKorisnikaZaOtkazivanjeRezervacije(String email, String nazivDogadjaja, LocalDateTime datumRezervacije) {
        String subject = "Obaveštenje o otkazivanju rezervacije";
        String body = String.format("Poštovani, \n\nObavještavamo Vas da je Vaša rezervacija za događaj '%s' otkazana jer je prošao poslednji datum za rezervaciju. " +
                                    "Rezervacija je prvobitno kreirana %s.\n\nUkoliko imate bilo kakvih pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim",
                                    nazivDogadjaja, datumRezervacije);

        sendEmail(email, subject, body);
    }
}
