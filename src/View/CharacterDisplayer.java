package View;

import algorithms.mazeGenerators.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.GridPane;

public class CharacterDisplayer extends GridPane {

    private double width;
    private double height;
    private int column;
    private int row;
    private int charCol;
    private int charRow;
    private double cellWidth;
    private double cellHeight;

    private StringProperty imageFilePath = new SimpleStringProperty();

    public String getImageFilePath() {
        return imageFilePath.get();
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.set(imageFilePath);
    }

    public void setCharacterDisplayer(double width, double height, int columns, int rows,int charRow,int charCol) {
        this.width = width;
        this.height = height;
        this.charCol = charCol;
        this.charRow = charRow;
        this.column=columns;
        this.row=rows;
        cellWidth = width / (double) columns;
        cellHeight = height / (double) rows;
        initialize(rows, columns);
        getChildren().clear();
        draw(charRow,charCol);

    }

    private void initialize(int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            addRow(i);
        }

        for (int j = 0; j < columns; j++) {
            addColumn(j);
        }
    }

    public void draw(int rowIndex, int columnIndex) {


//        for (int r = 0; r < row; r++) {
//            for (int c = 0; c < column; c++) {
//                if (r==rowIndex && c==columnIndex) {
                    Cell cell = new Cell(cellWidth, cellHeight, imageFilePath.toString());
                    add(cell, columnIndex,rowIndex );
                    cell.draw();
//                }
//            }
//        }
    }
}
