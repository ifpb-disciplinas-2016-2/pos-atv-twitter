package io.github.victorhsr.pos.twitter.auth;

import ifpb.ads.AuthenticatorException;
import ifpb.ads.Pair;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Victor Hugo & Ricardo Job <victor.hugo.origins@gmail.com>
 */
@RequestScoped
public class TwitterSecurityHeaderHandler implements Serializable {

    @Inject
    private Credentials credentials;

    public TwitterSecurityHeaderHandler() {
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public String header(List<Pair> urlParams, String method, String url) throws Exception {
        String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oAuthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;
        String oAuthSignatureMethod = "HMAC-SHA1";
        String oAuthTimestamp = time();
        String oAuthVersion = "1.0";

        String signatureBaseString1 = method;
        String signatureBaseString2 = url;

        List<Pair> allParams = new ArrayList<>();
        allParams.add(Pair.create("oauth_consumer_key", credentials.getApiKey()));
        allParams.add(Pair.create("oauth_nonce", oAuthNonce));
        allParams.add(Pair.create("oauth_signature_method", oAuthSignatureMethod));
        allParams.add(Pair.create("oauth_timestamp", oAuthTimestamp));
        allParams.add(Pair.create("oauth_token", credentials.getOauthToken()));
        allParams.add(Pair.create("oauth_version", oAuthVersion));
        allParams.addAll(urlParams);

        allParams.sort((p1, p2) -> p1.key().compareTo(p2.key()));

        StringBuilder signatureBaseString3 = new StringBuilder();
        for (int i = 0; i < allParams.size(); i++) {
            Pair nvp = allParams.get(i);
            if (i > 0) {
                signatureBaseString3.append("&");
            }
            signatureBaseString3.append(encode(nvp.key())).append("=").append(encode(nvp.value().toString()));
        }

        String signatureBaseStringTemplate = "%s&%s&%s";
        String signatureBaseString = String.format(signatureBaseStringTemplate,
                encode(signatureBaseString1),
                encode(signatureBaseString2),
                encode(signatureBaseString3.toString()));

        String compositeKey = encode(credentials.getApiSecret()) + "&" + encode(credentials.getOauthVerifier());
        String oAuthSignature = computeSignature(signatureBaseString, compositeKey);
        String oAuthSignatureEncoded = encode(oAuthSignature);
        String authorizationHeaderValueTempl = "OAuth oauth_consumer_key=\"%s\", oauth_nonce=\"%s\", oauth_signature=\"%s\", oauth_signature_method=\"%s\", oauth_timestamp=\"%s\", oauth_token=\"%s\", oauth_version=\"%s\"";

        String authorizationHeaderValue = String.format(
                authorizationHeaderValueTempl,
                credentials.getApiKey(),
                oAuthNonce,
                oAuthSignatureEncoded,
                oAuthSignatureMethod,
                oAuthTimestamp,
                credentials.getOauthToken(),
                oAuthVersion);

        return authorizationHeaderValue;

    }

    protected String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    }

    public String encode(String value) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
            throw new AuthenticatorException(ignore);
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%'
                    && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7'
                    && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }

    private String time() {
        long millis = System.currentTimeMillis();
        long secs = millis / 1000;
        return String.valueOf(secs);
    }

    public String headerUpdate(String method, String url) throws Exception {
        List<Pair> urlParams = new ArrayList<>();
        return header(urlParams, method, url);
    }

    public String headerUpdate(String method, URI uri) throws Exception {
        List<Pair> urlParams = new ArrayList<>();;
        return header(urlParams, method, uri.toString());
    }

    public String headerUpdate(String method, URI uri, List<Pair> urlParams) throws Exception {
        return header(urlParams, method, uri.toString());
    }

}
