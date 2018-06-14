package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

public class MazeDisplayer extends GridPane {
    private double width;
    private double height;
    private Maze maze;

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }

    public void setMaze(Maze maze, double width, double height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
        initialize(maze.getRow(), maze.getCol());
        getChildren().clear();
        draw();
    }

    private void initialize(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            addRow(i);
        }

        for (int j = 0; j < columns; j++) {
            addColumn(j);
        }
    }

    public void draw() {
        if (maze != null) {
            double cellHeight = height / maze.getRow();
            double cellWidth = width / maze.getCol();
            try {
                //Draw Maze
                for (int row = 0; row < maze.getRow(); row++) {
                    for (int column = 0; column < maze.getCol(); column++) {
                        if (maze.getValueAt(new Position(row,column)) == 1) {
                            Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
                            add(cell,column , row);
                            cell.draw();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
    }
    //endregion
}
