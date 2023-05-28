package core;

import levelBuild.Pipe;
import levelBuild.TopPipe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    public void testGameInitialization() {

        Game game = new Game();

        Assertions.assertEquals(GameState.START, game.getGameState());

    }
}