package com.github.zipcodewilmington;

import java.util.Arrays;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.CasinoAccountManager;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.games.numberguess.NumberGuessGame;
import com.github.zipcodewilmington.casino.games.numberguess.NumberGuessPlayer;
import com.github.zipcodewilmington.casino.games.tictactoe.TicTacToeGame;
import com.github.zipcodewilmington.casino.games.tictactoe.HumanPlayer;
import com.github.zipcodewilmington.casino.games.slots.SlotsGame;
import com.github.zipcodewilmington.casino.games.slots.SlotsPlayer;
import com.github.zipcodewilmington.casino.games.craps.CrapsGame;
import com.github.zipcodewilmington.casino.games.craps.CrapsPlayer;
import com.github.zipcodewilmington.casino.games.war.WarGame;
import com.github.zipcodewilmington.casino.games.war.WarPlayer;
import com.github.zipcodewilmington.casino.games.Poker.CardRenderer;
import com.github.zipcodewilmington.casino.games.Poker.PokerGame;
import com.github.zipcodewilmington.casino.games.Poker.PokerPlayer;
import com.github.zipcodewilmington.utils.AnsiColor;
import com.github.zipcodewilmington.utils.Card;
import com.github.zipcodewilmington.utils.IOConsole;
import com.github.zipcodewilmington.utils.Rank;
import com.github.zipcodewilmington.utils.Suit;


/**
 * Created by leon on 7/21/2020.
 */
public class Casino implements Runnable {
    private final IOConsole console = new IOConsole(AnsiColor.BLUE);

    @Override
    public void run() {
        String arcadeDashBoardInput;
        CasinoAccountManager casinoAccountManager = new CasinoAccountManager();
        CasinoAccount currentAccount = null;
        String accountName = "";
        String accountPassword= "";
        casinoAccountManager.loadAccounts("accounts.json");
        do {
            if (currentAccount == null) {
                arcadeDashBoardInput = getLoginDashboardInput();
                if ("login".equals(arcadeDashBoardInput)) {
                    accountName = console.getStringInput("Enter your account name:");
                    accountPassword = console.getStringInput("Enter your account password:");
                    currentAccount = casinoAccountManager.getAccount(accountName, accountPassword);
                } else if ("create-account".equals(arcadeDashBoardInput)) {
                    console.println("Welcome to the account-creation screen.");
                    accountName = console.getStringInput("Enter your account name:");
                    accountPassword = console.getStringInput("Enter your account password:");
                    CasinoAccount newAccount = casinoAccountManager.createAccount(accountName, accountPassword);
                    casinoAccountManager.registerAccount(newAccount);
                } else {
                    console.println("Invalid Command.");
                }
            } else {
                arcadeDashBoardInput = getArcadeDashboardInput();
                if ("select-game".equals(arcadeDashBoardInput)) {
                    boolean isValidLogin = currentAccount != null;
                    if (isValidLogin) {
                        String gameSelectionInput = getGameSelectionInput().toUpperCase();
                        if (gameSelectionInput.equals("SLOTS")) {
                            SlotsGame slotsGame = new SlotsGame();
                            SlotsPlayer slotsPlayer = new SlotsPlayer(currentAccount, slotsGame.getSlotMachine());
                            play(slotsGame, slotsPlayer);
                        } else if (gameSelectionInput.equals("NUMBERGUESS")) {
                            play(new NumberGuessGame(), new NumberGuessPlayer());
                        } else if (gameSelectionInput.equals("CRAPS")) {
                            CrapsGame crapsGame = new CrapsGame();
                            CrapsPlayer crapsPlayer = new CrapsPlayer(accountName, currentAccount);
                            play(crapsGame, crapsPlayer);
                        } else if (gameSelectionInput.equals("WAR")) {
                            play(new WarGame(), new WarPlayer(currentAccount));
                        } else if (gameSelectionInput.equals("TICTACTOE")) {
                            play(new TicTacToeGame(), new HumanPlayer(currentAccount));
                        } else if (gameSelectionInput.equals("POKER")) {
                            play(new PokerGame(), new PokerPlayer(currentAccount));
                        } else {
                            String errorMessage = "[ %s ] is an invalid game selection";
                            console.println(String.format(errorMessage, gameSelectionInput));
                        }
                    }
                } else if ("logout".equals(arcadeDashBoardInput)) {
                    currentAccount = null;
                } else if ("deposit".equals(arcadeDashBoardInput)) {
                    double deposit = getMoneyAmount();
                    depositAccount(currentAccount, deposit);
                    console.println(currentAccount.getAccountName() + " now has " + currentAccount.getAccountBalance() + " credits");
                } else if ("withdraw".equals(arcadeDashBoardInput)) {
                    double withdraw = getMoneyAmount();
                    withdrawAccount(currentAccount, withdraw);
                    console.println(currentAccount.getAccountName() + " now has " + currentAccount.getAccountBalance() + " credits");
                } else {
                    console.println("Invalid Command.");
                }
            }
            casinoAccountManager.saveAccounts("accounts.json");
        } while (!"exit".equals(arcadeDashBoardInput));
    }

    private String getLoginDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Arcade Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ create-account ], [login]")
                .toString());
    }

    private String getArcadeDashboardInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Arcade Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[select-game], [deposit], [withdraw], [logout]")
                .toString());
    }

    private String getGameSelectionInput() {
        return console.getStringInput(new StringBuilder()
                .append("Welcome to the Game Selection Dashboard!")
                .append("\nFrom here, you can select any of the following options:")
                .append("\n\t[ SLOTS ], [ NUMBERGUESS ], [ WAR ], [ CRAPS ], [TICTACTOE], [ POKER ]")
                .toString());
    }

    private void play(Object gameObject, Object playerObject) {
        GameInterface game = (GameInterface)gameObject;
        PlayerInterface player = (PlayerInterface)playerObject;
        game.add(player);
        game.run();
    }

    private double getMoneyAmount() {
        return Double.valueOf(console.getStringInput(new StringBuilder()
                .append("How many credits?")
                .toString()));
    }

    private void depositAccount (CasinoAccount casinoAccount, double amount) {
        casinoAccount.creditAccount(amount);
    }

    private void withdrawAccount (CasinoAccount casinoAccount, double amount) {
        casinoAccount.debitAccount(amount);
    }
}
