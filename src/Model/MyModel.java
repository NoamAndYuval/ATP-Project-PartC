package Model;

import Client.Client;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;


public class MyModel extends Observable implements IModel {
     private Server mazeGeneratingServer;
     private Server solveSearchProblemServer;
     Maze maze ;
     int PlayerPosX;
     int PlayerPosY;
     Solution solution;

    public MyModel() {
        mazeGeneratingServer = new Server(5400, 1000, new
                ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new
                ServerStrategySolveSearchProblem());
    }

    public void startServers(){
        mazeGeneratingServer.start();
        solveSearchProblemServer.start();

    }

    @Override
    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new
                    IClientStrategy() {
                        @Override
                        public void clientStrategy(InputStream inFromServer,
                                                   OutputStream outToServer) {
                            try {
                                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                                toServer.flush();
                                int[] mazeDimensions = new int[]{row, col};
                                toServer.writeObject(mazeDimensions);
                                //send maze dimensions to server
                                toServer.flush();
                                byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                                InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                                byte[] decompressedMaze = new byte[row*col+24/*CHANGESIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed
                                is.read(decompressedMaze); //Fill decompressedMaze with bytes

                                maze = new Maze(decompressedMaze);
                                PlayerPosX = maze.getStartPosition().getRowIndex();
                                PlayerPosY = maze.getStartPosition().getColumnIndex();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void moveCharacter(KeyEvent movement) {

        switch (movement.getCode()) {
            case UP:
                if (PlayerPosX > 0 && maze.getPosition(PlayerPosX - 1, PlayerPosY).getVal() == 0)
                    PlayerPosX -= 1;
                break;
            case DOWN:
                if (PlayerPosX + 1 < maze.getRow() && maze.getPosition(PlayerPosX + 1, PlayerPosY).getVal() == 0)
                    PlayerPosX += 1;
                break;
            case RIGHT:
                if (PlayerPosY + 1 < maze.getCol() && maze.getPosition(PlayerPosX, PlayerPosY + 1).getVal() == 0)
                    PlayerPosY += 1;
                break;
            case LEFT:
                if (PlayerPosY > 0 && maze.getPosition(PlayerPosX, PlayerPosY - 1).getVal() == 0)
                    PlayerPosY -= 1;
                break;

        }
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public int getCharacterPositionRow() {
        return PlayerPosX;
    }

    @Override
    public int getCharacterPositionCol() {
        return PlayerPosY;
    }

    @Override
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer,
                                           OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        MyMazeGenerator mg = new MyMazeGenerator();
                        Maze maze = mg.generate(50,50);
                        maze.print();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution= (Solution) fromServer.readObject();
                        //read generated maze (compressed with MyCompressor) from server

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void save(String filename) {

    }

    @Override
    public void load(String path) {

    }

    public void stopServers(){
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();

    }
}
