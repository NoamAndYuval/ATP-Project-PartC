package ViewModel;

import Model.IModel;
import Model.MyModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private MyModel model;

    public MyViewModel() {
        this.model = new MyModel();
        model.addObserver(this);

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
        setChanged();
        notifyObservers(arg);
    }

    public void generateMaze(int row, int col) {
        model.generateMaze(row, col);
    }

    public void moveCharacter(KeyEvent movement) {
        model.moveCharacter(movement);
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionCol() {
        return model.getCharacterPositionCol();
    }

    public void solveMaze() {
        model.solveMaze();
    }

    public Solution getSolution() {
        return model.getSolution();
    }

    public void save(String filename) {
        model.save(filename);
    }

    public void load(String path) {
        model.load(path);
    }
    public void StartServers(){
        model.startServers();
    }
    public void StopServers(){
        model.stopServers();
    }
    public void Clear(){
        model.Clear();
    }
    public void ClearSolution(){
        model.ClearSolution();
    }




}
