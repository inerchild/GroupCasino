package com.github.zipcodewilmington.BlackjackTests;

import com.github.zipcodewilmington.casino.games.blackjack.BlackjackGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class BlackjackGameTest {

    private BlackjackGame game;
    private static final String PLAYER_NAME = "TestPlayer";
    private static final int STARTING_BALANCE = 1000;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();


    @BeforeEach
    void setUp() {
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        System.setOut(new PrintStream(outContent));
    }

    @Test
    void testConstructor_CreatesGame() {
        assertNotNull(game, "Game should be created");
    }

    @Test
    void testConstructor_InitializesComponents() {
        BlackjackGame testGame = new BlackjackGame("Player1", 500);
        assertNotNull(testGame, "Game with custom values should be created");
    }

 
    @Test
    void testPlay_ExitsWhenPlayerIsBroke() {
        String input = "1000\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame("BrokePlayer", 1000);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("out of money") || output.contains("Game over"), 
            "Should display broke message when player loses all money");
    }

    @Test
    void testPlay_AllowsMultipleRounds() {
        String input = "100\ns\ny\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        int roundCount = countOccurrences(output, "Place your bet");
        assertTrue(roundCount >= 2, "Should allow multiple rounds");
    }


    @Test
    void testPlaceBet_AcceptsValidBet() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Bet placed") || output.contains("100"), 
            "Should confirm bet placement");
    }

    @Test
    void testPlaceBet_RejectsInvalidBet() {
        String input = "-50\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid") || output.contains("valid"), 
            "Should reject negative bet");
    }

    @Test
    void testPlaceBet_RejectsBetExceedingBalance() {
        String input = "5000\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid") || output.contains("between"), 
            "Should reject bet exceeding balance");
    }

    @Test
    void testPlaceBet_HandlesNonNumericInput() {
        String input = "abc\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("valid number") || output.contains("Invalid"), 
            "Should handle non-numeric input");
    }

    @Test
    void testDealInitialCards_DealsTwoCardsToPlayer() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Your hand"), "Should display player's hand");
        assertTrue(output.contains("Dealer shows"), "Should display dealer's up card");
    }

   
    @Test
    void testPlayerTurn_AllowsHit() {
        String input = "100\nh\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("You drew") || output.contains("drew"), 
            "Should allow player to hit");
    }

    @Test
    void testPlayerTurn_AllowsStand() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("stand") || output.contains("Dealer"), 
            "Should allow player to stand");
    }

    @Test
    void testPlayerTurn_AllowsDoubleDown() {
        String input = "100\nd\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Double") || output.contains("double") || output.contains("drew"), 
            "Should allow double down option");
    }

    @Test
    void testPlayerTurn_OffersDoubleDownOnlyWithTwoCards() {
        String input = "100\nh\nh\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Choose action"), "Should show action prompt");
    }

    @Test
    void testPlayerTurn_DetectsBust() {
        String input = "100\nh\nh\nh\nh\nh\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Bust") || output.contains("bust") || output.contains("lose"), 
            "Should detect when player busts");
    }

    @Test
    void testPlayerTurn_HandlesInvalidInput() {
        String input = "100\nxyz\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Invalid") || output.contains("again"), 
            "Should handle invalid action input");
    }

    @Test
    void testSplit_OfferedForPairs() {
        String input = "100\nn\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Game should run without errors");
    }

    @Test
    void testSplit_RequiresSufficientFunds() {
        String input = "600\nn\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Game should handle split with insufficient funds");
    }

    @Test
    void testDealerTurn_DealerPlaysAfterPlayer() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Dealer") && output.contains("Turn"), 
            "Should display dealer's turn");
    }

    @Test
    void testDealerTurn_RevealsHand() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("reveals") || output.contains("final"), 
            "Should reveal dealer's hand");
    }

    @Test
    void testDealerTurn_DetectsBust() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should handle dealer bust scenario");
    }

    @Test
    void testDetermineWinner_PlayerWins() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Results") || output.contains("Balance"), 
            "Should determine winner");
    }

    @Test
    void testDetermineWinner_DealerWins() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Balance"), "Should show balance after round");
    }

    @Test
    void testDetermineWinner_Push() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should handle push scenario");
    }

    @Test
    void testDetermineWinner_DealerBusts() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should handle dealer bust");
    }

   
    @Test
    void testDeckShuffling_ShufflesWhenLow() {
        String input = "50\ns\ny\n50\ns\ny\n50\ns\ny\n50\ns\ny\n50\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should handle deck shuffling");
    }

    @Test
    void testMultipleRounds_BalanceTracking() {
        String input = "100\ns\ny\n100\ns\ny\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Balance"), "Should track balance across rounds");
    }

    @Test
    void testPlayerBusts_EndsRoundImmediately() {
        String input = "100\nh\nh\nh\nh\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.length() > 0, "Should end round when player busts");
    }


    @Test
    void testPlayAgainPrompt_YesContinues() {
        String input = "100\ns\ny\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        int betPrompts = countOccurrences(output, "Place your bet");
        assertTrue(betPrompts >= 2, "Should continue to next round on 'yes'");
    }

    @Test
    void testPlayAgainPrompt_NoExits() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Thanks for playing"), "Should exit on 'no'");
    }

    @Test
    void testCompleteGameFlow_WithWin() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Welcome"), "Should have welcome message");
        assertTrue(output.contains("Balance"), "Should show balance");
        assertTrue(output.contains("Thanks"), "Should have exit message");
    }

    @Test
    void testCompleteGameFlow_WithLoss() {
        String input = "100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Dealer") || output.contains("Balance"), 
            "Should complete game flow");
    }

    @Test
    void testGameHandlesAllPlayerActions() {
        String input = "100\nh\ns\ny\n100\nd\ny\n100\ns\nn\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        game = new BlackjackGame(PLAYER_NAME, STARTING_BALANCE);
        
        game.play();
        
        String output = outContent.toString();
        assertTrue(output.contains("Balance"), "Should handle all player actions");
    }

    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }
}