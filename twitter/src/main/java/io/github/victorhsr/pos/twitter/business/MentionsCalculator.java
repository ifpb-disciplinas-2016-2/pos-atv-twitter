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
public class MentionsCalculator {

    @Inject
    private TwitterSecurityHeaderHandler headerHandler;
    @Inject
    private TokenManager tokenManager;
    @Inject
    private FollowersManager followersManager;
    @Inject
    private TweetManager tweetManager;
    private final Map<Long, Integer> followersMentions = new HashMap<>();
    private final Client cliente = ClientBuilder.newClient();
    private final Gson gson = new Gson();
    private long count;

    public long getMentionsCountage() throws Exception {

        fillFollowersId();
        fillFollwersMentions();

        count = 0;

        followersMentions.values().forEach(mentionCount -> count += mentionCount);

        return count;
    }

    private void fillFollowersId() throws Exception {
        List<Long> followersList = followersManager.getFollowers();
        followersList.forEach(followerId -> followersMentions.put(followerId, 0));
    }

    private void fillFollwersMentions() throws Exception {
        JsonArray jsonArray = tweetManager.getTweets();

        for (JsonElement element : jsonArray) {
            updateMentionCountage(element.getAsJsonObject());
        }
    }

    private void updateMentionCountage(JsonObject jsonObject) throws Exception {

        long tweetId = jsonObject.get("id").getAsLong();

        JsonObject tweetObject = tweetManager.getTweetDatails(tweetId);

        JsonObject tweetEntitie = tweetObject.getAsJsonObject("entities");
        JsonArray tweetMentions = tweetEntitie.getAsJsonArray("user_mentions");

        tweetMentions.forEach(mentionElement -> {
            JsonObject mentionObject = mentionElement.getAsJsonObject();
            long userId = mentionObject.get("id").getAsLong();

            if (followersMentions.containsKey(userId)) {
                int count = followersMentions.get(userId);
                followersMentions.replace(userId, ++count);
            }

        });

    }

}
