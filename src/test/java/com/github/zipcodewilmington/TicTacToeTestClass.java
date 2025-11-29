
package com.github.zipcodewilmington;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.zipcodewilmington.casino.CasinoAccount;

import java.util.Random; 

class TicTacToeTest {
    public static void main(String[] args) {
        CasinoAccount humanAccount = new CasinoAccount("Player", "password123");
        CasinoAccount computerAccount = new CasinoAccount("Computer", "cpu");
        
        HumanPlayer humanPlayer = new HumanPlayer(humanAccount);
        ComputerPlayer computerPlayer = new ComputerPlayer(computerAccount);
        
        TicTacToeGame game = new TicTacToeGame();
        game.add(humanPlayer);
        game.add(computerPlayer);
        
        game.run();
    }
}

