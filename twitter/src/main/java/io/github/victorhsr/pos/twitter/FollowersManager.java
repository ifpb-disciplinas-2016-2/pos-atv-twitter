/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class FollowersManager extends TwitterRequester {

    private final Client cliente = ClientBuilder.newClient();
    private final Gson gson = new Gson();
    private final WebTarget follwersWt = cliente.target("https://api.twitter.com/1.1/followers/ids.json");

    public List<Long> getFollowers() throws Exception {

        String jsonResponse = doGetRequest(follwersWt);

        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

        JsonArray jsonArray = jsonObject.getAsJsonArray("ids");
        List<Long> follwers = new ArrayList<>();

        jsonArray.forEach(element -> {
            follwers.add(element.getAsLong());
        });

        return follwers;
    }

}
