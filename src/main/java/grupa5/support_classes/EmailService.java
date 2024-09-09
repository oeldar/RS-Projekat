package grupa5.support_classes;

import java.time.LocalDateTime;
import java.util.List;
import grupa5.baza_podataka.Dogadjaj;
import grupa5.baza_podataka.DogadjajPrijedlog;
import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Lokacija;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class EmailService {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy 'u' HH:mm'h'");

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
                      "' koji je trebalo da se održi " + dogadjaj.getPocetakDogadjaja().format(formatter) +
                      " otkazan. Ukoliko ste kupili kartu ili napravili rezervaciju, izvršiće se automatska refundacija.\n\nS poštovanjem,\nVaš tim";
        
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }

    public void obavjestiOrganizatoraZaOdbijanjeDogadjaja(Dogadjaj dogadjaj, String email, String razlogOdbijanja) {
        String subject = "Odbijen događaj: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nŽao nam je što Vas obavještavamo da je Vaš prijedlog za događaj '" + dogadjaj.getNaziv() +
                      "' koji je trebalo da se održi " + dogadjaj.getPocetakDogadjaja().format(formatter) + " odbijen.\n\n" +
                      "Razlog odbijanja: " + razlogOdbijanja + "\n\n" +
                      "Ukoliko imate bilo kakva pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim";
    
        sendEmail(email, subject, body);
    }    

    public void obavjestiOrganizatoraZaOdobravanjeDogadjaja(Dogadjaj dogadjaj, String email) {
        String subject = "Odobren događaj: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nSa zadovoljstvom Vas obavještavamo da je Vaš prijedlog za događaj '" + dogadjaj.getNaziv() +
                "' koji će se održati " + dogadjaj.getPocetakDogadjaja().format(formatter) + " odobren. Možete početi sa pripremama i promocijom događaja.\n\nS poštovanjem,\nVaš tim";

        sendEmail(email, subject, body);
    }

    public void obavjestiKorisnikaZaOdbijanjeNjihoveRegistracije(Korisnik korisnik, String razlogOdbijanja) {
        String subject = "Odbijena registracija na Ticketio";
        String body = "Poštovani/a " + korisnik.getIme() + ", \n\nŽao nam je što Vas obavještavamo da je Vaša registracija na Ticketio odbijena. ";
        if (razlogOdbijanja != null) {
            body += "\n\nRazlog odbijanja: " + razlogOdbijanja + "\n\n";
        }
        body +="Ukoliko imate bilo kakva pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nTicketio tim";

        sendEmail(korisnik.getEmail(), subject, body);
    }

    public void obavjestiKorisnikaZaOdobravanjeNjihoveRegistracije(Korisnik korisnik) {
        String subject = "Odobrena registracija na Ticketio";
        String body = "Poštovani/a " + korisnik.getIme() + ", \n\nSa zadovoljstvom Vas obavještavamo da je Vaša registracija na Ticketio odobrena. " +
                      "Sada možete pristupiti svom nalogu i koristiti sve pogodnosti koje naš sistem nudi.\n\nS poštovanjem,\nTicketio tim";

        sendEmail(korisnik.getEmail(), subject, body);
    }

    public void obavjestiKorisnikaZaOtkazivanjeRezervacije(String email, String nazivDogadjaja, LocalDateTime datumRezervacije) {
        String subject = "Obavještenje o otkazivanju rezervacije";
        String body = String.format("Poštovani, \n\nObavještavamo Vas da je Vaša rezervacija za događaj '%s' otkazana jer je prošao poslednji datum za rezervaciju. " +
                                    "Rezervacija je prvobitno kreirana %s.\n\nUkoliko imate bilo kakvih pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim",
                                    nazivDogadjaja, datumRezervacije.format(formatter));

        sendEmail(email, subject, body);
    }

    public void obavjestiKorisnikaPrijeIstekaRezervacije(String email, String nazivDogadjaja, LocalDateTime datumRezervacije, LocalDateTime datumIsteka) {
        // Calculate the time left before expiration
        Duration timeLeft = Duration.between(LocalDateTime.now(), datumIsteka);
        long hoursLeft = timeLeft.toHours();
    
        // Customize the subject and body of the email based on the time left
        String subject = "Podsjetnik: Rezervacija za događaj '" + nazivDogadjaja + "' ističe uskoro";
        String body = String.format("Poštovani, \n\nVaša rezervacija za događaj '%s' ističe za %d sati. " +
                                    "Molimo Vas da kupite kartu kako biste osigurali svoje mjesto. " +
                                    "Rezervacija je prvobitno kreirana %s.\n\nUkoliko imate bilo kakvih pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim",
                                    nazivDogadjaja, hoursLeft, datumRezervacije.format(formatter));
    
        sendEmail(email, subject, body);
    }

    public void obavjestiOrganizatoraZaOdbijanjePromjenaNaDogadjaju(DogadjajPrijedlog prijedlog, String email, String razlogOdbijanja) {
        String subject = "Odbijene promjene na događaju: " + prijedlog.getOriginalniDogadjaj().getNaziv();
        String body = "Poštovani, \n\nŽao nam je što Vas obavještavamo da su promjene na događaju '" + prijedlog.getOriginalniDogadjaj().getNaziv() +
                    "' koje ste predložili odbijene.\n\n" +
                    "Razlog odbijanja: " + razlogOdbijanja + "\n\n" +
                    "Ukoliko imate bilo kakva pitanja, slobodno nas kontaktirajte.\n\nS poštovanjem,\nVaš tim";

        sendEmail(email, subject, body);
    }

    public void obavjestiKorisnikeZaPromjenuDatumaIMjesta(Dogadjaj originalniDogadjaj, DogadjajPrijedlog prijedlog, List<String> userEmails) {
        String subject = "Promjene u događaju: " + originalniDogadjaj.getNaziv();
        String body = "Poštovani, \n\nObavještavamo Vas da su se promijenili datum/vrijeme i lokacija održavanja događaja '" + originalniDogadjaj.getNaziv() +
                      "'. Novi datum i vrijeme su: " + prijedlog.getPocetakDogadjaja().format(formatter) + ", a nova lokacija je: " + prijedlog.getLokacija().getNaziv() +
                      ".\n\nMožete izvršiti zamjenu karte ili dobiti povrat novca.\n\nS poštovanjem,\nVaš tim";
        
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }
    
    public void obavjestiKorisnikeZaPromjenuDatuma(Dogadjaj originalniDogadjaj, LocalDateTime noviDatum, List<String> userEmails) {
        String subject = "Promjena datuma/vremena događaja: " + originalniDogadjaj.getNaziv();
        String body = "Poštovani, \n\nObavještavamo Vas da je došlo do promjene datuma/vremena održavanja događaja '" + originalniDogadjaj.getNaziv() +
                      "'. Novi datum i vrijeme su: " + noviDatum.format(formatter) + ".\n\nMožete dobiti povrat novca ako otkažete Vašu kupovinu ili rezervaciju\n\nS poštovanjem,\nVaš tim";
        
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }
    
    public void obavjestiKorisnikeZaPromjenuLokacije(Dogadjaj originalniDogadjaj, Lokacija novaLokacija, List<String> userEmails) {
        String subject = "Promjena lokacije održavanja događaja: " + originalniDogadjaj.getNaziv();
        String body = String.format(
            "Poštovani,\n\n" +
            "Obavještavamo Vas da je došlo do promjene lokacije održavanja događaja '%s'.\n\n" +
            "Nova lokacija je:\n" +
            "%s, %s\n" +
            "Adresa: %s\n\n" +
            "Možete izvršiti zamjenu karte ili dobiti povrat novca.\n\n" +
            "S poštovanjem,\n" +
            "Vaš tim",
            originalniDogadjaj.getNaziv(),
            novaLokacija.getNaziv(),
            novaLokacija.getMjesto().getNaziv(),
            novaLokacija.getAdresa()
        );
    
        for (String email : userEmails) {
            sendEmail(email, subject, body);
        }
    }
    
    
    public void obavjestiOrganizatoraZaOdobravanjePromjena(Dogadjaj dogadjaj, String email) {
        String subject = "Odobrene promjene događaja: " + dogadjaj.getNaziv();
        String body = "Poštovani, \n\nObavještavamo Vas da su Vaše promjene za događaj '" + dogadjaj.getNaziv() +
                      "' odobrene. Događaj je sada ažuriran u skladu sa Vašim prijedlogom.\n\nS poštovanjem,\nVaš tim";
        
        sendEmail(email, subject, body);
    }
    

    
}
