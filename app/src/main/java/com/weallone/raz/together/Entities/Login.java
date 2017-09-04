package com.weallone.raz.together.Entities;

/**
 * Represents the login object from login activity.
 */
public class Login {
    private String email;
    private String pass;

    /**
     * get method
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * set method
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get method
     * @return
     */
    public String getPass() {
        return pass;
    }

    /**
     * set method
     * @param pass
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * Construcot, init members.
     * @param email
     * @param pass
     */
    public Login(String email, String pass) {
        this.email = email;
        this.pass = pass;
    }
}
