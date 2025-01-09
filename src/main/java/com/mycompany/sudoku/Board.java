package com.mycompany.sudoku;
import com.mycompany.sudoku.SubBoard;
import java.util.*;
import java.util.regex.*;

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
                getHint();
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
    
    private void getHint() {
        printGame();
        
        System.out.print
            ("""
            Dica de jogada: ([linha],[coluna])
            """);
        
        String entry;
        entry = teclado.nextLine();
        
        if (entry.isEmpty()) {
            entry = teclado.nextLine();
        } 
        
        String regex = "\\([1-9],[1-9]\\)";
        
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(entry);
        
        while (!matcher.matches()) {
            System.out.println("Entrada inválida, tente novamente ([linha],[coluna]): ");
            entry = teclado.nextLine();
            matcher = pattern.matcher(entry);
        }
        
        String originalEntry = entry;
        entry = entry.replace("(", "").replace(")", "");
        
        String[] coordString = entry.split(",");
        
        int x = Integer.parseInt(coordString[0]);
        int y = Integer.parseInt(coordString[1]);
        int pos = calculatePosition(x, y);
        
        List<Integer> freeNumbers = new ArrayList<>();
        Map<Integer, Boolean> usedNumbers = new HashMap<>();
        
        for (int i = 1; i <= numberOfSubBoards; i++) {
            usedNumbers.put(i, false);
        }
        
        int i = 0;
        for (SubBoard subBoard : subBoards) {
            subBoard.getInUse(x - 1, y - 1, pos, i, usedNumbers);
            i++;
        }
        
        for (Map.Entry<Integer, Boolean> usedNumber : usedNumbers.entrySet()) {
            Integer key = usedNumber.getKey();
            Boolean value = usedNumber.getValue();
            
            if (!value) freeNumbers.add(key);
        }
        
        
        
        System.out.print("Possíveis jogadas para a posição " + originalEntry + ": ");
        printArrayList(freeNumbers);
        System.out.println("");
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
    
    private void printArrayList(List<Integer> list) {
        System.out.print("[");
        
        for (int i = 0; i < list.size(); i++) {
            int value = list.get(i);
            System.out.print(value);
            
            if (i != list.size() - 1) System.out.print(",");
        }
        
        System.out.print("]");
    }
}
