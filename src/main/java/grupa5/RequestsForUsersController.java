package grupa5;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.transform.Scale;

public class RequestsForUsersController implements Initializable {

    private static final String USERS_REQUEST_CARD = "views/user-request-card.fxml";
    //private List<Request> requests;

    @FXML
    private FlowPane requestsFlowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        showRequests();
    }

    private void showRequests() {
        for (int i = 0; i < 50; ++i) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(USERS_REQUEST_CARD));
            try {
                Parent requestView = loader.load();
                scaleView(requestView);
                requestsFlowPane.getChildren().add(requestView);
            } catch (IOException e) {
                e.printStackTrace();
            }
       }
    }

    private void scaleView(Parent view) {
        Scale scale = new Scale();
        scale.setX(0.9);
        scale.setY(0.9);
        view.getTransforms().add(scale);
    }

    
}
    