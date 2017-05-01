package ifpb.ads.auth;

import ifpb.ads.auth.twitter.TwitterAuthenticator;
import ifpb.ads.Authenticator;
import ifpb.ads.Credentials;
import ifpb.ads.Pair;
import ifpb.ads.Token;
import ifpb.ads.core.Connection;
import ifpb.ads.core.ImmutableRequest;
import ifpb.ads.core.Request;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 28/08/2016, 23:14:11
 */
public class FlowOAuth {

    private final Credentials credentials;
    private Token oauthToken;

    public FlowOAuth(Credentials credentials) {
        this.credentials = credentials;
    }

    public FlowOAuth(Credentials credentials, Token token) {
        this.credentials = credentials;
        this.oauthToken = token;
    }

    public Token startFlow(Pair urlAndMethod) {
        String url = urlAndMethod.key();
        String method = urlAndMethod.value().toString();
        Request requisicao = ImmutableRequest
                .builder()
                .url(url)
                .method(method)
                .credentials(credentials)
                .addHeader(Pair.create("Content-Type", "text/html"))
                .addHeader(Pair.create("charset", "utf-8"))
                .build();

        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);
        Connection con = new Connection(requisicao, authenticator);
        oauthToken = con.response().token();
        con.close();
        return oauthToken;
    }

    public Token finishFlow(Pair urlAndMethod, Token token) {
        String url = urlAndMethod.key();
        String method = urlAndMethod.value().toString();

        Request requisicao = ImmutableRequest
                .builder()
                .url(url)
                .method(method)
                .credentials(credentials)
                .addHeader(Pair.create("Content-Type", "text/html"))
                .addHeader(Pair.create("charset", "utf-8"))
                .addHeader(Pair.create("oauth_token", token.oauth()))
                .addParam(Pair.create("oauth_verifier", token.verifier()))
                .build();

        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);
        authenticator.token(token);

        Connection con = new Connection(requisicao, authenticator);
        Token tokenResponse = con.response().token();
        con.close();
        return tokenResponse;
    }

    public Token finishFlow(Pair urlAndMethod) {
        return finishFlow(urlAndMethod, oauthToken);
    }

    public Token token() {
        return this.oauthToken;
    }

}
