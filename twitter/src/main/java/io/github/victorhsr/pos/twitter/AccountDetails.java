/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class AccountDetails extends TwitterRequester {

    private final Client cliente = ClientBuilder.newClient();
    private final Gson gson = new Gson();
    private final WebTarget accountWt = cliente.target("https://api.twitter.com/1.1/account/verify_credentials.json");

    public int getFollowersNumber() throws Exception {
        JsonObject jsonObject = gson.fromJson(getAccountDetails(), JsonObject.class);

        return jsonObject.get("followers_count").getAsInt();
    }

    public int getFollowingNumber() throws Exception {
        JsonObject jsonObject = gson.fromJson(getAccountDetails(), JsonObject.class);

        return jsonObject.get("friends_count").getAsInt();
    }

    public JsonObject getFollowersAndFollowing() throws Exception {
        JsonObject jsonObject = gson.fromJson(getAccountDetails(), JsonObject.class);

        JsonObject result = new JsonObject();
        result.add("friends_count", jsonObject.get("friends_count"));
        result.add("followers_count", jsonObject.get("followers_count"));

        return result;
    }

    public String getAccountDetails() throws Exception {
        return doGetRequest(accountWt);
    }

}
