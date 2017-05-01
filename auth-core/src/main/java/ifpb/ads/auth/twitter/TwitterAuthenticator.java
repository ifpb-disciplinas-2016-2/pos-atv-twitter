package ifpb.ads.auth.twitter;

import ifpb.ads.AuthenticatorException;
import ifpb.ads.Credentials;
import ifpb.ads.auth.OAuthAuthenticator;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 00:50:19
 */
public class TwitterAuthenticator extends OAuthAuthenticator {

    public TwitterAuthenticator(Credentials credentials, String url, String method) {
        super(credentials, url, method);
    }

    @Override
    public String signatureMethod() {
        return "HmacSHA1";
    }
    //TODO: refactoring headerAuthorizationWithToken
    //http://stackoverflow.com/questions/13387025/simplest-java-example-retrieving-user-timeline-with-twitter-api-version-1-1
    //https://tectonictech.wordpress.com/2014/08/19/how-to-generate-oauth-signature-for-twitter-in-core-java/
    @Override
    public String headerAuthorization() {
//        if (token() != null) {
//            return headerAuthorizationWithToken();
//        }
        String method = method();
        String oauthSignatureMethod = "HMAC-SHA1";
        String oauthTimestamp = Long.toString(System.currentTimeMillis() / 1000);
        String oauthNonce = "pos12desdfedrfedwsderd" + oauthTimestamp;
//        String oauthNonce = oauthTimestamp;

        String consumerKey = credentials().acessKey();
        String consumerSecret = credentials().acessSecret();
        String oauthToken = (token() == null) ? "" : "&oauth_token=" + token().oauth();

        //ordem alfabetica
        String parameters = "oauth_consumer_key=" + consumerKey
                + "&oauth_nonce=" + oauthNonce
                + "&oauth_signature_method=" + oauthSignatureMethod
                + "&oauth_timestamp=" + oauthTimestamp
                + oauthToken
                + "&oauth_version=1.0";
        String twitterEndpoint = url();

        // METHOD & encode(endpoint) & encode(parametro)
        // O metodo URLEncoder.encode não funciona. RFC 3986 "+", "%20", "*","%2A"
        String signatureBase = method + "&" + encode(twitterEndpoint) + "&" + encode(parameters);
        String oauthVerifier = (token() == null) ? "" : encode(token().verifier());
        String composite = encode(consumerSecret) + "&" + oauthVerifier;
        String oauthSignature = "";
        try {
            oauthSignature = computeSignature(signatureBase, composite);
//            oauthSignature = computeSignature(signatureBase, consumerSecret + "&");
        } catch (GeneralSecurityException | UnsupportedEncodingException e) {
            throw new AuthenticatorException(e);
        }

        oauthToken = (token() == null) ? "" : "\", oauth_token=\"" + token().oauth();

        String authorizationHeader = type()
                + "oauth_consumer_key=\"" + consumerKey
                + "\", oauth_nonce=\"" + oauthNonce
                + "\", oauth_signature=\"" + encode(oauthSignature)
                + "\", oauth_signature_method=\"HMAC-SHA1"
                + "\", oauth_timestamp=\"" + oauthTimestamp
                + oauthToken
                + "\", oauth_version=\"1.0\"";
        return authorizationHeader;
    }

    
}
