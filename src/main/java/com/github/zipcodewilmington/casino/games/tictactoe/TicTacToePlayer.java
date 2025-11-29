

package com.github.zipcodewilmington.casino.games.tictactoe;

import java.util.Scanner;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface; 

/**
 * Created by leon on 7/21/2020.
 * All players of a game should abide by `PlayerInterface`.
 * All players must have reference to the `ArcadeAccount` used to log into the `Arcade` system.
 * All players are capable of `play`ing a game.
 */
abstract class TicTacToePlayer implements PlayerInterface {
    protected CasinoAccount account;
    protected char symbol;
    
    public TicTacToePlayer(CasinoAccount account) {
        this.account = account;
    }
    
    @Override
    public CasinoAccount getArcadeAccount() {
        return account;
    }
    
    @Override
    public <SomeReturnType> SomeReturnType play() {
        return null;
    }
    
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    
    public char getSymbol() {
        return symbol;
    }
    
    public abstract int[] makeMove(Board board);
}