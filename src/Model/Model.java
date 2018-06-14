package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import algorithms.mazeGenerators.Position;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model extends Observable implements IModel{


    Server mazeGeneratingServer;
    Server solveSearchProblemServer;

    public Model() {
        //Raise the servers

        //Initializing servers
        mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());


    }

    public void startServers() {
        //Starting  servers
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
    }

    public void stopServers() {
        solveSearchProblemServer.stop();
        mazeGeneratingServer.stop();
    }

    private Maze maze;
    private Solution mazeSolution;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;

    @Override
    public void generateMaze(int width, int height) {
        //Generate maze
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{height, width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[100000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        //maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void solveMaze(Maze maze) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
/*
                        //Print Maze Solution retrieved from the server
                        System.out.println(String.format("Solution steps: %s", mazeSolution));
                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public void moveCharacter(KeyCode movement) {
        switch (movement) {
            case NUMPAD8:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn))
                    characterPositionRow--;
                break;
            case NUMPAD2:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn))
                    characterPositionRow++;
                break;
            case NUMPAD6:
                if(checkIfLegal(characterPositionRow,characterPositionColumn+1))
                    characterPositionColumn++;
                break;
            case NUMPAD4:
                if(checkIfLegal(characterPositionRow,characterPositionColumn-1))
                    characterPositionColumn--;
                break;
            case NUMPAD9:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn+1)) {
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD7:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn-1)) {
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD3:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn+1)) {
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD1:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn-1)) {
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;


        }
        setChanged();
        notifyObservers();
    }

    private boolean checkIfLegal(int row, int column) {
        if (maze.inBound(row, column)) {
            if (maze.getValueAt(new Position(row, column)) == 0) {
                return true;
            }
        }
        return false;
    }




    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    public Solution getMazeSolution() {
        return mazeSolution;
    }
}
