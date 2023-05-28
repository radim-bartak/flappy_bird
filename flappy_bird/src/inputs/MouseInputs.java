package inputs;

import core.Panel;
import core.GameState;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private Panel gamePanel;

    public MouseInputs(Panel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (gamePanel.getGame().getGameState()){
            case START:
                gamePanel.getGame().setGameState(GameState.PLAYING);
                break;
            case PLAYING:
                gamePanel.getGame().getPlayer().setJumping(true);
                break;
            case GAME_OVER:
                gamePanel.getGame().setGameState(GameState.START);
                break;
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
       // System.out.println("released");
        // gamePanel.getGame().getPlayer().setFallSpeed(2);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
