package com.mycompany.sudoku;

public class Cell {
    private int value;
    private int x = -1, y = -1;
    private boolean changeable;

    public Cell(int value, boolean changeable) {
        this.value = value;
        this.changeable = changeable;
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isChangeable() {
        return changeable;
    }

    @Override
    public String toString() {
        switch (value) {
            case -1:
                return "";
            default:
                return "" + value;
        }
    }
}
