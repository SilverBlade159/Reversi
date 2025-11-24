package player.ai;

import game.GamePlayer;

import java.awt.*;

public class HumanPlayer extends GamePlayer {

    public HumanPlayer(int mark) {
        super(mark);
    }

    @Override
    public boolean isUserPlayer() {
        return true;
    }

    @Override
    public String playerName() {
        return "User";
    }

    @Override
    public Move play(Board board) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }

}
