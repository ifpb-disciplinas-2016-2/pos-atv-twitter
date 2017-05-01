/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.business;

import com.google.gson.JsonObject;
import io.github.victorhsr.pos.twitter.AccountDetails;
import io.github.victorhsr.pos.twitter.FollowersManager;
import io.github.victorhsr.pos.twitter.TweetManager;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class PopularityCalculator {

    @Inject
    private FollowersManager followersManager;
    @Inject
    private TweetManager tweetManager;
    @Inject
    private AccountDetails accountDetails;

    public double getPopularity() throws Exception {

        JsonObject followersAnFollowing = accountDetails.getFollowersAndFollowing();

        double following = followersAnFollowing.get("friends_count").getAsInt();
        double followers = followersAnFollowing.get("followers_count").getAsInt();

        double myFavoritedTweets = tweetManager.getMyTweetsFavorited().size();
        double favoritedsByMe = tweetManager.getFavoritedTweetsByMe().size();

        double retweets = tweetManager.getRetweets().size();

        double parcelOne = following / followers;

        double parcelTwo = favoritedsByMe + retweets;
        parcelTwo /= myFavoritedTweets;

        double result = parcelOne * parcelTwo;

        return result;
    }

}
