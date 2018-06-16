package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public interface IModel {

    void generateMaze(int height, int width);

    void solveMaze();

    void moveCharacter(KeyCode movement);

    Maze getMaze();

    int getCharacterPositionRow();

    int getCharacterPositionColumn();

    boolean isReached();

    public void loadMaze(File file);

    public void saveMaze(File file);

    ArrayList<Position> getSolutionPath();
}
