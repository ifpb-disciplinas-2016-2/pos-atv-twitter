/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.business;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class InfluenceCalculator {

    @Inject
    private MentionsCalculator mentionsCalculator;
    @Inject
    private RetweetsCalculator retweetsCalculator;

    public double getInfluence() throws Exception {

        double mentions = mentionsCalculator.getMentionsCountage();

        double retweets = retweetsCalculator.getRetweetsCountage();

        double sum = mentions + retweets + 2;

        double result = 1 - (1 / sum);

        return result;
    }

}
