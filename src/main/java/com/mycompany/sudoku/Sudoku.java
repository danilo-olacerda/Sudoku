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
        List<Integer> numbersToInsert = new ArrayList<>();
        
        for (int i = 0; i < randomNumbers.size(); i++) {
            int value = randomNumbers.get(i);
            numbersToInsert.add(value);
        }

        game = new Board(randomNumbers, numbersToInsert);
    }
    
    private static void customBoard() {
        String entry;
        boolean keepGet = true;
        
        while (keepGet) {
            entry = teclado.nextLine();
            String[] entries = entry.split("\\)");
        }
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
