package View;

import javafx.event.ActionEvent;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class WinController implements Initializable {
    public MediaPlayer mediaPlayer;
    public Button StartBTN;
    public ImageView WinImg;
    public Label ScoreNum;
    public Label WinLBL;
    public Label ScoreIsLBL;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        try {
            mediaPlayer = new MediaPlayer(new Media(WinController.class.getResource("/images/LivDeDream.mp3").toURI().toString()));
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

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

    public void setScore(double score) {
        if (score >= 1)
            score = 1;
        DecimalFormat df = new DecimalFormat("0.00");
        ScoreNum.setText(df.format(score * 100) + " %");
    }


}
