package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Launcher.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene=new Scene(root, 600, 365);
        String css = this.getClass().getResource("MainStyle.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setTitle("Launcher");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        LauncherController launcherController = fxmlLoader.getController();
        launcherController.setCerStage(primaryStage);
        primaryStage.show();



    }


    public static void main(String[] args) {
        launch(args);
    }
}
