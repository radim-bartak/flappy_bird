package player;

import core.ImageImport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Player implements ImageImport {

    private double x;
    private double y;
    private int startY;
    private int width;
    private int height;
    private double fallSpeed;
    private double currentFallSpeed = fallSpeed;
    private boolean jumping;
    private double jumpSpeed;
    private double currentJumpSpeed = jumpSpeed;
    private Rectangle hitbox;

    private BufferedImage img;

    public Player(double x, double y, int startY, int width, int height, double fallSpeed, boolean jumping, double jumpSpeed) {
        this.x = x;
        this.y = y;
        this.startY = startY;
        this.width = width;
        this.height = height;
        this.fallSpeed = fallSpeed;
        this.jumping = jumping;
        this.jumpSpeed = jumpSpeed;

        importImg();
        hitbox = new Rectangle((int) x,(int) y,width,height); //creates hitbox for collision
    }

    public void fall(){ //method for fall of the player
        if(jumping){ //if player jumps:
            y -= currentJumpSpeed;

            currentJumpSpeed -= 0.1; //jumping gets progressively slower until it stops at zero

            if(y <= 0) {
                jumping = false; //if player hits the roof, then he can't jump anymore
            }
            if(currentJumpSpeed <= 0){ //jump stops
                currentJumpSpeed = jumpSpeed;
                currentFallSpeed = fallSpeed;
                jumping = false;
            }
        }else{ //player falls:
            y += currentFallSpeed;
            if(currentFallSpeed < 2){
                currentFallSpeed += 0.015; //falling gets progressively faster until the speed of two
            }
        }
        hitbox.y = (int) y; //hitbox moves with player
    }

    @Override
    public void render(Graphics g){
        g.drawImage(img,(int) x,(int) y, width, height,null);
    }

    @Override
    public void importImg(){
        try {
            img = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/bird.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public double getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(double jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public double getCurrentJumpSpeed() {
        return currentJumpSpeed;
    }

    public void setCurrentJumpSpeed(double currentJumpSpeed) {
        this.currentJumpSpeed = currentJumpSpeed;
    }

    public double getFallSpeed() {
        return fallSpeed;
    }

    public void setFallSpeed(double fallSpeed) {
        this.fallSpeed = fallSpeed;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
