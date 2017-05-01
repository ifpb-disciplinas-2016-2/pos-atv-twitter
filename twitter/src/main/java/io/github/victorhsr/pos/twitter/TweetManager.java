/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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
public class TweetManager extends TwitterRequester {

    private final Client cliente = ClientBuilder.newClient();
    private final Gson gson = new Gson();
    private final WebTarget retweetsWt = cliente.target("https://api.twitter.com/1.1/statuses/retweets_of_me.json");
    private final WebTarget tweetsWt = cliente.target("https://api.twitter.com/1.1/statuses/user_timeline.json");
    private final WebTarget favoritsWt = cliente.target("https://api.twitter.com/1.1/favorites/list.json");

    public JsonArray getTweets() throws Exception {
        return gson.fromJson(doGetRequest(tweetsWt), JsonArray.class);
    }

    public JsonArray getRetweets() throws Exception {
        return gson.fromJson(doGetRequest(retweetsWt), JsonArray.class);
    }

    public JsonArray getRetweetDatails(long retweetId) throws Exception {
        WebTarget target = cliente.target("https://api.twitter.com/1.1/statuses/retweets/" + retweetId + ".json");
        return gson.fromJson(doGetRequest(target), JsonArray.class);
    }

    public JsonObject getTweetDatails(long tweetId) throws Exception {
        WebTarget target = cliente.target("https://api.twitter.com/1.1/statuses/show/" + tweetId + ".json");
        return gson.fromJson(doGetRequest(target), JsonObject.class);
    }

    public JsonArray getFavoritedTweetsByMe() throws Exception {
        return gson.fromJson(doGetRequest(favoritsWt), JsonArray.class);
    }

    public JsonArray getMyTweetsFavorited() throws Exception {
        JsonArray tweets = getTweets();

        JsonArray result = new JsonArray();

        for (JsonElement tweet : tweets) {
            JsonObject tweetObject = tweet.getAsJsonObject();
            if (tweetObject.get("favorite_count").getAsInt() > 0) {
                result.add(tweet);
            }
        }

        return result;
    }

}
