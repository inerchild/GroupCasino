
package com.github.zipcodewilmington;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.games.tictactoe.HumanPlayer;
import com.github.zipcodewilmington.casino.games.tictactoe.ComputerPlayer;
import com.github.zipcodewilmington.casino.games.tictactoe.TicTacToeGame;


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

