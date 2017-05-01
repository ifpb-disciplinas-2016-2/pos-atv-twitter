/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.auth;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class TokenManager {

    @Inject
    private Credentials credentials;
    @Inject
    private TwitterSecurityHeaderHandler headerHandler;

    public TokenManager() {
    }

    public void requestLastToken(String oauthToken, String oauthVerifier) throws Exception {

        Client cliente = ClientBuilder.newClient();
        WebTarget target = cliente.target("https://api.twitter.com/oauth/access_token");
        credentials.setOauthToken(oauthToken);
        credentials.setOauthVerifier(oauthVerifier);
        Form form = new Form("oauth_token", oauthToken).param("oauth_verifier", oauthVerifier);

        String header = headerHandler.headerUpdate("POST", "https://api.twitter.com/oauth/access_token");

        Response resposta = target
                .request()
                .header("Authorization", header)
                .post(Entity.form(form));

        String tokens = resposta.readEntity(String.class);

        String[] split = tokens.split("&");
        //tokens finais
        String token_a = split[0].split("=")[1];
        String token_v = split[1].split("=")[1];
        credentials.setOauthToken(token_a);
        credentials.setOauthVerifier(token_v);
        System.out.println("---");
        System.out.println(credentials);
        System.out.println("---");
    }

    public String requestFirstToken() {

        try {
            Client cliente = ClientBuilder.newClient();
            WebTarget target = cliente.target("https://api.twitter.com/oauth/request_token");
            Form form = new Form();
            String header = headerHandler.headerUpdate("POST", "https://api.twitter.com/oauth/request_token");

            Response resposta = target
                    .request()
                    .header("Authorization", header)
                    .post(Entity.form(form));
            return resposta.readEntity(String.class);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
