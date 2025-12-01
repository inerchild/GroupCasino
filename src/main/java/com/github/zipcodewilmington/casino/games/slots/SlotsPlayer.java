package com.github.zipcodewilmington.casino.games.slots;

import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.PlayerInterface;

//Represents a player playing the Slots game
public class SlotsPlayer  implements PlayerInterface{
    private CasinoAccount account;
    private SlotMachine slotMachine;

    public SlotsPlayer(CasinoAccount account, SlotMachine slotMachine) {
        this.account = account;
        this.slotMachine = slotMachine;
    }

    @Override
    public CasinoAccount getArcadeAccount() {
        return account;
    }

    @Override
    public <SomeReturnType> SomeReturnType play() {
        //This will be called by the SlotsGame.run() method
        //The actual game loop is handled in SlotsGame
        return null;
    }

    //Gets the slot machine this player is using
    public SlotMachine getSlotMachine() {
        return slotMachine;
    }

    //Sets the slot machine for this player
    public void setSlotMachine(SlotMachine slotMachine) {
        this.slotMachine = slotMachine;
    }
} 