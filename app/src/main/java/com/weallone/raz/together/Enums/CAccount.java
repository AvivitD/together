package com.weallone.raz.together.Enums;

/**
 * Enum Class for represnting the Account-entity enum.
 */
public class CAccount {
    Account acount;

    /**
     * Construcot
     * @param val - init desired
     */
    public CAccount(String val) {
        switch(val){
            case "Child": acount = Account.CHILD;
                break;
            case "Psychologist": acount = Account.PSYCHOLOGIST;
                break;
            case "Manager": acount = Account.MANAGER;
                break;
            case "Developer": acount = Account.DEVELOPER;
                break;
            default: acount = Account.CHILD;
        }
    }

    /**
     * toString*() override
     * @return
     */
    @Override
    public String toString() {
        return acount.toString();
    }

    /**
     * get method
     * @return
     */
    public Account getAcount() {
        return acount;
    }

    /**
     * set method
     * @param acount
     */
    public void setAcount(Account acount) {
        this.acount = acount;
    }
}
