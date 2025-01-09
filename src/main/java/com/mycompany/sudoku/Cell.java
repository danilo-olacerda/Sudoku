package com.mycompany.sudoku;

public class Cell {
    private int value;
    private int x = -1, y = -1;
    private boolean changeable;

    public Cell(int value, boolean changeable, int x, int y) {
        this.value = value;
        this.changeable = changeable || value == -1;
        this.x = x;
        this.y = y;
    }
    
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value == -1) return;
        
        if (!this.changeable) {
            addErrorMessage(2, value);
            return;
        }
        
        if (this.value != -1) {
            addErrorMessage(1, value);
            return;
        }
        
        this.value = value;
    }

    public boolean isChangeable() {
        return changeable;
    }

    @Override
    public String toString() {
        switch (value) {
            case -1:
                return "-";
            default:
                return "" + value;
        }
    }
    
    private void addErrorMessage(int type, int value) {
        String entry = positionString(value);
        
        switch (type) {
            case 1:
                System.out.println("A entrada " + entry + " não foi inserida, pois já possui um valor atribuído");
                break;
            case 2:
                System.out.println("A entrada " + entry + " não foi inserida, pois ela é um valor inicial e não pode ser alterado");
                break;
        }
    }
    
    private void removeErrorMessage(int type) {
        String entry = positionRemoveString();
        
        switch (type) {
            case 1:
                System.out.println("A posição " + entry + " não foi removida, pois ela é um valor inicial e não pode ser alterado");
                break;
            case 2:
                System.out.println("A posição " + entry + " não foi removida, pois não possui um valor atribuído");
                break;
        }
    }
    
    private String positionString(int newValue) {
        return "(" + (this.x + 1) + "," + (this.y + 1) + "," + newValue + ")";
    }
    
    private String positionRemoveString() {
        return "(" + (this.x + 1) + "," + (this.y + 1) + ")";
    }
    
    public void removeValue() {
        if (!this.changeable) {
            removeErrorMessage(1);
            return;
        }
        
        if (this.value == -1) {
            removeErrorMessage(2);
            return;
        }
        
        this.value = -1;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
}
