package com.github.zipcodewilmington.CrapsTest;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.craps.CrapsGame;
import com.github.zipcodewilmington.casino.games.craps.CrapsPlayer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

public class CrapsTest {

  
    private static class FixedDiceCrapsGame extends CrapsGame {

        private final int[] diceSequence;
        private int index = 0;

        public FixedDiceCrapsGame(int... diceSequence) {
            super(new Scanner(""));
            this.diceSequence = diceSequence;
        }

        public FixedDiceCrapsGame(Scanner scanner, int... diceSequence) {
            super(scanner);
            this.diceSequence = diceSequence;
        }

        @Override
        protected int rollDice() {
            if (index < diceSequence.length) {
                return diceSequence[index++];
            }
            throw new IllegalStateException("No more dice in test sequence.");
        }
    }

    private CrapsPlayer testPlayer(double startingBalance) {
        CasinoAccount acct = new CasinoAccount("dev", "pass");
        acct.setAccountBalance(startingBalance);
        return new CrapsPlayer("TestPlayer", acct);
    }

 
    @Test
    public void testPassLineImmediateWin_Seven() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(7);

        double initial = player.getArcadeAccount().getAccountBalance();

        game.resolvePassLineBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testPassLineImmediateLoss_Two() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(2);

        double initial = player.getArcadeAccount().getAccountBalance();

        game.resolvePassLineBet(player, 100);

        Assertions.assertEquals(initial - 100, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testPassLinePointWin() {
        Scanner sc = new Scanner("n\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 6, 4, 6);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolvePassLineBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testPassLinePointSevenOut() {
        Scanner sc = new Scanner("n\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 5, 3, 7);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolvePassLineBet(player, 100);

        Assertions.assertEquals(initial - 100, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontPassImmediateWin_OnThree() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(3);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontPassBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontPassImmediateLose_OnSeven() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(7);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontPassBet(player, 100);

        Assertions.assertEquals(initial - 100, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontPassPointWin_OnSevenOut() {
        Scanner sc = new Scanner("n\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 8, 5, 7);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontPassBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontPassPointLose_WhenPointHit() {
        Scanner sc = new Scanner("n\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 8, 4, 8);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontPassBet(player, 100);

        Assertions.assertEquals(initial - 100, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testFieldBetDoubleOnTwo() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(2);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveFieldBet(player, 50);

        double expected = initial - 50 + 150; 
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testFieldBetTripleOnTwelve() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(12);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveFieldBet(player, 50);

        double expected = initial - 50 + 200; 
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testFieldBetLoseOnFive() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(5);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveFieldBet(player, 50);

        Assertions.assertEquals(initial - 50, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testComeBetImmediateWin_OnSeven() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(7);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveComeBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testComeBetPointHit() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(5, 3, 5);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveComeBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontComeBetImmediateWin_OnTwo() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(2);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontComeBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testDontComeBetSevenOutWin() {
        CrapsPlayer player = testPlayer(1000);
        CrapsGame game = new FixedDiceCrapsGame(6, 4, 7);

        double initial = player.getArcadeAccount().getAccountBalance();
        game.resolveDontComeBet(player, 100);

        double expected = initial - 100 + 200;
        Assertions.assertEquals(expected, player.getArcadeAccount().getAccountBalance(), 0.001);
    }



    @Test
    public void testPlaceBetSixWins() {
    
        Scanner sc = new Scanner("6\nn\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 6);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();

        game.resolvePlaceBet(player, 60);

        double expectedWin = 60 * (7.0 / 6.0);
        double expectedBalance = initial - 60 + 60 + expectedWin;

        Assertions.assertEquals(expectedBalance, player.getArcadeAccount().getAccountBalance(), 0.001);
    }

    @Test
    public void testPlaceBetLosesOnSeven() {
        Scanner sc = new Scanner("6\nn\n");

        CrapsGame game = new FixedDiceCrapsGame(sc, 7);
        CrapsPlayer player = testPlayer(1000);

        double initial = player.getArcadeAccount().getAccountBalance();

        game.resolvePlaceBet(player, 50);

        Assertions.assertEquals(initial - 50, player.getArcadeAccount().getAccountBalance(), 0.001);
    }
}
