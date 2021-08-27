package graphics;

import imageControl.getImageFile;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPages.Info;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


public class selectIcon {
    Image image1;

    public selectIcon(String username,int i) throws IOException {

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Choose image");


        final FileChooser fileChooser = new FileChooser();
        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");
        openButton.setOnAction((final ActionEvent e) -> {
            File file = fileChooser.showOpenDialog(cStage);


            if (file != null) {
                try {
                    Path simple = Paths.get("simple.png");
                    Path path = Paths.get(simple.toAbsolutePath().getParent()+
                            "\\main\\src\\resources\\profiles\\"+username+".png");
                    Files.copy(file.toPath(),path, StandardCopyOption.REPLACE_EXISTING);
                    File file1 = new File(String.valueOf(path));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                Image image1 = new Image(file.toURI().toString());
                Info.Icon.setImage(image1);
                pic.setImage(image1);
                pic.setVisible(true);
            }
        });

        try {
            try {
                getImageFile getProfileFile = new getImageFile();
                File file = getProfileFile.profile(username);
                Image image = new Image(file.toURI().toString());
                FileUtils.readFileToByteArray(file);
                pic.setImage(image);
            } catch (NullPointerException| FileNotFoundException ignored) {
            } catch (IOException e) {
                e.printStackTrace();
            }

        }catch (NullPointerException ignored){
            pic.setVisible(false);
        }
        if (i==2){
            openButton.setVisible(false);
        }

        Button Back = (Button) cScene.lookup("#back");
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        cStage.show();
    }

    public selectIcon(File file) throws IOException {

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Show image");

        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");

            if (file != null) {
                Image image1 = new Image(file.toURI().toString());
                pic.setImage(image1);
                pic.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(null,"An error ourcud, try again");
            }


        openButton.setVisible(false);



        Button Back = (Button) cScene.lookup("#back");
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        cStage.show();
    }

    public static Image ImageIm;
    public static File fileIm;
    public void SelectImage() throws IOException {

        Parent cRoot = FXMLLoader.load(getClass().getResource("/layout/mini_page/select_image.fxml"));
        Scene cScene = new Scene(cRoot);
        Stage cStage = new Stage();
        cStage.setScene(cScene);

        cStage.setTitle("Slecting image");


        final FileChooser fileChooser = new FileChooser();
        ImageView pic = (ImageView) cScene.lookup("#pic");
        final Button openButton = (Button) cScene.lookup("#select");
        openButton.setOnAction((final ActionEvent e) -> {
            selectIcon.fileIm = fileChooser.showOpenDialog(cStage);
            selectIcon.ImageIm = new Image(selectIcon.fileIm.toURI().toString());
            try {
                pic.setImage(ImageIm);
            }catch (NullPointerException ignored){
                pic.setVisible(false);
            }
        });

        try {
            pic.setImage(ImageIm);
        }catch (NullPointerException ignored){
            pic.setVisible(false);
        }

        Button Back = (Button) cScene.lookup("#back");
        Back.setText("Confirm");
        Back.getStyleClass().remove("login");
        Back.getStyleClass().add("toggle_accept");
        Back.setOnAction(event -> {
            Back.getScene().getWindow().hide();
        });
        cStage.show();

    }

    public selectIcon() {

    }

}
