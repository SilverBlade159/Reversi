package player.ai;

import java.util.ArrayList;

import game.BoardHelper;
import player.evaluator.StaticEvaluator;

public class Board {
    public static final int W = 2;
    public static final int B = 1;
    public static final int EMPTY = 0;

    private int[][] gameBoard;

    private int lastPlayer;

    private Move lastMove;

    private int dimension;

    public Board(int playerMark) {
        this.lastMove = new Move();
        this.lastPlayer = playerMark;
        this.gameBoard = new int[8][8];
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard.length; j++) {
                if ((i == 3 && j == 3) || (i == 4 && j == 4))
                    this.gameBoard[i][j] = W;
                else if ((i == 3 && j == 4) || (i == 4 && j == 3))
                    this.gameBoard[i][j] = B;
                else
                    this.gameBoard[i][j] = EMPTY;
            }
        }
    }

    // copy constructor
    public Board(Board board) {
        this.lastMove = board.lastMove;
        this.lastPlayer = board.lastPlayer;
        this.gameBoard = new int[8][8];
        for (int i = 0; i < this.gameBoard.length; i++) {
            for (int j = 0; j < this.gameBoard.length; j++) {
                this.gameBoard[i][j] = board.gameBoard[i][j];
            }
        }
    }

    public Board(int[][] board) {
        gameBoard = board;
    }

    public void print() {
        System.out.println("\n***************************");
        for (int row = 0; row < gameBoard.length; row++) {
            System.out.print("* ");
            for (int col = 0; col < gameBoard.length; col++) {
                switch (this.gameBoard[row][col]) {
                    case Board.B -> System.out.print("⚫ ");
                    case Board.W -> System.out.print("⚪ ");
                    case EMPTY -> System.out.print("🟩");
                    default -> {
                    }
                }
            }
            System.out.println("*");
        }
        System.out.println("***************************\n\n");
    }

    ArrayList<Board> getChildren(int player) {
        ArrayList<Board> result = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // if (this.isValidMove(gameBoard, player, i, j)) {
                if (BoardHelper.canPlay(gameBoard, player, i, j)) {
                    Board child = new Board(this);
                    child.makeMove(i, j, player);
                    result.add(child);
                }
            }
        }
        return result;
    }

    public boolean isTerminal() {
        return BoardHelper.isGameFinished(gameBoard);
    }

    // create our own heuristics
    public int evaluate() {
        return StaticEvaluator.evalBoardMap(gameBoard, lastPlayer);
    }

    public Move getLastMove() {
        return this.lastMove;
    }

    public int getLastPlayer() {
        return this.lastPlayer;
    }

    public int[][] getGameBoard() {
        return this.gameBoard;
    }

    void setGameBoard(int[][] gameBoard) {
        for (int i = 0; i < this.dimension; i++) {
            for (int j = 0; j < this.dimension; j++) {
                this.gameBoard[i][j] = gameBoard[i][j];
            }
        }
    }

    void setLastMove(Move lastMove) {
        this.lastMove.setRow(lastMove.getRow());
        this.lastMove.setCol(lastMove.getCol());
        this.lastMove.setValue(lastMove.getValue());
    }

    void setLastPlayer(int lastPlayer) {
        this.lastPlayer = lastPlayer;
    }

    // Make a move; it places a letter in the board
    void makeMove(int row, int col, int letter) {
        this.gameBoard[row][col] = letter;
        this.lastMove = new Move(row, col);
        this.lastPlayer = letter;
    }
}