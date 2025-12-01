package com.github.zipcodewilmington.casino.games.numberguess;

import java.util.Scanner;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;

/**
 * Created by leon on 7/21/2020.
 */
public class NumberGuessPlayer implements PlayerInterface {

    private CasinoAccount account;

    public NumberGuessPlayer(CasinoAccount account) {
        this.account = account;
    }

    @Override
    public CasinoAccount getArcadeAccount() {
        return account;
    }

    @Override
    public Integer play() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your guess (1-100): ");
        return scanner.nextInt();
    }

}