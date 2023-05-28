package core;

import levelBuild.*;
import player.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;

public class Game implements Runnable{

    Random rn = new Random();
    private Window gameWindow;
    private Panel gamePanel;
    private Thread gameLoop;
    GameState gameState = GameState.START;

    private final int FPS = 60;
    private final int TPS = 200;
    private BufferedImage start;
    private BufferedImage gameOver;
    private Font font;

    private Player player;
    private Background bg;
    private Floor fl;
    private Pipe topPipes[];
    private Pipe botPipes[];
    private int currentScore;
    private int highScore;

    public Game(){
        bg = new Background(0,0.5); //Creates background object
        topPipes = new Pipe[5]; //New array of top pipes objects
        botPipes = new Pipe[5]; //New array of bottom pipes objects

        for(int i = 0; i < 5; i++){ //Adding pipe objects to arrays
            topPipes[i] = new TopPipe(1300+(i*500),0,1300+(i*500),1,100,400);
            botPipes[i] = new BottomPipe(1300+(i*500),0,1300+(i*500),1,100,400);
        }

        fl = new Floor(0, 683,1); //Creates floor object
        player = new Player(300,300,300,74,44,0.5,false,4.5); //Creates player object

        gamePanel = new Panel(this);
        gameWindow = new Window(gamePanel);
        gamePanel.requestFocus();

        startGameLoop(); //game loop starts
    }

    private void startGameLoop(){
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() { //method for game loop thread
        double timePerFrame = 1000000000.0 / FPS;
        double timePerTick = 1000000000.0 / TPS;

        long previousTime = System.nanoTime();

        int frames = 0;
        int ticks = 0;

        double tickDiff = 0;
        double frameDiff = 0;

        while(true){
            long currentTime = System.nanoTime();

            tickDiff += (currentTime - previousTime) / timePerTick;
            frameDiff += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;

            if(tickDiff >= 1){ //TPS
                tick();
                ticks++;
                tickDiff--;
            }

            if(frameDiff >= 1){ //FPS
                gamePanel.repaint();
                frames++;
                frameDiff--;
            }
        }
    }

    public void tick(){ //method for one tick of the game
        switch (gameState){
            case START: //tick while start screen
                bg.move(); //background moving
                fl.move(); //floor moving
                break;
            case PLAYING: //tick while playing
                player.fall(); //player falls and jumps
                bg.move();
                fl.move();
                for (Pipe topPipe : topPipes) { //each top pipe moves
                    topPipe.move();
                    if(topPipe.getPipeHitbox().intersects(player.getHitbox())){ //if player collides with pipe then game over
                        gameState = GameState.GAME_OVER;
                    }
                    if(topPipe.getX() == 200){  //if player gets through a pipe, player gets a point
                        currentScore += 1;
                        if(currentScore > highScore){
                            highScore = currentScore;
                        }
                    }
                }
                for (Pipe botPipe : botPipes) { //each bottom pipe moves
                    botPipe.move();
                    if(botPipe.getPipeHitbox().intersects(player.getHitbox())){ //if player collides with pipe then game over
                        gameState = GameState.GAME_OVER;
                    }
                }
                if(player.getHitbox().intersects(fl.getFlHitbox())){ //if player collides with floor then game over
                    gameState = GameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                break;
        }
    }

    public void render(Graphics g) throws IOException, FontFormatException { //method for rendering objects of the game
        bg.render(g); //rendering background
        player.render(g); //rendering player
        for (Pipe topPipe : topPipes) { //rendering top pipes from array
            topPipe.render(g);
        }
        for (Pipe botPipe : botPipes) { //rendering bottom pipes from array
            botPipe.render(g);
        }
        fl.render(g); //rendering floor
        switch (gameState) {
            case PLAYING: //render when playing
                font = new Font("Agency FB",Font.BOLD,40);
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString(String.valueOf(currentScore), 640, 50); //shows current score
                break;
            case GAME_OVER: //render game over screen
                try {
                    gameOver = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/gameover_screen.png")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(gameOver,0,0,null); //render game over screen

                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("HIGHSCORE: " + String.valueOf(highScore), 540, 300); //render high score title

                currentScore = 0; //current score resets back to zero
                break;
            case START: //render start screen
                try {
                    start = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/start_screen.png")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(start,0,0,null); //render start screen

                player.setY(player.getStartY()); //resets player back to starting position
                for (Pipe topPipe : topPipes) { //resets top pipes back to starting position
                    topPipe.setX(topPipe.getStartX());
                    topPipe.setY(-(50 + rn.nextInt(100))); //random gap between pipes
                }
                for (Pipe botPipe : botPipes) { //resets bottom pipes back to starting position
                    botPipe.setX(botPipe.getStartX());
                    botPipe.setY(400+(50 + rn.nextInt(100))); //random gap between pipes
                }
        }
    }

    public Player getPlayer() {
        return player;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}