package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public interface IModel {
    void generateMaze(int row,int col);
    void moveCharacter(KeyEvent movement);
    Maze getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionCol();
    void solveMaze();
    Solution getSolution();
    void save(String filename);
    void load(String path);

}
