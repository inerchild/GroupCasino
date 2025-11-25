package com.github.zipcodewilmington.casino.games.war;

import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.utils.AnsiColor;
import com.github.zipcodewilmington.utils.IOConsole;

public class WarPlayer implements PlayerInterface{
    CasinoAccount arcadeAccount;
    private final IOConsole console = new IOConsole(AnsiColor.RED);

    public WarPlayer(CasinoAccount arcadeAccount) {
        this.arcadeAccount = arcadeAccount;
    }

    public CasinoAccount getArcadeAccount() {
        return this.arcadeAccount;
    }

    @Override
    public Double play() {
        double betAmount = 0;
        String input = console.getStringInput(new StringBuilder()
                        .append("Would you like to bet?")
                        .append("\nEnter your bet amount or nothing to play without gamlbing:")
                        .toString());
        if (input.equals("")) {
            console.println("You have chosen to play without gambling.");
        } else if (isDouble(input)) {
            betAmount = Double.parseDouble(input);
            console.println("You have chosen to bet $" + betAmount);
        } else {
            console.println("Invalid input. You will play without gambling.");
        }
        return betAmount;
    }

    public boolean tie() {
        String input = "";
        while (!input.equalsIgnoreCase("Y") && !input.equalsIgnoreCase("N")){
            input = console.getStringInput(new StringBuilder()
                    .append("It's a tie! Would you like to try your odds?")
                    .append("\nIf the next round is a win, you win double your bet amount.")
                    .append("\nIf the next round is a loss, you lose double your bet amount.")
                    .append("\nEnter Y to accept or N to decline:")
                    .toString());

        }
        if (input.equalsIgnoreCase("N")) {
            console.println("You have chosen not to try your odds.");
            return false;
        }
        console.println("You have chosen to try your odds.");
        return true;
    }
    
    public static boolean isDouble(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
