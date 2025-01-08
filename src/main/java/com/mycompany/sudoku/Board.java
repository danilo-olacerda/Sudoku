package com.mycompany.sudoku;
import com.mycompany.sudoku.SubBoard;

import java.util.*;

public class Board {
    private List<SubBoard> subBoards = new ArrayList<>();
    private final int numberOfSubBoards = 9;
    
    public Board(Map<Integer, Integer> initialValues) {   
        for (int i = 0; i < numberOfSubBoards; i++) {
            subBoards.add(new SubBoard(initialValues, i));
        }
    }
    
    public void play() {
        printGame();
    }
    
    public void printGame() {
        int line = 0;
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                subBoards.get(j + line * 3).printBoard(i);
            }
            
            System.out.println("");
            
            if (line < 2 && i == 2) {
                i = -1;
                line++;
            }
        }
    }
}
