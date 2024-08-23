package grupa5;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

public class MojiDogadjajiController {

    @FXML
    private FlowPane dogadjaji;

    @FXML
    private void initialize() {
        for (int i = 0; i < 10; i++) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("views/mojDogadjajCard.fxml"));
                AnchorPane eventCard;
                try {
                    eventCard = loader.load();
                // controller.setDogadjaj(dogadjaj);
                // controller.setMainScreenController(this);
    
                dogadjaji.getChildren().add(eventCard);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        
                
    }
}
