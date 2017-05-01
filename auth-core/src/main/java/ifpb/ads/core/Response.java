package ifpb.ads.core;

import ifpb.ads.ImmutableToken;
import ifpb.ads.Pair;
import ifpb.ads.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 03:20:50
 */
public class Response {

    private String response;
    private Map<String, List<String>> headers;

    public Response(String response) {
        this.response = response;
    }

    public String string() {
        return response;
    }

    public String json() {
        return stringToJson(response);

    }

    private String stringToJson(String tokens_response) {
        StringBuilder builder = new StringBuilder("{");
        StringTokenizer tokenizer = new StringTokenizer(tokens_response, "&");
        while (tokenizer.hasMoreElements()) {
            Object nextElement = tokenizer.nextElement();
            String[] chaves = nextElement.toString().split("=");
            builder.append("\"").append(chaves[0]).append("\" : \"").append(chaves[1]).append("\",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("}");
        return builder.toString();
    }

    public List<Pair> pairs() {
        return stringToPairs(response);
    }

    /*
        params a=1&b=5&c=3
     */
    private List<Pair> stringToPairs(String tokens_response) {
        List<Pair> pairs = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(tokens_response, "&");
        while (tokenizer.hasMoreElements()) {
            Object nextElement = tokenizer.nextElement();
            String[] chaves = nextElement.toString().split("=");
            pairs.add(Pair.create(chaves[0], chaves[1]));
        }
        return pairs;
    }

    protected void headers(Map<String, List<String>> headerFields) {
        this.headers = headerFields;
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public Token token() {
        List<Pair> pairs = pairs();
        Token token = ImmutableToken
                .builder()
                .oauth(valuePair(pairs, "oauth_token"))
                .verifier(valuePair(pairs, "oauth_token_secret"))
                .build();

        return token;
    }

    private String valuePair(List<Pair> pairs, String filter) {
        return pairs.stream()
                .filter((t) -> filter.equals(t.key()))
                .map((t) -> t.value().toString())
                .findFirst()
                .get();
    }

}
