package com.mycompany.sudoku;
import com.mycompany.sudoku.SubBoard;
import java.util.*;

public class Board {
    private static Scanner teclado = new Scanner(System.in);
    private List<SubBoard> subBoards = new ArrayList<>();
    private final int numberOfSubBoards = 9;
    
    public Board(Map<Integer, Integer> initialValues) {   
        for (int i = 0; i < numberOfSubBoards; i++) {
            subBoards.add(new SubBoard(initialValues, i));
        }
    }
    
    public void play() {
        System.out.println("Jogo criado com sucesso ! (As posições iniciais não podem ser alteradas)");
        printGame();
        chooseAction();
    }
    
    public void printGame() {
        int line = 0;
        
        System.out.println(" -------------------------------------------");
        
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                subBoards.get(j + line * 3).printBoard(i);
            }
            
            System.out.println("");
            
            if (line < 2 && i == 2) {
                i = -1;
                System.out.println(" -------------------------------------------");
                line++;
            }
        }
        
        System.out.println(" -------------------------------------------");
    }
    
    private void chooseAction() {
        int option = -1;
        
        printActions();
        option = teclado.nextInt();
        
        while (option < 1 || option > 5) {
            try {
                printGame();
                printActions();
                option = teclado.nextInt();
            } catch (InputMismatchException e) {
                teclado.nextLine();
            }
        }
        
        doAction(option);
    }
    
    private void doAction(int option) {
        switch (option) {
            case 1:
                addMove();
                break;
            case 2:
                removeMove();
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                return;
        }
        
        printGame();
        chooseAction();
    }
    
    private void addMove() {
        printGame();
        
        System.out.print
            ("""
            Adicionar jogada: ([linha],[coluna],[valor])
            Várias entradas são aceitas ! Ex.: (1,2,3)(4,5,6)(7,8,9)
            """);
        
        String entry;
        teclado.nextLine();
        Map<Integer, Integer> numbersToInsert = new HashMap<>();
        
        entry = teclado.nextLine();
        String[] entries = entry.split("\\)");

        filterValidInsertions(entries, numbersToInsert);
        
        int i = 0;
        for (SubBoard subBoard : subBoards) {
            subBoard.addMove(numbersToInsert, i);
            i++;
        }
    }
    
    private void removeMove() {
        printGame();
        
        System.out.print
            ("""
            Remover jogada: ([linha],[coluna])
            Várias entradas são aceitas ! Ex.: (1,2)(4,5)(7,8)
            """);
        
        String entry;
        teclado.nextLine();
        List<Integer> numbersToRemove = new ArrayList<>();
        
        entry = teclado.nextLine();
        String[] entries = entry.split("\\)");

        filterValidRemovals(entries, numbersToRemove);
        
        int i = 0;
        for (SubBoard subBoard : subBoards) {
            subBoard.removeMove(numbersToRemove, i);
            i++;
        }
    }
    
    private static void printActions() {
        System.out.print(
            """
            Escolha uma ação:
            1 - Adicionar jogada
            2 - Remover jogada
            3 - Verificar
            4 - Dica
            5 - Sair
            """
        );
    }
    
    private static void filterValidInsertions(String[] entriesToFilter, Map<Integer, Integer> valuesMap) {
        for (String toInsert : entriesToFilter) {
            String originalString = toInsert;
            toInsert = toInsert.replaceAll("\\(", "").replaceAll("\\)", "");
            
            String[] stringNumbers = toInsert.split(",");
            List<Integer> numbers = new ArrayList<>();
            
            try {
                for (String stringNumber : stringNumbers) {
                    numbers.add(Integer.valueOf(stringNumber));
                }
            } catch (Error e) {
                notAcceptedEntry(originalString);
            }
            
            if (validateNumberList(numbers)) {
                int key = calculatePosition(numbers.get(0), numbers.get(1));
                int value = numbers.get(2);
                valuesMap.put(key, value);
            } else {
                notAcceptedEntry(originalString);
            }
        }
    }
    
    private static void filterValidRemovals(String[] entriesToFilter, List<Integer> values) {
        for (String toInsert : entriesToFilter) {
            String originalString = toInsert;
            toInsert = toInsert.replaceAll("\\(", "").replaceAll("\\)", "");
            
            String[] stringNumbers = toInsert.split(",");
            List<Integer> numbers = new ArrayList<>();
            
            try {
                for (String stringNumber : stringNumbers) {
                    numbers.add(Integer.valueOf(stringNumber));
                }
            } catch (Error e) {
                notAcceptedRemoval(originalString);
            }
            
            if (validateNumberListRemoval(numbers)) {
                int key = calculatePosition(numbers.get(0), numbers.get(1));
                values.add(key);
            } else {
                notAcceptedRemoval(originalString);
            }
        }
    }
    
    private static void notAcceptedEntry(String entry) {
        System.out.println("Entrada " + entry + ")" + " não adicionada pois o formato está inválido !");
    }
    
    private static void notAcceptedRemoval(String entry) {
        System.out.println("Entrada " + entry + ")" + " não removida pois o formato está inválido !");
    }
    
    private static boolean validateNumberList(List<Integer> list) {
        if (list.size() < 3) return false;
        return compareValue(list.get(0)) && compareValue(list.get(1)) && compareValue(list.get(2));
    }
    
    private static boolean validateNumberListRemoval(List<Integer> list) {
        if (list.size() != 2) return false;
        return compareValue(list.get(0)) && compareValue(list.get(1));
    }
    
    private static boolean compareValue(int value) {
        return value >= 1 && value <= 9;
    }
    
    private static int calculatePosition(int x, int y) {
        return ((y - 1) * 9) + x - 1;
    }
}
