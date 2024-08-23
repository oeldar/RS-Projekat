package grupa5;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;

public class EventsRequestsController implements Initializable
{
    private static final String EVENT_REQUEST_CARD = "views/eventRequestCard.fxml";

    @FXML
    private FlowPane requestsFlowPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showRequests();
    }

    private void showRequests() {
        for (int i = 0; i < 8; ++i) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(EVENT_REQUEST_CARD));
            try {
                Parent requestView = loader.load();
                requestsFlowPane.getChildren().add(requestView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
