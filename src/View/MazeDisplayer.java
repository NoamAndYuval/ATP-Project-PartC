package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;

public class MazeDisplayer extends Canvas {
    // wall and player images:
    StringProperty imageFileNameWall = new SimpleStringProperty();
    StringProperty imageFileNamePlayer = new SimpleStringProperty();
    StringProperty imageFileNameTarget = new SimpleStringProperty();
    StringProperty imageFileNameSol = new SimpleStringProperty();


    Image wallImage = null;
    Image TargetImage = null;
    Image playerImage = null;
    Image PlayerR = null;
    Image PlayerL = null;
    Image PlayerU = null;
    Image PlayerD = null;
    Image solImg = null;
    Image PointsImg = null;
    boolean[][] points;
    private int Score;


    public String getImageFileNameWall() {
        return imageFileNameWall.get();
    }

    public String getImageFileNameTarget() {
        return imageFileNameTarget.get();
    }

    public String getImageFileNameSol() {
        return imageFileNameSol.get();
    }


    public void setImageFileNameTarget(String imageFileNameTarget) {
        this.imageFileNameTarget.set(imageFileNameTarget);
    }

    public void setImageFileNameSol(String imageFileNameSol) {
        this.imageFileNameSol.set(imageFileNameSol);
    }

    public String imageFileNameWallProperty() {
        return imageFileNameWall.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.imageFileNameWall.set(imageFileNameWall);
    }

    public String getImageFileNamePlayer() {
        return imageFileNamePlayer.get();
    }

    public void initPoints(int row, int col) {
        points = new boolean[row][col];
        this.Score = 0;

    }

    public String imageFileNamePlayerProperty() {
        return imageFileNamePlayer.get();
    }

    public void setImageFileNamePlayer(KeyEvent keyEvent) {


        try {


            switch (keyEvent.getCode()) {
                case UP:
                case NUMPAD8:
                    if (PlayerU == null)
                        PlayerU = new Image(String.valueOf(getClass().getResource("/images/pacmanUp.png")));
                    playerImage = PlayerU;
                    break;
                case DOWN:
                case NUMPAD2:
                    if (PlayerD == null)
                        PlayerD = new Image(String.valueOf(getClass().getResource("/images/pacmanDown.png")));
                    playerImage = PlayerD;
                    break;
                case LEFT:
                case NUMPAD4:
                case NUMPAD1:
                case NUMPAD7:
                    if (PlayerL == null)
                        PlayerL = new Image(String.valueOf(getClass().getResource("/images/pacmanLeft.png")));
                    playerImage = PlayerL;
                    break;
                case RIGHT:
                case NUMPAD6:
                case NUMPAD3:
                case NUMPAD9:
                    if (PlayerR == null)
                        PlayerR = new Image(String.valueOf(getClass().getResource("/images/pacmanRight.png")));
                    playerImage = PlayerR;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void drawMaze(Maze maze, int playerRow, int playerCol, Solution solution) {
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, canvasWidth, canvasHeight);
        if (maze != null) {

            int rows = maze.getRow();
            int cols = maze.getCol();

            double cellHeight = canvasHeight / rows;
            double cellWidth = canvasWidth / cols;

            //clear the canvas:
            drawPoints(graphicsContext, cellHeight, cellWidth, rows, cols);
            drawMazeWalls(maze, graphicsContext, cellHeight, cellWidth, rows, cols);
            drawMazeSolution(solution, graphicsContext, cellHeight, cellWidth);
            drawPlayer(playerRow, playerCol, graphicsContext, cellHeight, cellWidth);
        }
    }

    private void drawPoints(GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        try {
            if (PointsImg == null)
                PointsImg = new Image(String.valueOf(getClass().getResource("/images/Point.png")));

        } catch (Exception e) {
            System.out.println("There is no player image file");
        }
        graphicsContext.setFill(Color.RED);


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                if (!points[i][j]) {
                    //if it is a wall:
                    if (PointsImg == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(PointsImg, x, y, cellWidth, cellHeight);

                }

            }
        }
    }


    private void drawMazeWalls(Maze maze, GraphicsContext graphicsContext, double cellHeight, double cellWidth, int rows, int cols) {
        graphicsContext.setFill(Color.RED);


        try {
            if (wallImage == null)
                wallImage = new Image(String.valueOf(getClass().getResource("/images/wall.png")));
        } catch (Exception e) {
            System.out.println("There is no wall image file");
        }

        try {
            if (TargetImage == null)
                TargetImage = new Image(String.valueOf(getClass().getResource("/images/Target.png")));

        } catch (Exception e) {
            System.out.println("There is no Target image file");
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double x = j * cellWidth;
                double y = i * cellHeight;
                if (maze.getPosition(i, j).getVal() == 1) {
                    //if it is a wall:
                    if (wallImage == null)
                        graphicsContext.fillRect(x, y, cellWidth, cellHeight);
                    else
                        graphicsContext.drawImage(wallImage, x, y, cellWidth, cellHeight);

                }
                if (i == maze.getGoalPosition().getRowIndex() && j == maze.getGoalPosition().getColumnIndex()) {

                    graphicsContext.drawImage(TargetImage, x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    private void drawPlayer(int playerRow, int playerCol, GraphicsContext graphicsContext, double cellHeight, double cellWidth) {
        double x = playerCol * cellWidth;
        double y = playerRow * cellHeight;
        graphicsContext.setFill(Color.GREEN);
        if (!points[playerRow][playerCol]) {
            points[playerRow][playerCol] = true;
            Score++;
        }
        try {
            if (playerImage == null)
                playerImage = new Image(String.valueOf(getClass().getResource("/images/pacmanRight.png")));

        } catch (Exception e) {
            System.out.println("There is no player image file");
        }
        if (playerImage == null)
            graphicsContext.fillRect(x, y, cellWidth, cellHeight);
        else
            graphicsContext.drawImage(playerImage, x, y, cellWidth, cellHeight);
    }

    public void drawMazeSolution(Solution solution, GraphicsContext graphicsContext, double cellHeight, double cellWidth) {


        try {
            if (solImg == null)
                solImg = new Image(String.valueOf(getClass().getResource("/images/greanL.png")));
        } catch (Exception e) {
            System.out.println("There is no solution image file");
        }
        if (solution != null) {
            graphicsContext.setFill(Color.RED);
            if (solImg == null) {
                for (AState a : solution.getSolutionPath()) {
                    MazeState mazeState = (MazeState) a;
                    double x = mazeState.getMyPos().getColumnIndex() * cellWidth;
                    double y = mazeState.getMyPos().getRowIndex() * cellHeight;
                    graphicsContext.fillRect(x, y, cellWidth, cellHeight);


                }
            } else {
                for (AState a : solution.getSolutionPath()) {
                    MazeState mazeState = (MazeState) a;
                    double x = mazeState.getMyPos().getColumnIndex() * cellWidth;
                    double y = mazeState.getMyPos().getRowIndex() * cellHeight;
                    graphicsContext.drawImage(solImg, x, y, cellWidth, cellHeight);

                }
            }

        }

    }

    public String getScore() {
        return this.Score + "";
    }

    public void NoPoints() {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[0].length; j++) {
                points[i][j] = true;

            }
        }
    }
}
