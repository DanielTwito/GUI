package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

public class MazeDisplayer extends GridPane {
    private double width;
    private double height;
    private int[][] maze;

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }

    public void setMaze(int[][] maze, double width, double height) {
        this.maze = maze;
        this.width = width;
        this.height = height;
        initialize(maze.length, maze[0].length);
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
            double cellHeight = height / maze.length;
            double cellWidth = width / maze[0].length;
            try {
                //Draw Maze
                for (int row = 0; row < maze.length; row++) {
                    for (int column = 0; column < maze[row].length; column++) {
                        if (maze[row][column] == 1) {
                            Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
                            add(cell, row, column);
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
