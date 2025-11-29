package com.github.zipcodewilmington.casino;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by leon on 7/21/2020.
 * `ArcadeAccountManager` stores, manages, and retrieves `ArcadeAccount` objects
 * it is advised that every instruction in this class is logged
 */
public class CasinoAccountManager {

    private ArrayList<CasinoAccount> casinoAccounts = new ArrayList<>();
    /**
     * @param accountName     name of account to be returned
     * @param accountPassword password of account to be returned
     * @return `ArcadeAccount` with specified `accountName` and `accountPassword`
     */ 
    public CasinoAccount getAccount(String accountName, String accountPassword) {
        for (CasinoAccount account : casinoAccounts) {
            if (account.getAccountName().equals(accountName) && account.getAccountPassword().equals(accountPassword)) {
                return account;
            }
        }
        return null;
    }

    /**
     * logs & creates a new `ArcadeAccount`
     *
     * @param accountName     name of account to be created
     * @param accountPassword password of account to be created
     * @return new instance of `ArcadeAccount` with specified `accountName` and `accountPassword`
     */
    public CasinoAccount createAccount(String accountName, String accountPassword) {
        return new CasinoAccount(accountName, accountPassword);
    }

    /**
     * logs & registers a new `ArcadeAccount` to `this.getArcadeAccountList()`
     *
     * @param casinoAccount the arcadeAccount to be added to `this.getArcadeAccountList()`
     */
    public void registerAccount(CasinoAccount casinoAccount) {
        casinoAccounts.add(casinoAccount);
    }

    public void loadAccounts(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(filename);
        try {
            List<CasinoAccount> accountsFromFile = mapper.readValue(
                    jsonFile,
                    new TypeReference<List<CasinoAccount>>() {}
            );

            casinoAccounts.addAll(accountsFromFile);
        } catch (IOException e) {
            System.err.println("Failed to load accounts from JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveAccounts(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(filename);
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, casinoAccounts);
        } catch (IOException e) {
            System.err.println("Failed to save accounts to JSON file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
