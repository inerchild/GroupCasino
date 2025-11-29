package com.github.zipcodewilmington.casino.games.slots;

import com.github.zipcodewilmington.casino.CasinoAccount;

public class SlotsGameRunner {
    public static void main(String[] args) {
        // Create the game
        SlotsGame game = new SlotsGame();
        
        // Create a player with account
        CasinoAccount account = new CasinoAccount("TestPlayer", "password");
        account.setAccountBalance(500.0);
        
        SlotsPlayer player = new SlotsPlayer(account, game.getSlotMachine());
        
        // Add player to game
        game.add(player);
        
        // Run the game!
        game.run();
    }
}