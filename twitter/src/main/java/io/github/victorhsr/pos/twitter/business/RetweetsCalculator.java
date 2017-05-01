/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.business;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.victorhsr.pos.twitter.FollowersManager;
import io.github.victorhsr.pos.twitter.TweetManager;
import io.github.victorhsr.pos.twitter.auth.TokenManager;
import io.github.victorhsr.pos.twitter.auth.TwitterSecurityHeaderHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class RetweetsCalculator {

    @Inject
    private TwitterSecurityHeaderHandler headerHandler;
    @Inject
    private TokenManager tokenManager;
    @Inject
    private FollowersManager followersManager;
    @Inject
    private TweetManager tweetManager;
    private final Map<Long, Integer> retweetsByFollwer = new HashMap<>();
    private final Client cliente = ClientBuilder.newClient();
    private final Gson gson = new Gson();
    private long count;

    public long getRetweetsCountage() throws Exception {

        fillFollowersId();
        fillRetweetsByFollwers();

        count = 0;

        retweetsByFollwer.values().forEach(retweetCount -> count += retweetCount);

        return count;
    }

    private void fillFollowersId() throws Exception {
        List<Long> followersList = followersManager.getFollowers();
        followersList.forEach(followerId -> retweetsByFollwer.put(followerId, 0));
    }

    private void fillRetweetsByFollwers() throws Exception {
        JsonArray jsonArray = tweetManager.getRetweets();

        for (JsonElement element : jsonArray) {
            updateRetweetCountage(element.getAsJsonObject());
        }
    }

    private void updateRetweetCountage(JsonObject jsonObject) throws Exception {

        long tweetId = jsonObject.get("id").getAsLong();

        JsonArray jsonArray = tweetManager.getRetweetDatails(tweetId);

        jsonArray.forEach(element -> {
            JsonObject retweetObject = element.getAsJsonObject();
            JsonObject userObject = retweetObject.getAsJsonObject("user");
            long userId = userObject.get("id").getAsLong();

            if (retweetsByFollwer.containsKey(userId)) {
                int count = retweetsByFollwer.get(userId);
                retweetsByFollwer.replace(userId, ++count);
            }
        });

    }

    double getRetweetsCountage(Long user) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
