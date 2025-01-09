package com.mycompany.sudoku;
import com.mycompany.sudoku.Cell;
import java.util.*;

public class SubBoard {
    List<Cell> cells = new ArrayList<>();
    private final int numberOfCells = 9;
    
    public SubBoard (Map<Integer, Integer> initialValues, int index) {
        int initialValue = getInitialValue(index) * 27;
        
        for (int i = 0; i < numberOfCells; i++) {
            int verticalToSum = (i / 3) * 9;
            int horizontalToSum = (index % 3) * 3;
            int innerIndexToSum = i % 3;
            int cellKey = initialValue + verticalToSum + horizontalToSum + innerIndexToSum;
            int x = calcX(index, i);
            int y = calcY(index, i);
            
            cells.add(new Cell(initialValues.getOrDefault(cellKey, -1), false, x, y));
        }
    }
    
    private static int getInitialValue(int index) {
        int valueToCompare = index / 3;
        
        if (valueToCompare < 1) return 0;
        if (valueToCompare >= 1 && valueToCompare < 2) return 1;
        
        return 2;
    }
    
    private static int calcX(int outIndex, int inIndex) {
        return (outIndex % 3) * 3 + (inIndex % 3);
    }
    
    private static int calcY(int outIndex, int inIndex) {
        return (outIndex / 3) * 3 + (inIndex / 3);
    }
    
    public void printBoard(int y) {
        for (int i = 0; i < numberOfCells / 3; i++) {
            if (i == 0 || i == numberOfCells / 3) System.out.print(" | ");
            
            System.out.print(cells.get(i + (y * 3)) + " | ");
        }
    }
    
    public void addMove(Map<Integer, Integer> valueToAdd, int index) {
        int initialValue = getInitialValue(index) * 27;
        
        for (int i = 0; i < numberOfCells; i++) {
            int verticalToSum = (i / 3) * 9;
            int horizontalToSum = (index % 3) * 3;
            int innerIndexToSum = i % 3;
            int cellKey = initialValue + verticalToSum + horizontalToSum + innerIndexToSum;
            Cell cell = cells.get(i);
            
            int currentValue = valueToAdd.getOrDefault(cellKey, -1);
            
            if (currentValue == -1) continue;
            
            cell.setValue(currentValue);
        }
    }
    
    public void removeMove(List<Integer> positions, int index) {
        int initialValue = getInitialValue(index) * 27;
        
        for (int i = 0; i < numberOfCells; i++) {
            int verticalToSum = (i / 3) * 9;
            int horizontalToSum = (index % 3) * 3;
            int innerIndexToSum = i % 3;
            int cellKey = initialValue + verticalToSum + horizontalToSum + innerIndexToSum;
            Cell cell = cells.get(i);
            
            int currentValue = positions.indexOf(cellKey);
            
            if (currentValue == -1) continue;
            
            cell.removeValue();
        }
    }
}
