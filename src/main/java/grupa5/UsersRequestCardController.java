package grupa5;

import grupa5.baza_podataka.Korisnik;
import grupa5.baza_podataka.Korisnik.TipKorisnika;
import grupa5.baza_podataka.services.KorisnikService;
import grupa5.baza_podataka.services.NovcanikService;
import grupa5.baza_podataka.services.StatistikaKupovineService;
import grupa5.support_classes.EmailService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;

// @SuppressWarnings({"exports", "unused"})
public class UsersRequestCardController {

    @FXML
    private Text mailLabel;

    @FXML
    private Text nameLabel;

    @FXML
    private Button odbijButton;

    @FXML
    private Button odobriButton;

    @FXML
    private Text roleLabel;

    @FXML
    private ImageView userImage;

    @FXML
    private Text usernameLabel;

    private Korisnik korisnik;
    private KorisnikService korisnikService;
    private NovcanikService novcanikService;
    private StatistikaKupovineService statistikaKupovineService;
    private RequestsForUsersController requestsForUsersController;
    private MainScreenController mainScreenController;

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
        updateUI();
    }

    public void setRequestsForUsersController(RequestsForUsersController requestsForUsersController) {
        this.requestsForUsersController = requestsForUsersController;
    }
    
    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }
    
    public void setKorisnikService(KorisnikService korisnikService) {
        this.korisnikService = korisnikService;
    }

    public void setNovcanikService(NovcanikService novcanikService) {
        this.novcanikService = novcanikService;
    }

    public void setStatistikaKupovineService(StatistikaKupovineService statistikaKupovineService) {
        this.statistikaKupovineService = statistikaKupovineService;
    }

    private void updateUI() {
        mailLabel.setText(korisnik.getEmail());
        nameLabel.setText(korisnik.getIme() + " " + korisnik.getPrezime());
        roleLabel.setText(korisnik.getTipKorisnika().toString());
        usernameLabel.setText(korisnik.getKorisnickoIme());
        // Load user image if available
        loadUserImage(korisnik.getPutanjaDoSlike());
    }

    private void loadUserImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            userImage.setImage(image);
        } else {
            // Set default image
            userImage.setImage(new Image(getClass().getResourceAsStream("assets/users_photos/" + korisnik.getTipKorisnika().toString() + ".png")));
        }
    }

    @FXML
    void odbijKorisnika(ActionEvent event) {
        if (korisnik != null) {
            EmailService emailService = new EmailService();
            emailService.obavjestiKorisnikaZaOdbijanjeNjihoveRegistracije(korisnik);
            korisnikService.obrisiKorisnika(korisnik.getKorisnickoIme());
            if (requestsForUsersController != null) {
                requestsForUsersController.refreshRequests();
            }
        }
    }

    @FXML
    void odobriKorisnika(ActionEvent event) {
        if (korisnik != null) {
            korisnikService.verifikujKorisnika(korisnik.getKorisnickoIme());
            if (requestsForUsersController != null) {
                requestsForUsersController.refreshRequests();
            }
            if (korisnik.getTipKorisnika().equals(TipKorisnika.KORISNIK)) {
                statistikaKupovineService.kreirajStatistikuKupovine(korisnik.getKorisnickoIme(), 0, 0.0);
                novcanikService.kreirajNovcanik(korisnik.getKorisnickoIme());
            }
            EmailService emailService = new EmailService();
            emailService.obavjestiKorisnikaZaOdobravanjeNjihoveRegistracije(korisnik);
        }
    }
}
