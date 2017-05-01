package ifpb.ads.auth;

import ifpb.ads.AuthenticatorException;
import ifpb.ads.Authenticator;
import ifpb.ads.Credentials;
import ifpb.ads.Token;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 00:45:22
 */
public abstract class OAuthAuthenticator implements Authenticator {

    private final String type = "OAuth ";
    private final Credentials credentials;
    private final String url;
    private final String method;
    private Token token;

    public OAuthAuthenticator(Credentials credentials, String url, String method) {
        this.credentials = credentials;
        this.url = url;
        this.method = method;
        this.token = Token.empty();
    }

    @Override
    public String type() {
        return this.type;
    }

    protected Credentials credentials() {
        return credentials;
    }

    protected String url() {
        return url;
    }

    protected String method() {
        return method;
    }

    public Token token() {
        return token;
    }

    @Override
    public void token(Token token) {
        this.token = token;
    }

     

    protected String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKey secretKey = null;

        byte[] keyBytes = keyString.getBytes();
        secretKey = new SecretKeySpec(keyBytes, signatureMethod());

        Mac mac = Mac.getInstance(signatureMethod());
        mac.init(secretKey);

        byte[] text = baseString.getBytes();

        return new String(Base64.getEncoder().encode(mac.doFinal(text))).trim();
    }

    @Override
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

}
