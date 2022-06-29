package Model;

import Client.Client;
import IO.MyCompressorOutputStream;
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
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Observable;


public class MyModel extends Observable implements IModel {
    private static Server mazeGeneratingServer = null;
    private static Server solveSearchProblemServer = null;
    Maze maze;
    int PlayerPosX;
    int PlayerPosY;
    Solution solution;

    public MyModel() {
    }

    public void startServers() {
        if (mazeGeneratingServer == null) {

            ServerStrategyGenerateMaze serverStrategyGenerateMaze=new ServerStrategyGenerateMaze();
            mazeGeneratingServer = new Server(5500, 1000, serverStrategyGenerateMaze);
            mazeGeneratingServer.start();


        }
        if (solveSearchProblemServer == null) {

            solveSearchProblemServer = new Server(5501, 1000, new
                    ServerStrategySolveSearchProblem());
            solveSearchProblemServer.start();

        }
    }

    @Override
    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5500, new
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
                                byte[] decompressedMaze = new byte[row * col + 24/*CHANGESIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed
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
        solution = null;
        setChanged();
        notifyObservers("Maze generated");


    }

    @Override
    public void moveCharacter(KeyEvent movement) {

        switch (movement.getCode()) {
            case UP:
            case NUMPAD8:
                if (PlayerPosX > 0 && maze.getPosition(PlayerPosX - 1, PlayerPosY).getVal() == 0) {
                    PlayerPosX -= 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case DOWN:
            case NUMPAD2:
                if (PlayerPosX + 1 < maze.getRow() && maze.getPosition(PlayerPosX + 1, PlayerPosY).getVal() == 0) {
                    PlayerPosX += 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case RIGHT:
            case NUMPAD6:
                if (PlayerPosY + 1 < maze.getCol() && maze.getPosition(PlayerPosX, PlayerPosY + 1).getVal() == 0) {
                    PlayerPosY += 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case LEFT:
            case NUMPAD4:
                if (PlayerPosY > 0 && maze.getPosition(PlayerPosX, PlayerPosY - 1).getVal() == 0) {
                    PlayerPosY -= 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case NUMPAD1:
                if (PlayerPosY > 0 && PlayerPosX + 1 < maze.getRow() && maze.getPosition(PlayerPosX + 1, PlayerPosY - 1).getVal() == 0 &&
                        ((maze.getPosition(PlayerPosX + 1, PlayerPosY).getVal() == 0) || maze.getPosition(PlayerPosX, PlayerPosY - 1).getVal() == 0)) {
                    PlayerPosY -= 1;
                    PlayerPosX += 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case NUMPAD3:
                if (PlayerPosY + 1 < maze.getCol() && PlayerPosX + 1 < maze.getRow() && maze.getPosition(PlayerPosX + 1, PlayerPosY + 1).getVal() == 0 &&
                        ((maze.getPosition(PlayerPosX + 1, PlayerPosY).getVal() == 0) || maze.getPosition(PlayerPosX, PlayerPosY + 1).getVal() == 0)) {
                    PlayerPosY += 1;
                    PlayerPosX += 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
                break;
            case NUMPAD7:
                if (PlayerPosY > 0 && PlayerPosX > 0 && maze.getPosition(PlayerPosX - 1, PlayerPosY - 1).getVal() == 0 &&
                        ((maze.getPosition(PlayerPosX - 1, PlayerPosY).getVal() == 0) || maze.getPosition(PlayerPosX, PlayerPosY - 1).getVal() == 0)) {
                    PlayerPosY -= 1;
                    PlayerPosX -= 1;
                    setChanged();
                    notifyObservers("Character moved");
                }
            case NUMPAD9:
                if (PlayerPosY + 1 < maze.getCol() && PlayerPosX > 0 && maze.getPosition(PlayerPosX - 1, PlayerPosY + 1).getVal() == 0 &&
                        ((maze.getPosition(PlayerPosX - 1, PlayerPosY).getVal() == 0) || maze.getPosition(PlayerPosX, PlayerPosY + 1).getVal() == 0)) {
                    PlayerPosY += 1;
                    PlayerPosX -= 1;
                    setChanged();
                    notifyObservers("Character moved");

                }
                break;
        }
        if (PlayerPosX == maze.getGoalPosition().getRowIndex() && PlayerPosY == maze.getGoalPosition().getColumnIndex()) {
            setChanged();
            notifyObservers("Win");
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
        if (maze == null)
            return;
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5501, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer,
                                           OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        solution = (Solution) fromServer.readObject();
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
        setChanged();
        notifyObservers("Maze solved");

    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public void save(String filename) {
        if (maze == null)
            return;
        try {
            filename = filename + ".maze";
            MyCompressorOutputStream myCompressorOutputStream = new MyCompressorOutputStream(new FileOutputStream(filename));
            myCompressorOutputStream.write(maze.toByteArray());

            myCompressorOutputStream.flush();
            myCompressorOutputStream.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    @Override
    public void load(String path) {
        try {


            FileInputStream fileInputStream = new FileInputStream(path);
            byte[] compressedMaze = fileInputStream.readAllBytes(); //read generated maze (compressed with MyCompressor) from server
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            int row = ByteBuffer.wrap(Arrays.copyOfRange(compressedMaze, 0, 4)).getInt();
            int col = ByteBuffer.wrap(Arrays.copyOfRange(compressedMaze, 4, 8)).getInt();
            byte[] decompressedMaze = new byte[row * col + 24/*CHANGESIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed
            is.read(decompressedMaze);
            maze = new Maze(decompressedMaze);

            PlayerPosX = maze.getStartPosition().getRowIndex();
            PlayerPosY = maze.getStartPosition().getColumnIndex();
            solution = null;
            setChanged();
            notifyObservers("Maze generated");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void stopServers() {
        mazeGeneratingServer.stop();
        solveSearchProblemServer.stop();

    }

    public void Clear() {
        maze = null;
        PlayerPosX = 0;
        PlayerPosY = 0;
        solution = null;
        setChanged();
        notifyObservers("Maze Deleted");


    }

    public void ClearSolution() {
        solution = null;
        setChanged();
        notifyObservers("Solution Deleted");
    }
}
