package grupa5;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import grupa5.DogadjajMoj;

public class EventCardController {

    @FXML
    private Text nazivText;

    @FXML
    private Text datumText;

    public void setDogadjajMoj(DogadjajMoj dogadjajMoj) {
        nazivText.setText(dogadjajMoj.getNaziv());
        datumText.setText(dogadjajMoj.getDatum().toString());
    }
}