package View;

import Model.MyModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
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

public class MyViewController implements Initializable, Observer {
    public TextField textField_mazeRows;
    public TextField textField_mazeColumns;
    public MazeDisplayer mazeDisplayer;
    public Label playerRow;
    public Label playerCol;
    public MenuItem Save;
    public MyViewModel myViewModel;
    public MenuItem Delete;
    public Button SolveBTN;
    public Button GenerateBTN;
    public Pane myPane;
    public BorderPane myBorederPane;
    public Stage CerStage;
    public MenuItem Properties;
    public Stage PropStage;
    public Button MuteBTN;
    public MediaPlayer mediaPlayer;
    public Slider slider;
    public GridPane myGridPane;
    public Label PointsTaken;
    public boolean Solved = false;
    StringProperty updatePlayerRow = new SimpleStringProperty();
    StringProperty updatePlayerCol = new SimpleStringProperty();
    StringProperty updatePlayerScore = new SimpleStringProperty();

    public void setCerStage(Stage cerStage) {
        try {
            CerStage = cerStage;
            mazeDisplayer.heightProperty().bind(myPane.heightProperty());
            mazeDisplayer.widthProperty().bind(myPane.widthProperty());
            CerStage.widthProperty().addListener((obs, oldVal, newVal) -> {
                mazeDisplayer.drawMaze(myViewModel.getMaze(), myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol(), myViewModel.getSolution());
                setPlayerPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol());
            });
            CerStage.heightProperty().addListener((obs, oldVal, newVal) -> {
                mazeDisplayer.drawMaze(myViewModel.getMaze(), myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol(), myViewModel.getSolution());
                setPlayerPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol());
                // Do whatever you want
            });

            cerStage.setOnHiding(event -> {
                if (PropStage.isShowing()) {
                    PropStage.close();

                }
                myViewModel.StopServers();
            });
            addMouseScrolling(mazeDisplayer);
            File file = new File("./resources/Cuteboy.mp3");
            Media media = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            slider.valueProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                    mediaPlayer.setVolume(slider.getValue() * 0.01);

                }
            });


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }

    private void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {
            // Adjust the zoom factor as per your requirement

            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0) {
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX(node.getScaleX() * zoomFactor);
            node.setScaleY(node.getScaleY() * zoomFactor);
        });
    }


    public MyViewController() {

        myViewModel = new MyViewModel();
        myViewModel.addObserver(this);
        try {
            myViewModel.StartServers();
        } catch (Exception e) {
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Prop.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 400);
            String css = this.getClass().getResource("MainStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            PropStage = new Stage();
            PropStage.setScene(scene);
            PropStage.setTitle("Properties Editor");
            PropStage.setResizable(false);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }


    public String getUpdatePlayerRow() {
        return updatePlayerRow.get();
    }

    public void setUpdatePlayerRow(int updatePlayerRow) {
        this.updatePlayerRow.set(updatePlayerRow + "");
    }

    public String getUpdatePlayerCol() {
        return updatePlayerCol.get();
    }

    public void setUpdatePlayerCol(int updatePlayerCol) {
        this.updatePlayerCol.set(updatePlayerCol + "");
    }

    public void setUpdatePlayerScore() {
        this.updatePlayerScore.set(mazeDisplayer.getScore() + "");
    }

    public String getUpdatePlayerScore() {
        return updatePlayerScore.get();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerRow.textProperty().bind(updatePlayerRow);
        playerCol.textProperty().bind(updatePlayerCol);
        PointsTaken.textProperty().bind(updatePlayerScore);


    }


    public void generateMaze(ActionEvent actionEvent) {
        try {

            int rows = Integer.parseInt(textField_mazeRows.getText());
            int cols = Integer.parseInt(textField_mazeColumns.getText());
            mazeDisplayer.initPoints(rows, cols);
            if (rows < 2 || cols < 2)
                throw new Exception("please insert a valid number");
            Save.setVisible(true);
            Delete.setVisible(true);
            SolveBTN.setText("Solve Maze");
            myViewModel.generateMaze(rows, cols);


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }


    }

    public void solveMaze(ActionEvent actionEvent) {
        if (myViewModel.getMaze() == null)
            return;
        if (SolveBTN.getText().equals("Solve Maze")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Maze Solved!!!");
            alert.show();

            Solved = true;
            mazeDisplayer.NoPoints();
            myViewModel.solveMaze();
            SolveBTN.setText("Clear Solution");
        } else {
            SolveBTN.setText("Solve Maze");
            myViewModel.ClearSolution();
        }


    }

    public void openFile(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Open maze");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze"));
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showOpenDialog(null);
        myViewModel.load(chosen.getPath());
        //...
    }

    public void SaveMaze(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Maze");
        fc.setInitialDirectory(new File("./resources"));
        File chosen = fc.showSaveDialog(null);
        String path = chosen.getPath();
        myViewModel.save(path);

    }

    public void keyPressed(KeyEvent keyEvent) {
        mazeDisplayer.setImageFileNamePlayer(keyEvent);
        myViewModel.moveCharacter(keyEvent);
        keyEvent.consume();
    }

    public void setPlayerPosition(int row, int col) {
        setUpdatePlayerRow(row);
        setUpdatePlayerCol(col);
        setUpdatePlayerScore();

    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@code Observable} object's
     * {@code notifyObservers} method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the {@code notifyObservers}
     */
    @Override
    public void update(Observable o, Object arg) {
        String msg = null;
        try {
            msg = (String) arg;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        switch (msg) {
            case "Maze generated":
                mazeDisplayer.initPoints(myViewModel.getMaze().getRow(),myViewModel.getMaze().getCol());
                mazeDisplayer.drawMaze(myViewModel.getMaze(), myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol(), myViewModel.getSolution());
                setPlayerPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol());
                Solved=false;
                break;
            case "Win":
                WinFunc();
            case "Maze Deleted":
            case "Maze solved":
            case "Solution Deleted":
            case "Character moved":
                mazeDisplayer.drawMaze(myViewModel.getMaze(), myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol(), myViewModel.getSolution());
                setPlayerPosition(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionCol());
                break;


        }


    }

    private void WinFunc() {
        Parent root = null;
        try {
            mediaPlayer.stop();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Win.fxml"));
            root = fxmlLoader.load();
            Stage stage = (Stage) textField_mazeRows.getScene().getWindow();
            Scene scene = new Scene(root, 600, 600);
            String css = this.getClass().getResource("MainStyle.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.setTitle("Win");
            stage.setResizable(false);
            WinController winController = fxmlLoader.getController();
            myViewModel.solveMaze();

            double sulsize = myViewModel.getSolution().getSulSize();
            double score = Double.parseDouble(mazeDisplayer.getScore());
            if (Solved)
                sulsize = 0;
            winController.setScore(sulsize / score);
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void DeleteMaze(ActionEvent actionEvent) {
        myViewModel.Clear();
        SolveBTN.setText("Solve Maze");
        Save.setVisible(false);
        Delete.setVisible(false);
    }

    public void OpenProp(ActionEvent actionEvent) {
        if (PropStage.isShowing())
            return;
        try {
            PropStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void MediaMute(ActionEvent actionEvent) {
        if (MuteBTN.getText().equals("mute"))
            MuteBTN.setText("unmute");
        else
            MuteBTN.setText("mute");
        mediaPlayer.setMute(!mediaPlayer.isMute());
    }


}
