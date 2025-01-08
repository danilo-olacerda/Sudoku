package com.mycompany.sudoku;
import com.mycompany.sudoku.ValidBoards;
import com.mycompany.sudoku.Board;
import java.util.*;

public class Sudoku {
    private static int gameType = -1;
    private static Scanner teclado = new Scanner(System.in);
    private static int quantityToStart = -1;
    private static final int min = 0;
    private static final int max = 80;
    private static Board game;
    
    public static void main(String[] args) {
        startNewGame();
    }
    
    private static void startNewGame() {
        welcomeMessage();
        setGameType();
        setBoard();
        game.play();
    }
    
    private static void welcomeMessage() {
        System.out.print(
        """
        #########################
            BEM-VINDO AO JOGO
                 SUDOKU
        #########################
        POR FAVOR ESCOLHA O MODO!
            1 - Aleatório
            2 - Definir jogo
        """);
    }
    
    private static void setGameType() {
        gameType = -1;
        
        while (gameType != 1 && gameType != 2) {
            try {
                gameType = teclado.nextInt();
            } catch (InputMismatchException e) {
                teclado.nextLine();
            }
        }
    }
    
    private static void setBoard() {
        switch (gameType) {
            case 1:
                System.out.println("Modo aleatório selecionado, quantos números deseja sortear ? (" + min + " a " + max + ")");
                
                quantityToStart = -1;
                
                while (quantityToStart < min || quantityToStart > max) {
                    try {
                        quantityToStart = teclado.nextInt();
                    } catch (InputMismatchException e) {
                        teclado.nextLine();
                    }
                }
                
                randomBoard();
                break;
            case 2:
                System.out.print
            ("""
            Modo definir jogo selecionado, define os valores iniciais do jogo com o seguinte formato: 
            ([linha],[coluna],[valor])
            Várias entradas são aceitas ! Ex.: (1,2,3)(4,5,6)(7,8,9)
            Quando estiver satisfeito coloque a entrada (-1,-1,-1)
            """);
                
                customBoard();
                break;
        }
    }
    
    private static void randomBoard() {
        List<Integer> randomNumbers = getRandomNumbersInRange(quantityToStart);
        int[] validBoard = ValidBoards.getRandomValidBoard();
        Map<Integer, Integer> numbersToInsert = new HashMap<>();
        
        for (int i = 0; i < randomNumbers.size(); i++) {
            int key = randomNumbers.get(i);
            int value = validBoard[key];
            numbersToInsert.put(key, value);
        }

        game = new Board(numbersToInsert);
    }
    
    private static void customBoard() {
        String entry;
        boolean keepGet = true;
        Map<Integer, Integer> numbersToInsert = new HashMap<>();
        
        while (keepGet) {
            entry = teclado.nextLine();
            String[] entries = entry.split("\\)");
            
            filterValidInsertions(entries, numbersToInsert);
            
            if (numbersToInsert.getOrDefault(-1, 0) == -1) {
                keepGet = false;
                numbersToInsert.remove(-1);
            }
        }
    }
    
    private static void filterValidInsertions(String[] entriesToFilter, Map<Integer, Integer> valuesMap) {
        for (int i = 0; i < entriesToFilter.length; i++) {
            String toInsert = entriesToFilter[i];
            String originalString = toInsert;
            toInsert = toInsert.replace("\\(", "").replace("\\)", "");
            
            String[] stringNumbers = toInsert.split(",");
            List<Integer> numbers = new ArrayList<>();
            
            try {
                for (int j = 0; j < stringNumbers.length; j++) {
                    numbers.add(Integer.parseInt(stringNumbers[i]));
                }
            } catch (Error e) {
                System.out.println("Entrada " + originalString + " não adicionada pois o formato está inválido !");
            }
            
            if (validateNumberList(numbers)) {
                int key = calculatePosition(numbers.get(0), numbers.get(1));
                int value = numbers.get(2);
                valuesMap.put(key, value);
            }
            
            if (validateIsStop(numbers)) {
                valuesMap.put(-1, -1);
            }
        }
    }
    
    private static boolean validateIsStop(List<Integer> list) {
        return list.get(0) == -1 && list.get(1) == -1 && list.get(2) == -1;
    }
    
    private static boolean validateNumberList(List<Integer> list) {
        return isValidPosition(list.get(0), list.get(1)) && isValidValue(list.get(2));
    }
    
    private static boolean isValidPosition(int x, int y) {
        int position = calculatePosition(x, y);
        return position >= min && position <= max;
    }
    
    private static boolean isValidValue(int value) {
        return value >= 1 && value <= 9;
    }
    
    private static int calculatePosition(int x, int y) {
        return ((y - 1) * 9) + x;
    }
    
    public static List<Integer> getRandomNumbersInRange(int count) {
        List<Integer> numbers = new ArrayList<>();
        
        for (int i = min; i <= max; i++) {
            numbers.add(i);
        }

        Collections.shuffle(numbers);

        return numbers.subList(0, count);
    }
}
