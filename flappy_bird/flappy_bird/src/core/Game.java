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
        bg = new Background(0,0.5);
        topPipes = new Pipe[5]; //New array of top pipes objects
        botPipes = new Pipe[5]; //New array of bottom pipes objects

        for(int i = 0; i < 5; i++){
            topPipes[i] = new TopPipe(1300+(i*500),0,1300+(i*500),1,100,400);
            botPipes[i] = new BottomPipe(1300+(i*500),0,1300+(i*500),1,100,400);
        }

        fl = new Floor(0, 683,1);
        player = new Player(300,300,300,74,44,0.5,false,4.5);

        gamePanel = new Panel(this);
        gameWindow = new Window(gamePanel);
        gamePanel.requestFocus();

        startGameLoop();
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

            if(tickDiff >= 1){
                tick();
                ticks++;
                tickDiff--;
            }

            if(frameDiff >= 1){
                gamePanel.repaint();
                frames++;
                frameDiff--;
            }
        }
    }

    public void tick(){
        switch (gameState){
            case START:
                bg.move();
                fl.move();
                break;
            case PLAYING:
                player.fall();
                bg.move();
                fl.move();
                for (Pipe topPipe : topPipes) {
                    topPipe.move();
                    if(topPipe.getPipeHitbox().intersects(player.getHitbox())){
                        gameState = GameState.GAME_OVER;
                    }
                    if(topPipe.getX() == 200){
                        currentScore += 1;
                        if(currentScore > highScore){
                            highScore = currentScore;
                        }
                    }
                }
                for (Pipe botPipe : botPipes) {
                    botPipe.move();
                    if(botPipe.getPipeHitbox().intersects(player.getHitbox())){
                        gameState = GameState.GAME_OVER;
                    }
                }
                if(player.getHitbox().intersects(fl.getFlHitbox())){
                    gameState = GameState.GAME_OVER;
                }
                break;
            case GAME_OVER:
                break;
        }
    }

    public void render(Graphics g) throws IOException, FontFormatException {
        bg.render(g);
        player.render(g);
        for (Pipe topPipe : topPipes) {
            topPipe.render(g);
        }
        for (Pipe botPipe : botPipes) {
            botPipe.render(g);
        }
        fl.render(g);
        switch (gameState) {
            case PLAYING:
                font = new Font("Agency FB",Font.BOLD,40);
                g.setFont(font);
                g.setColor(Color.white);
                g.drawString(String.valueOf(currentScore), 640, 50);
                break;
            case GAME_OVER:
                try {
                    gameOver = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/gameover_screen.png")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(gameOver,0,0,null);

                g.setFont(font);
                g.setColor(Color.white);
                g.drawString("HIGHSCORE: " + String.valueOf(highScore), 540, 300);

                currentScore = 0;
                break;
            case START:
                try {
                    start = ImageIO.read(Objects.requireNonNull(this.getClass().getResource("/start_screen.png")));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                g.drawImage(start,0,0,null);

                player.setY(player.getStartY());
                for (Pipe topPipe : topPipes) {
                    topPipe.setX(topPipe.getStartX());
                    topPipe.setY(-(140 + rn.nextInt(60)));
                }
                for (Pipe botPipe : botPipes) {
                    botPipe.setX(botPipe.getStartX());
                    botPipe.setY(400+(40 - rn.nextInt(60)));
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