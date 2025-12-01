package com.github.zipcodewilmington.NumberGuessTests;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.games.numberguess.NumberGuessGame;
import com.github.zipcodewilmington.casino.games.numberguess.NumberGuessPlayer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NumberGuessTests {

    @Test

    public void testAddPlayerIncreasePlayerCount() {
        //Given - a new game with no players
        NumberGuessGame game = new NumberGuessGame();
        CasinoAccount account= new CasinoAccount("Alice", "pass123");
        NumberGuessPlayer player = new NumberGuessPlayer(account);

        // When  - we add a player
        game.add(player);

        //Then - the game should have that player
        assertNotNull(player.getArcadeAccount());
        assertEquals("Alice", player.getArcadeAccount().getAccountName());
    } //first test ends here



     @Test
    public void testIsGuessCorrect() {
        // Given - a game with secret number 42
        NumberGuessGame game = new NumberGuessGame(42);
        
        // When/Then - check if guess is correct
        assertTrue( game.isCorrectGuess(42));  // 42 should be wrong
        assertFalse(game.isCorrectGuess(50));  // 50 should be correct

        
    }  

    @Test 
     public void testEasyDifficultyRange() {
        //Given - a game with Easy difficulty
        NumberGuessGame game = new NumberGuessGame("EASY");

            //When/Then - max range should be 50
            assertEquals(50, game.getMaxRange());
            assertEquals(10, game.getMaxAttempts());

    } 
    
    @Test
    public void testMediumDifficultyRange() {
        //Given a game with medium difficulty
        NumberGuessGame game = new NumberGuessGame("MEDIUM");


        //When/Then - max range should be 100, 7 attempts
        assertEquals(100, game.getMaxRange()); 
        assertEquals(7, game.getMaxAttempts());
        
    }

    @Test
    public void testHardDifficultRange() {
        //Given a game with hard difficulty
        NumberGuessGame game = new NumberGuessGame("HARD");

        //When/Then - max range should be 1000, 10 attempts
        assertEquals(1000, game.getMaxRange()); 
        assertEquals(10, game.getMaxAttempts());
    }

    @Test
    public void testGetHint() {
        //Given - a game with secret number 42
        NumberGuessGame game = new NumberGuessGame(42);

        //When/Then - hints should guide toward correct answer
        assertEquals("HIGHER", game.getHint(30)); // 30 is too low
        assertEquals("LOWER", game.getHint(50)); //50 is too high
        assertEquals("CORRECT", game.getHint(42)); // is correct
    }

    @Test
    public void testTrackAttempts () {
        // Given - a game with 3 max attempts
        NumberGuessGame game =new NumberGuessGame("MEDIUM"); // 7 Attempts

        //When/Then - should track attempts
        assertEquals(0, game.getCurrentAttempts()); //starts at 0

    }

    @Test
    public void testIncrementAttempts() {
        //Given - a game that starts a 0 attempts
        NumberGuessGame game = new NumberGuessGame ("EASY");

        //When - we make a guess
        game.makeGuess(25);

        //Then - attempts should increase to 1
        assertEquals(1, game.getCurrentAttempts());
    }

}

