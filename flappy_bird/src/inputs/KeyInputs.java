package inputs;

import core.GameState;
import core.Panel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyInputs implements KeyListener {

    private Panel gamePanel;

    public KeyInputs(Panel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (gamePanel.getGame().getGameState()){
            case START:
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    gamePanel.getGame().setGameState(GameState.PLAYING);
                }
                break;
            case PLAYING:
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    gamePanel.getGame().getPlayer().setJumping(true);
                }
                break;
            case GAME_OVER:
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    gamePanel.getGame().setGameState(GameState.START);
                }
                break;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
