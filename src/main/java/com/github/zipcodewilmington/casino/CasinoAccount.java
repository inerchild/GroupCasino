package com.github.zipcodewilmington.casino;

/**
 * Created by leon on 7/21/2020.
 * `ArcadeAccount` is registered for each user of the `Arcade`.
 * The `ArcadeAccount` is used to log into the system to select a `Game` to play.
 */
public class CasinoAccount {
    private String accountName;
    private String accountPassword;
    private Double accountBalance;

    public CasinoAccount(String accountName, String accountPassword) {
        this.accountName = accountName;
        this.accountPassword = accountPassword;
        this.accountBalance = 1000.0;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void debitAccount(Double amount) {
        this.accountBalance -= amount;
    }

    public void creditAccount(Double amount) {
        this.accountBalance += amount;
    }
}
