package levelBuild;

import core.ImageImport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Floor extends Background implements ImageImport {

    private int y;
    BufferedImage img;
    Rectangle flHitbox;

    public Floor(double x, int y, double moveSpeed) {
        super(x, moveSpeed);
        this.y = y;
        flHitbox = new Rectangle((int) x,y,2560,(800-y));

    }

    @Override
    public void importImg() {
        try {
            img = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/floor.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(img,(int) x,y,null);
    }

    public void move(){
        x -= getMoveSpeed();

        if(x < -1280){
            x = 0;
        }
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rectangle getFlHitbox() {
        return flHitbox;
    }

    public void setFlHitbox(Rectangle flHitbox) {
        this.flHitbox = flHitbox;
    }
}
