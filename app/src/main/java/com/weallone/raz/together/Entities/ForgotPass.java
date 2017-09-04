package com.weallone.raz.together.Entities;

/**
 * Represents the forgot password object in login activity
 */
public class ForgotPass {
    private String email;
    private String code;

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
    public String getCode() {
        return code;
    }

    /**
     * set method
     * @param code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * construcotr - init members.
     * @param email
     * @param code
     */
    public ForgotPass(String email, String code) {
        this.email = email;
        this.code = code;
    }
}
