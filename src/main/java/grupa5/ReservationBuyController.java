package grupa5;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ReservationBuyController {

    @FXML
    private AnchorPane mainAnchorPane, brojKartiPane, cijenaPane; // Ovo je glavni AnchorPane iz FXML-a

    @FXML
    private TextField brojKarti; // TextField za unos broja karata

    @FXML
    public void initialize() {
        brojKarti.setText("1");
        configureBrojKartiTextField();
    }

    @FXML
    public void moveToBtn1() {
        moveComponentsTo("btn1");
    }

    @FXML
    public void moveToBtn2() {
        moveComponentsTo("btn2");
    }

    @FXML
    public void moveToBtn3() {
        moveComponentsTo("btn3");
    }

    @FXML
    public void moveToBtn31() {
        moveComponentsTo("btn31");
    }

    @FXML
    public void moveToBtn311() {
        moveComponentsTo("btn311");
    }

    @FXML
    public void moveToBtn3111() {
        moveComponentsTo("btn3111");
    }

    private void moveComponentsTo(String buttonId) {
        Button selectedButton = (Button) mainAnchorPane.lookup("#" + buttonId);
        double newY = selectedButton.getLayoutY(); // Uzmi Y poziciju dugmeta

        // Animiraj inputSpinner i cijena
        animirajKomponentu(brojKartiPane, brojKartiPane.getLayoutX(), newY - 50);
        animirajKomponentu(cijenaPane, cijenaPane.getLayoutX(), newY - 50); // Možeš prilagoditi ovu vrijednost prema potrebi
    }

    private void animirajKomponentu(javafx.scene.Node node, double newX, double newY) {
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.millis(500));  // Trajanje animacije u milisekundama
        transition.setNode(node);  // Čvor koji želimo animirati
        transition.setToX(newX);  // Nova X koordinata (ako se mijenja)
        transition.setToY(newY);  // Nova Y koordinata
        transition.play();  // Pokreni animaciju
    }

    private void configureBrojKartiTextField() {
        brojKarti.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, event -> {
            String character = event.getCharacter();

            // Provjeri da li je uneseni karakter cifra
            if (!character.matches("\\d")) {
                event.consume(); // Blokiraj unos ako nije cifra
            } else {
                // Provjeri da li je uneseni broj unutar dozvoljenog raspona
                String currentText = brojKarti.getText() + character;
                int currentValue = Integer.parseInt(currentText);
                if (currentValue > 20 || currentValue < 1) {
                    event.consume(); // Blokiraj unos ako je broj izvan dozvoljenog raspona
                }
            }
        });
    }

    @FXML
    public void incBrojKarti() {
        int currentValue = Integer.parseInt(brojKarti.getText());
        if (currentValue < 20) {
            brojKarti.setText(String.valueOf(currentValue + 1));
        }
    }

    @FXML
    public void decBrojKarti() {
        int currentValue = Integer.parseInt(brojKarti.getText());
        if (currentValue > 1) {
            brojKarti.setText(String.valueOf(currentValue - 1));
        }
    }

}
