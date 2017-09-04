package com.weallone.raz.together.Entities;

import com.weallone.raz.together.Enums.Account;
import com.weallone.raz.together.Enums.CAccount;

/**
 * Represents a User object - saved in bundle
 */
public class UserInfo {
    private String id;
    private CAccount account;
    private String name;
    private String userToken;
    private Integer face = null;
    private String pEmail = "";

    public String getpEmail() {
        return pEmail;
    }

    public void setpEmail(String pEmail) {
        this.pEmail = pEmail;
    }

    public String getpPhone() {
        return pPhone;
    }

    public void setpPhone(String pPhone) {
        this.pPhone = pPhone;
    }

    private String pPhone = "";

    public Integer getFace() {
        return face;
    }

    public void setFace(Integer face) {
        this.face = face;
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

    private String email;

    /**
     * get method
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     * set method
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * set method
     * @param account
     */
    public void setAccount(CAccount account) {
        this.account = account;
    }

    /**
     * get method
     * @return
     */
    public String getUserToken() {
        return userToken;
    }

    /**
     * set method
     * @param userToken
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * Constructor - init members
     * @param id - of user
     * @param account - entity
     * @param name - name
     * @param icon - icon choosen
     * @param userToken - deviceId
     * @param email - email
     * @param password - password
     */
    public UserInfo(String id, String account, String name, String icon, String userToken, String email, String password, String pEmail,
                    String pPhone) {
        this.id = id;
        this.account = new CAccount(account);
        this.name = name;
        this.icon = icon;
        this.userToken = userToken;
        this.email = email;
        this.pass = password;
        this.pEmail = pEmail;
        this.pPhone = pPhone;
    }

    private String icon;

    /**
     * get method
     * @return
     */
    public Account getAccount() {
        return account.getAcount();
    }

    /**
     * set method
     * @param account
     */
    public void setAccount(Account account) {
        this.account.setAcount(account);
    }

    /**
     * get method
     * @return
     */
    public String getId() {
        return id;
    }

    /**
     * set method
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * get method
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * set method
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

}
