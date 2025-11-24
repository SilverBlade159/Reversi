package game;

import java.awt.*;

import player.ai.*;

public abstract class GamePlayer {

    protected int myMark, oPlayer;

    public GamePlayer(int mark) {
        myMark = mark;
        oPlayer = (myMark == Board.B) ? Board.W : Board.B;
    }

    abstract public boolean isUserPlayer();

    abstract public String playerName();

    // abstract public Point play(int[][] board);

    abstract public Move play(Board board);
}
