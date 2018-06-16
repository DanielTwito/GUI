package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

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
    ArrayList<Position> getSolutionPath();
}
