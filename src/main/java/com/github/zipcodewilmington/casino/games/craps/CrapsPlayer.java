package com.github.zipcodewilmington.casino.games.craps;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;

public class CrapsPlayer implements PlayerInterface{

    private final String name;
    private final CasinoAccount casinoAccount;
    
    public CrapsPlayer(String name, CasinoAccount casinoAccount) {
        this.name = name;
        this.casinoAccount = casinoAccount;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public CasinoAccount getArcadeAccount() {
        return casinoAccount;
    }
   
    @Override
    public <SomeReturnType> SomeReturnType play() {
        return null;
}
}
