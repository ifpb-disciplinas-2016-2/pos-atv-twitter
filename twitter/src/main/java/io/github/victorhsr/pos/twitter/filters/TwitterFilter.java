/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.victorhsr.pos.twitter.filters;

import io.github.victorhsr.pos.twitter.auth.Credentials;
import io.github.victorhsr.pos.twitter.auth.TokenManager;
import java.io.IOException;
import java.io.Serializable;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Victor Hugo <victor.hugo.origins@gmail.com>
 */
@WebFilter(filterName = "TwitterFilter", urlPatterns = {"/faces/*"})
public class TwitterFilter implements Filter, Serializable {

    @Inject
    private Credentials credentials;
    @Inject
    private TokenManager tockenManager;

    public TwitterFilter() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        if (credentials.getOauthToken() == null || credentials.getOauthToken().trim().isEmpty()) {
            String url = "https://api.twitter.com/oauth/authenticate?" + tockenManager.requestFirstToken();
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendRedirect(url);
        } else {
            chain.doFilter(request, response);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {
    }

}
