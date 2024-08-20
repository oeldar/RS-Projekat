package grupa5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static Scene scene;
    private static FXMLLoader fxmlLoader;

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

    public static boolean isFXMLLoaded(String resource) {
        try {
            Parent root = fxmlLoader.load(App.class.getResource(resource).openStream());
            return root != null;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        launch();
    }

}