package levelBuild;

import core.ImageImport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Background implements ImageImport {

    protected double x;
    protected double moveSpeed;
    BufferedImage img;

    public Background(double x,double moveSpeed) {
        this.x = x;
        this.moveSpeed = moveSpeed;
        importImg();
    }

    @Override
    public void importImg() {
        try {
            img = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/background.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img,(int)x,0,null);
    }

    public void move(){ //Method for moving background
        x -= getMoveSpeed();

        if(x < -1280){ //if half of the background is outside the window, the background resets
            x = 0;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
