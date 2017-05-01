/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.rest;

import io.github.victorhsr.pos.twitter.auth.Credentials;
import io.github.victorhsr.pos.twitter.auth.TokenManager;
import java.io.Serializable;
import java.net.URI;
import java.util.ResourceBundle;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Path("/twittcercallback")
@SessionScoped
public class TwitterOauthCallback implements Serializable {

    @Inject
    private TokenManager tokenManager;
    @Inject
    private Credentials credentials;

    @GET
    public Response requestToken(@QueryParam("oauth_token") String oauthToken,
            @QueryParam("oauth_verifier") String oauthVerifier) {

        try {
            tokenManager.requestLastToken(oauthToken, oauthVerifier);

            ResourceBundle bundle = ResourceBundle.getBundle("application");

            URI baseUri = new URI("/twitter/faces/tuites.xhtml");

            return Response.seeOther(baseUri).build();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Response.noContent().build();
    }

}
