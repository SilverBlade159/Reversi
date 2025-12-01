package game;

import player.ai.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GamePanel extends JPanel implements GameEngine {

    // reversi board
    int[][] board;

    // player turn
    // black plays first
    int turn = Board.B;

    // swing elements
    BoardCell[][] cells;
    JLabel score1;
    JLabel score2;

    int totalscore1 = 0;
    int totalscore2 = 0;

    JLabel tscore1;
    JLabel tscore2;

    GamePlayer player1;
    GamePlayer player2;

    Timer player1HandlerTimer;
    Timer player2HandlerTimer;

    private boolean awaitForClick = false;

    // main constructor
    public GamePanel(int maxDepth, boolean userPlaysFirst) {
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());

        // dimiourgia paikton me vasi tin seira
        if (userPlaysFirst) {
            // xristis=mavra,minimax=aspra
            player1 = new HumanPlayer(Board.B);
            player2 = new MinimaxPlayer(Board.W, maxDepth);
        } else {
            // Minimax = mavra, xristis=aspra
            player1 = new MinimaxPlayer(Board.B, maxDepth);
            player2 = new HumanPlayer(Board.W);
        }

        turn = Board.B;

        JPanel reversiBoard = new JPanel();
        reversiBoard.setLayout(new GridLayout(8, 8));
        reversiBoard.setPreferredSize(new Dimension(500, 500));
        reversiBoard.setBackground(new Color(41, 100, 59));

        // init board
        resetBoard();

        cells = new BoardCell[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                cells[i][j] = new BoardCell(this, reversiBoard, i, j);
                reversiBoard.add(cells[i][j]);
            }
        }

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));

        score1 = new JLabel("Score 1");
        score2 = new JLabel("Score 2");

        tscore1 = new JLabel("Total Score 1");
        tscore2 = new JLabel("Total Score 2");

        sidebar.add(score1);
        sidebar.add(score2);

        sidebar.add(new JLabel("-----------"));

        sidebar.add(tscore1);
        sidebar.add(tscore2);

        this.add(sidebar, BorderLayout.WEST);
        this.add(reversiBoard);

        updateBoardInfo();
        updateTotalScore();

        // AI Handler Timer (to unfreeze gui)
        player1HandlerTimer = new Timer(1000, (ActionEvent e) -> {
            handleAI(player1);
            player1HandlerTimer.stop();
            manageTurn();
        });

        player2HandlerTimer = new Timer(1000, (ActionEvent e) -> {
            handleAI(player2);
            player2HandlerTimer.stop();
            manageTurn();
        });

        manageTurn();
    }

    // constructor poy kaleitai me default timi se periptosi poy den perastoyn oi parametroi
    public GamePanel() {
        this(4, true);
    }

    @Override
    public int getBoardValue(int i, int j) {
        return board[i][j];
    }

    @Override
    public void setBoardValue(int i, int j, int value) {
        board[i][j] = value;
    }

    public void manageTurn() {
        if (BoardHelper.hasAnyMoves(board, Board.B) || BoardHelper.hasAnyMoves(board, Board.W)) {
            updateBoardInfo();
            if (turn == Board.B) {
                if (BoardHelper.hasAnyMoves(board, Board.B)) {
                    if (player1.isUserPlayer()) {
                        awaitForClick = true;
                    } else {
                        player1HandlerTimer.start();
                    }
                } else {
                    System.out.println("Player 1 has no legal moves !");
                    turn = Board.W;
                    manageTurn();
                }
            } else {
                if (BoardHelper.hasAnyMoves(board, Board.W)) {
                    if (player2.isUserPlayer()) {
                        awaitForClick = true;
                    } else {
                        player2HandlerTimer.start();
                    }
                } else {
                    System.out.println("Player 2 has no legal moves !");
                    turn = Board.B;
                    manageTurn();
                }
            }
        } else {
            // game finished
            System.out.println("Game Finished !");
            int winner = BoardHelper.getWinner(board);
            if (winner == Board.B)
                totalscore1++;
            else if (winner == Board.W)
                totalscore2++;
            updateTotalScore();
        }
    }

    public void resetBoard() {
        board = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = 0;
            }
        }
        // initial board state
        setBoardValue(3, 3, Board.W);
        setBoardValue(3, 4, Board.B);
        setBoardValue(4, 3, Board.B);
        setBoardValue(4, 4, Board.W);
    }

    // update highlights on possible moves and scores
    public void updateBoardInfo() {

        int p1score = 0;
        int p2score = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == Board.B)
                    p1score++;
                if (board[i][j] == Board.W)
                    p2score++;

                if (BoardHelper.canPlay(board, turn, i, j)) {
                    cells[i][j].highlight = 1;
                } else {
                    cells[i][j].highlight = 0;
                }
            }
        }

        score1.setText(player1.playerName() + " : " + p1score);
        score2.setText(player2.playerName() + " : " + p2score);
    }

    public void updateTotalScore() {
        tscore1.setText(player1.playerName() + " : " + totalscore1);
        tscore2.setText(player2.playerName() + " : " + totalscore2);
    }

    @Override
    public void handleClick(int i, int j) {
        if (awaitForClick && BoardHelper.canPlay(board, turn, i, j)) {
            System.out.println("User Played in : " + i + " , " + j);

            // update board
            board = BoardHelper.getNewBoardAfterMove(board, new Move(i, j), turn);

            // advance turn
            turn = (turn == Board.B) ? Board.W : Board.B;

            repaint();

            awaitForClick = false;

            // callback
            manageTurn();
        }
    }

    public void handleAI(GamePlayer ai) {
        Move aiPlayPoint = ai.play(new Board(board));
        int i = aiPlayPoint.getRow();
        int j = aiPlayPoint.getCol();
        System.out.println("i: " + i + ", j: " + j + "\n");
        if (!BoardHelper.canPlay(board, ai.myMark, i, j))
            System.err.println("FATAL : AI Invalid Move !");
        System.out.println(ai.playerName() + " Played in : " + i + " , " + j);

        // update board
        board = BoardHelper.getNewBoardAfterMove(board, aiPlayPoint, turn);

        // advance turn
        turn = (turn == Board.B) ? Board.W : Board.B;

        repaint();
    }
}
