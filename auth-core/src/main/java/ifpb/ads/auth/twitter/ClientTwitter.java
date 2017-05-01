package ifpb.ads.auth.twitter;

import ifpb.ads.auth.FlowOAuth;
import ifpb.ads.Authenticator;
import ifpb.ads.Credentials;
import ifpb.ads.ImmutableCredentials;
import ifpb.ads.ImmutableToken;
import ifpb.ads.Pair;
import ifpb.ads.Token;
import ifpb.ads.core.Connection;
import ifpb.ads.core.ImmutableRequest;
import ifpb.ads.core.Request;
import java.io.Serializable;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 12:48:14
 */
public class ClientTwitter implements Serializable {

    private Credentials credentials;
    private FlowOAuth flow;

    public ClientTwitter() {
        ResourceBundle bundle = ResourceBundle.getBundle("client");
        credentials = ImmutableCredentials
                .builder()
                .acessKey(bundle.getString("acess_key"))
                .acessSecret(bundle.getString("acess_secret"))
                .build();

        Token token = ImmutableToken
                .builder()
                .oauth(bundle.getString("oauth_token"))
                .verifier(bundle.getString("oauth_verifier"))
                .build();

        this.flow = new FlowOAuth(credentials, token);
    }

    public ClientTwitter(String acessKey, String acessSecret) {
        this(ImmutableCredentials
                .builder()
                .acessKey(acessKey)
                .acessSecret(acessSecret)
                .build());
    }

    private ClientTwitter(Credentials credentials) {
        this.credentials = credentials;
        this.flow = new FlowOAuth(credentials);
    }

    /**
     * @return An temp token
     * <link>https://api.twitter.com/oauth/request_token</link>
     */
    public Token startFlow() {
        return flow.startFlow(
                Pair.create("https://api.twitter.com/oauth/request_token", "POST"));
    }

    public Token finishFlow(String oauth_token, String oauth_verifier) {
        Token token = ImmutableToken
                .builder()
                .oauth(oauth_token)
                .verifier(oauth_verifier)
                .build();

        return finishFlow(token);

    }

    public Token finishFlow(Token tempToken) {
        return flow.finishFlow(
                Pair.create("https://api.twitter.com/oauth/access_token", "POST"),
                tempToken);
    }

    public Token finishFlow() {
        return flow.finishFlow(
                Pair.create("https://api.twitter.com/oauth/access_token", "POST"));
    }

    public String timeline() {
        return timeline(flow.token());
    }

    public String timeline(Token token) {
        return timeline(token.oauth(), token.verifier());
    }

    public String timeline(String oauth_token, String oauth_verifier) {
        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json";
        String method = "GET";
        return requestConfig(url, method, oauth_token, oauth_verifier);
    }

    public String update(String string) {
        String url = "https://api.twitter.com/1.1/statuses/update.json";
        String method = "POST";
        return requestConfig(url, method, Pair.create("status", string));
    }

    private String requestConfig(String url, String method, Pair... param) {
        Request requisicao = ImmutableRequest
                .builder()
                .url(url)
                .method(method)
                .credentials(credentials)
                .addHeader(Pair.create("oauth_token", flow.token().oauth()))
                .addHeader(Pair.create("oauth_verifier", flow.token().verifier()))
                .addHeader(Pair.create("Accept-Language", "en-US,en;q=0.8"))
                .addHeader(Pair.create("User-Agent", "Mozilla"))
                .addHeader(Pair.create("Referer", "google.com"))
                .addAllParam(Arrays.asList(param))
                .build();

        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);

//        Token token = ImmutableToken
//                .builder()
//                .oauth(flow.token().)
//                .verifier(oauth_verifier)
//                .build();
        authenticator.token(flow.token());
        
        Connection con = new Connection(requisicao, authenticator);
        return con.response().string();
    }

    private String requestConfig(String url, String method, String oauth_token, String oauth_verifier) {
        Request requisicao = ImmutableRequest
                .builder()
                .url(url)
                .method(method)
                .credentials(credentials)
                .addHeader(Pair.create("oauth_token", oauth_token))
                .addHeader(Pair.create("oauth_verifier", oauth_verifier))
                .addHeader(Pair.create("Accept-Language", "en-US,en;q=0.8"))
                .addHeader(Pair.create("User-Agent", "Mozilla"))
                .addHeader(Pair.create("Referer", "google.com"))
                .build();

        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);

        Token token = ImmutableToken
                .builder()
                .oauth(oauth_token)
                .verifier(oauth_verifier)
                .build();

        authenticator.token(token);
        Connection con = new Connection(requisicao, authenticator);
        return con.response().string();
    }

//        String url = "https://api.twitter.com/oauth/request_token";
//        String method = "POST";
//
//        Request requisicao = ImmutableRequest
//                .builder()
//                .url(url)
//                .method(method)
//                .credentials(credentials)
//                .addHeader(Pair.create("Content-Type", "text/html"))
//                .addHeader(Pair.create("charset", "utf-8"))
//                .build();
//
//        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);
//
//        Connection con = new Connection(requisicao, authenticator);
//        Token token = con.response().token();
//        con.close();
    // }
//    public Token finishFlow(String oauth_token, String oauth_verifier) {
//        String url = "https://api.twitter.com/oauth/access_token";
//        String method = "POST";
//
//        Request requisicao = ImmutableRequest
//                .builder()
//                .url(url)
//                .method(method)
//                .credentials(credentials)
//                .addHeader(Pair.create("Content-Type", "text/html"))
//                .addHeader(Pair.create("charset", "utf-8"))
//                .addHeader(Pair.create("oauth_token", oauth_token))
//                .addParam(Pair.create("oauth_verifier", oauth_verifier))
//                .build();
//
//        Authenticator authenticator = new TwitterAuthenticator(credentials, url, method);
//
//        Token token = ImmutableToken
//                .builder()
//                .oauth(oauth_token)
//                .verifier(oauth_verifier)
//                .build();
//
//        authenticator.token(token);
//
//        Connection con = new Connection(requisicao, authenticator);
//        Token tokenResponse = con.response().token();
//        con.close();
//        return tokenResponse;
//}
}
