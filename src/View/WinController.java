package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;

import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class WinController implements Initializable {
    public MediaPlayer mediaPlayer;
    public Button StartBTN;
    public ImageView WinImg;
    public Label ScoreNum;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File file = new File("./resources/LivDeDream.mp3");
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }


    public void StartOver(ActionEvent actionEvent) {
        try {
            mediaPlayer.stop();
            FXMLLoader fxmlLoader = new FXMLLoader();

            Parent root = fxmlLoader.load(getClass().getResource("MazeWindow.fxml").openStream());
            Stage stage = (Stage) StartBTN.getScene().getWindow();
            Scene scene = new Scene(root, 1000, 750);
            String css = this.getClass().getResource("MainStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Maze game");
            MyViewController myViewController = fxmlLoader.getController();
            myViewController.setCerStage(stage);
            stage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }
    public void setScore(double score){
        if(score>=1)
            score=1;
        DecimalFormat df = new DecimalFormat("0.00");
        ScoreNum.setText(df.format(score*100)+" %");
    }


}
