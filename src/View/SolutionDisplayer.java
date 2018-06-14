package View;

import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;

public class SolutionDisplayer extends GridPane {
    private double width;
    private double height;
    private int columns;
    private int rows;
    private Solution solution;
    //solution object*/

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }


    public void setSolutionDisplayer(Solution sol,double width, double height, int columns, int rows) {
        this.width = width;
        this.height = height;
        this.columns = columns;
        this.rows = rows;
        this.solution=sol;
        initialize(rows, columns);
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
//        if (Solution != null) {
        double cellHeight = height / rows;
        double cellWidth = width / columns;
        ArrayList<AState> solList = solution.getSolutionPath();
        try {
            //Draw Maze
            for (int i = 0; i < solList.size(); i++) {
                String[] s = solList.get(i).toString().split(" ");
                int r = Integer.parseInt(s[1]);
                int c = Integer.parseInt((s[3].split("\'"))[0]);
                Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
                add(cell, c, r);
                cell.draw();
            }

        }
        catch (Exception e) {
        }

    }
}
