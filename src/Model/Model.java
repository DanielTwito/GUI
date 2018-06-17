package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import algorithms.mazeGenerators.Position;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Model extends Observable implements IModel{


    Server mazeGeneratingServer;
    Server solveSearchProblemServer;
    boolean isReached = false;

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
    private int characterPositionRow = 5;
    private int characterPositionColumn = 5;

    @Override
    public void generateMaze(int height, int width) {
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
                        byte[] decompressedMaze = new byte[100000000 /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        maze = new Maze(decompressedMaze);
                        characterPositionRow=maze.getStartPosition().getRowIndex();
                        characterPositionColumn=maze.getStartPosition().getColumnIndex();
                        isReached=false;
                        //maze.print();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers("maze");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void solveMaze() {
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
            setChanged();
            notifyObservers("solve");
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
            case DIGIT8:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn))
                    characterPositionRow--;
                break;
            case DIGIT2:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn))
                    characterPositionRow++;
                break;
            case DIGIT6:
                if(checkIfLegal(characterPositionRow,characterPositionColumn+1))
                    characterPositionColumn++;
                break;
            case DIGIT4:
                if(checkIfLegal(characterPositionRow,characterPositionColumn-1))
                    characterPositionColumn--;
                break;
            case DIGIT9:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn+1)) {
                    if(checkIfLegal(characterPositionRow-1,characterPositionColumn) ||
                       checkIfLegal(characterPositionRow,characterPositionColumn+1)){
                        characterPositionRow--;
                        characterPositionColumn++;
                    }

                }
                break;
            case DIGIT7:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn-1)) {
                    if(checkIfLegal(characterPositionRow-1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn-1)) {
                        characterPositionRow--;
                        characterPositionColumn--;
                    }
                }
                break;
            case DIGIT3:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn+1)) {
                    if(checkIfLegal(characterPositionRow+1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn+1)) {
                        characterPositionRow++;
                        characterPositionColumn++;
                    }
                }
                break;
            case DIGIT1:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn-1)) {
                    if(checkIfLegal(characterPositionRow+1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn-1)) {
                        characterPositionRow++;
                        characterPositionColumn--;
                    }
                }
                break;

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
                    if(checkIfLegal(characterPositionRow-1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn+1)){
                        characterPositionRow--;
                        characterPositionColumn++;
                    }

                }
                break;
            case NUMPAD7:
                if(checkIfLegal(characterPositionRow-1,characterPositionColumn-1)) {
                    if(checkIfLegal(characterPositionRow-1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn-1)) {
                        characterPositionRow--;
                        characterPositionColumn--;
                    }
                }
                break;
            case NUMPAD3:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn+1)) {
                    if(checkIfLegal(characterPositionRow+1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn+1)) {
                        characterPositionRow++;
                        characterPositionColumn++;
                    }
                }
                break;
            case NUMPAD1:
                if(checkIfLegal(characterPositionRow+1,characterPositionColumn-1)) {
                    if(checkIfLegal(characterPositionRow+1,characterPositionColumn) ||
                            checkIfLegal(characterPositionRow,characterPositionColumn-1)) {
                        characterPositionRow++;
                        characterPositionColumn--;
                    }
                }
                break;

        }
        reachGoal();
        setChanged();
        notifyObservers("character");
    }

    private void reachGoal() {
        if (characterPositionColumn == maze.getGoalPosition().getColumnIndex() &&
                characterPositionRow == maze.getGoalPosition().getRowIndex())
            isReached = true;
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


    @Override
    public boolean isReached() {
        return isReached;
    }

    @Override
    public ArrayList<Position> getSolutionPath() {
        ArrayList<Position> sol = new ArrayList<>();
        for(AState st :  mazeSolution.getSolutionPath() ){
            String[] s = st.toString().split(" ");
            int r = Integer.parseInt(s[1]);
            int c = Integer.parseInt((s[3].split("\'"))[0]);
            sol.add(new Position(r,c));
        }
        return sol;
    }

    @Override
    public void moveCharacterMouse(int row, int col) {

        if(checkIfLegal(row,col) && (characterPositionRow!=row || characterPositionColumn != col)){
            characterPositionColumn=col;
            characterPositionRow=row;
            reachGoal();
            setChanged();
            notifyObservers("character");
        }

    }

    @Override
    public void loadMaze(File file){
        try {
            FileInputStream fis= new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] savedMaze = (byte[]) ois.readObject();
            ois.close();
            maze = new Maze(savedMaze);
        } catch (Exception e) {
            maze = null;
        }
        characterPositionRow=maze.getStartPosition().getRowIndex();
        characterPositionColumn=maze.getStartPosition().getColumnIndex();
        setChanged();
        notifyObservers("loaded");
    }

    public void saveMaze(File file){
        try {
            FileOutputStream fout = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(maze.toByteArray());
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
