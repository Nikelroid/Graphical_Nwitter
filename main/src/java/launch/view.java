package launch;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import login.loginView;


import java.io.IOException;
import java.net.URISyntaxException;


public class view extends Application {
    public static Stage stage;
    public static Parent root;
    public static Scene scene;

    public view() {
    }

    public void start(Stage primaryStage) throws Exception {
        stage=primaryStage;
        root = FXMLLoader.load(getClass().getResource("/layout/page/login_page.fxml"));
        scene = new Scene(root);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/img/LOGO_main.png")));
        stage.show();

        new loginView(true);
    }

    public view(String[] args) throws IOException, URISyntaxException {
        launch(args);
    }

}
