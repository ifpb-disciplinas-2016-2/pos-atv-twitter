package io.github.victorhsr.pos.twitter.controller;

import io.github.victorhsr.pos.twitter.auth.TokenManager;
import io.github.victorhsr.pos.twitter.business.InfluenceCalculator;
import io.github.victorhsr.pos.twitter.business.PopularityCalculator;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@Named()
@RequestScoped
public class TwitterController implements Serializable {

    @Inject
    private InfluenceCalculator influenceCalculator;
    @Inject
    private PopularityCalculator popularityCalculator;
    @Inject
    private TokenManager tokenManager;

    public double getInfluence() throws IOException {
        try {
            return influenceCalculator.getInfluence();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    public double getPopularity() {
        try {
            return popularityCalculator.getPopularity();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

}
