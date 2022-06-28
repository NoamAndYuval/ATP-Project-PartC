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
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class LauncherController implements Initializable {


    public Button StartBTN;
    public Button HelpBTN;
    public Stage HelpStage;
    public Stage CerStage;
    public Label WelcomeLBL;
    public MenuItem AboutUs;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setCerStage(Stage cerStage) {
        CerStage = cerStage;
        cerStage.setOnHiding(event -> {
            if (HelpStage.isShowing()) {
                HelpStage.close();
            }
        });

    }

    public LauncherController() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Help.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            String css = this.getClass().getResource("MainStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            HelpStage = new Stage();
            HelpStage.setScene(scene);
            HelpStage.setTitle("Help");
            HelpStage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void StartGame(ActionEvent actionEvent) {
        try {
            if(HelpStage.isShowing())
                HelpStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MazeWindow.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1000, 750);
            String css = this.getClass().getResource("MainStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            CerStage.setScene(scene);
            CerStage.setTitle("Maze game");
            MyViewController myViewController = fxmlLoader.getController();
            myViewController.setCerStage(CerStage);
            CerStage.setResizable(true);
            CerStage.show();
        } catch (Exception e) {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void HelpFunc(ActionEvent actionEvent) {
        if (HelpStage.isShowing())
            return;
        HelpStage.show();
    }

    public void AboutUsFunc(ActionEvent actionEvent) {
        String msg = "The game was developed by Noam Azulay and Yuval Gorodissky during a course in advanced topics in programming, graduate students in information systems engineering, second year.";
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.show();
    }
}
