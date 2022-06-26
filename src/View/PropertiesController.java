package View;

import Server.Configurations;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

public class PropertiesController implements Initializable {
    public RadioButton BestFirstSearch;
    public RadioButton BreadthFirstSearch;
    public RadioButton DepthFirstSearch;
    public RadioButton MyMazeGenerator;
    public RadioButton SimpleMazeGenerator;
    public RadioButton EmptyMazeGenerator;
    public Button ApplyBTN;
    public Label MazePropLBL;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ToggleGroup Algo = new ToggleGroup();
        ToggleGroup Gen = new ToggleGroup();
        BestFirstSearch.setToggleGroup(Algo);
        BreadthFirstSearch.setToggleGroup(Algo);
        DepthFirstSearch.setToggleGroup(Algo);

        MyMazeGenerator.setToggleGroup(Gen);
        SimpleMazeGenerator.setToggleGroup(Gen);
        EmptyMazeGenerator.setToggleGroup(Gen);
//        Stage myStage = (Stage) BestFirstSearch.getScene().getWindow();
//        myStage.setOnHiding( event -> {System.out.println("Closing Stage");} );

        try {
            InputStream input = new FileInputStream("resources/config.properties");
            Properties prop = new Properties();
            prop.load(input);
            String mazeGen = prop.getProperty("mazeGeneratingAlgorithm");
            String mazeSearch = prop.getProperty("mazeSearchingAlgorithm");
            switch (mazeGen){
                case "MyMazeGenerator":
                    MyMazeGenerator.setSelected(true);
                    break;
                case "SimpleMazeGenerator":
                    SimpleMazeGenerator.setSelected(true);
                    break;
                case "EmptyMazeGenerator":
                    EmptyMazeGenerator.setSelected(true);
                    break;
            }
            switch (mazeSearch){
                case "BestFirstSearch":
                    BestFirstSearch.setSelected(true);
                    break;
                case "BreadthFirstSearch":
                    BreadthFirstSearch.setSelected(true);
                    break;
                case "DepthFirstSearch":
                    DepthFirstSearch.setSelected(true);
                    break;
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void ApplyFunc(ActionEvent actionEvent) {
        try(OutputStream out = new FileOutputStream("resources/config.properties")) {
            Properties prop = new Properties();

            if(MyMazeGenerator.isSelected()){
                prop.setProperty("mazeGeneratingAlgorithm","MyMazeGenerator");
            }
            if(SimpleMazeGenerator.isSelected()){
                prop.setProperty("mazeGeneratingAlgorithm","SimpleMazeGenerator");
            }
            if(EmptyMazeGenerator.isSelected()){
                prop.setProperty("mazeGeneratingAlgorithm","EmptyMazeGenerator");
            }
            if(BreadthFirstSearch.isSelected()){
                prop.setProperty("mazeSearchingAlgorithm","BreadthFirstSearch");
            }
            if(BestFirstSearch.isSelected()){
                prop.setProperty("mazeSearchingAlgorithm","BestFirstSearch");
            }
            if(DepthFirstSearch.isSelected()){
                prop.setProperty("mazeSearchingAlgorithm","DepthFirstSearch");
            }
            prop.setProperty("threadPoolSize","5");
            prop.store(out,null);

            ((Stage)BestFirstSearch.getScene().getWindow()).close();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
