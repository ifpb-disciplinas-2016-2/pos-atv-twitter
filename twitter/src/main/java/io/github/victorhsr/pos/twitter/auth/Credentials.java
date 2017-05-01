/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.auth;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@SessionScoped
public class Credentials implements Serializable {

    private String apiKey;
    private String apiSecret;

    private String oauthToken = "";
    private String oauthVerifier = "";

    public Credentials() {
        ResourceBundle bundle = ResourceBundle.getBundle("application");

        apiKey = bundle.getString("apiKey");
        apiSecret = bundle.getString("apiSecret");
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthVerifier() {
        return oauthVerifier;
    }

    public void setOauthVerifier(String oauthVerifier) {
        this.oauthVerifier = oauthVerifier;
    }

    @Override
    public String toString() {
        return "Credentials{" + "apiKey=" + apiKey + ", apiSecret=" + apiSecret + ", oauthToken=" + oauthToken + ", oauthVerifier=" + oauthVerifier + '}';
    }

}
