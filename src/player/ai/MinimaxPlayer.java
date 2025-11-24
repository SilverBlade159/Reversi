package player.ai;

import java.awt.Point;
import java.io.Console;
import java.util.ArrayList;

import game.GamePlayer;

public class MinimaxPlayer extends GamePlayer {
    private int maxDepth;

    public MinimaxPlayer(int mark) {
        super(mark);
        this.maxDepth = 5;
    }

    public MinimaxPlayer(int mark, int maxDepth) {
        super(mark);
        this.maxDepth = maxDepth;
    }

    public Move MiniMax(Board board) {
        // opponent mark
        // int oplayer = (myMark == Board.B) ? Board.W : Board.B;

        return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

        // if (myMark == Board.W) {
        // // If the White plays then it wants to maximize the heuristics value
        // return max(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        // } else {
        // // If the Black plays then it wants to minimize the heuristics value
        // return min(new Board(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        // }
    }

    // The max and min functions are called one after another until a max depth is
    // reached or a terminal state.
    // We create a tree using backtracking DFS.
    Move max(Board board, int depth, int alpha, int beta) {
        /*
         * If MAX is called on a state that is terminal or after a maximum depth is
         * reached,
         * then a heuristic is calculated on the state and the move returned.
         */
        if (board.isTerminal() || (depth == this.maxDepth)) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }
        // The children-moves of the state are calculated
        ArrayList<Board> children = board.getChildren(myMark);
        Move maxMove = new Move(Integer.MIN_VALUE); // put max node initially to smallest value.
        for (Board child : children) {
            // And for each child min is called, on a lower depth
            Move move = min(child, depth + 1, alpha, beta);

            // The child-move with the greatest value is selected and returned by max
            if (move.getValue() >= maxMove.getValue()) {
                maxMove = child.getLastMove();
            }

            // alpha beta pruning
            alpha = Math.max(alpha, maxMove.getValue());
            if (beta <= alpha)
                break;
        }
        return maxMove;
    }

    // Min works similarly to max
    Move min(Board board, int depth, int alpha, int beta) {
        if (board.isTerminal() || (depth == this.maxDepth)) {
            return new Move(board.getLastMove().getRow(), board.getLastMove().getCol(), board.evaluate());
        }

        // we want to generate the states for the opponent who has a different mark than
        // ours
        ArrayList<Board> children = board.getChildren(oPlayer);

        Move minMove = new Move(Integer.MAX_VALUE);
        for (Board child : children) {
            Move move = max(child, depth + 1, alpha, beta);

            if (move.getValue() <= minMove.getValue()) {
                minMove = child.getLastMove();
            }

            // alpha beta pruning
            beta = Math.min(beta, minMove.getValue());
            if (beta <= alpha)
                break;
        }
        return minMove;
    }

    @Override
    public boolean isUserPlayer() {
        return false;
    }

    @Override
    public String playerName() {
        return "MiniMax Player";
    }

    @Override
    public Move play(Board board) {
        board.print();
        return MiniMax(board);
    }
}