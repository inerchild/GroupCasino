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
