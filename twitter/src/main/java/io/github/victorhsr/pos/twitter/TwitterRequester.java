/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter;

import io.github.victorhsr.pos.twitter.auth.TwitterSecurityHeaderHandler;
import javax.inject.Inject;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
public abstract class TwitterRequester {

    @Inject
    private TwitterSecurityHeaderHandler headerHandler;

    protected String doGetRequest(WebTarget target) throws Exception {
        String header = headerHandler.headerUpdate("GET", target.getUri());

        Response response = target
                .request()
                .header("Authorization", header)
                .get();

        String jsonResponse = response.readEntity(String.class);

        return jsonResponse;
    }

}
