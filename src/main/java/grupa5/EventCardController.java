package grupa5;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import grupa5.DogadjajMoj;

public class EventCardController {

    @FXML
    private Text nazivText;

    @FXML
    private Text datumText;

    private MainScreenController mainScreenController;

    public void setMainScreenController(MainScreenController mainScreenController) {
        this.mainScreenController = mainScreenController;
    }

    public void setDogadjajMoj(DogadjajMoj dogadjajMoj) {
        nazivText.setText(dogadjajMoj.getNaziv());
        datumText.setText(dogadjajMoj.getDatum().toString());
    }

    public void eventClicked(MouseEvent event) {
        // Ovdje ide kod koji želite da se izvrši
        System.out.println("AnchorPane clicked!");
        if (mainScreenController != null) {
            mainScreenController.loadDogadjaj1View(); // ili drugi view koji želite
        }
    }
    
}