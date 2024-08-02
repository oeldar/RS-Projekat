package grupa5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main-view"));
        stage.setTitle("Ticketio App");
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fileName) throws IOException {
        scene.setRoot(loadFXML(fileName));
    }

    static Parent loadFXML(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fileName + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}