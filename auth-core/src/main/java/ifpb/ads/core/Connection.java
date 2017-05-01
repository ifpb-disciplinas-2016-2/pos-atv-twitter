package ifpb.ads.core;

import ifpb.ads.Authenticator;
import ifpb.ads.Pair;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 01:32:07
 */
public class Connection {

    private HttpURLConnection connection;
    private Request request;
    private Authenticator authenticator;
    private boolean allowRedirect = false;

    public Connection(Request request) {
        this.request = request;
    }

    public Connection(Request request, Authenticator authenticator) {
        this.request = request;
        this.authenticator = authenticator;
    }

    public Response response() {
        try {
            open();
            headers();
            body();
            return createResponse(read());
        } catch (Exception ex) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
            return new Response("oauth_token=" + ex.getMessage() + "&oauth_token_secret=" + ex.getMessage());
        }
    }

    //TODO: See param query for add in url
    private void open() throws IOException {
        StringBuilder query = encodeParams(request.query());
        String urlOpen = encodeURL(query);
        URL url = new URL(urlOpen); //TODO: need encode url?
        connection = (HttpURLConnection) url.openConnection();
        connection.setInstanceFollowRedirects(allowRedirect);
        HttpURLConnection.setFollowRedirects(allowRedirect);
        connection.setRequestMethod(request.method());
    }

    private String encodeURL(StringBuilder query) {
        StringBuilder builder = new StringBuilder(request.url());

        if (query.length() != 0) {
            builder.append('?');
        }
        builder.append(query);
        return builder.toString();
    }

    private void headers() {
        request.header().stream().forEach((pair) -> {
            connection.setRequestProperty(pair.key(), pair.value().toString());
        });
        if (authenticator != null) {
            connection.setRequestProperty("Authorization", authenticator.headerAuthorization());
        }
    }

    private void body() throws UnsupportedEncodingException, IOException {
        if ("PUT".equals(request.method()) || "POST".equals(request.method())) {
            connection.setDoOutput(true);
            StringBuilder postData = encodeParams(request.param());
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.getOutputStream().write(postDataBytes);
        }
    }

    //encode params
    private StringBuilder encodeParams(List<Pair> params) {
        StringBuilder postData = new StringBuilder();
        for (Pair pair : params) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(encode(pair.key()));
            postData.append('=');
            postData.append(encode(pair.value().toString()));
        }

        return postData;
    }

    //https://github.com/fivedogit/twitter_api_examples/blob/master/src/net/adkitech/Twitter.java
    private String read() throws IOException {
        Reader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;) {
            sb.append((char) c);
        }
        return sb.toString();
    }

    private String encode(String key) {
        if (authenticator == null) {
            return key; //Not encoded
        }
        return authenticator.encode(key);
    }

    private Response createResponse(String read) {
        Response response = new Response(read);
        response.headers(connection.getHeaderFields());
        return response;
    }

    public void allowRedirect() {
        this.allowRedirect = true;
    }

    public void close() {
        connection.disconnect();
    }
}
