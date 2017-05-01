package ifpb.ads;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 26/08/2016, 23:53:01
 */
public interface Authenticator {

    public String type(); //Basic, OAuth
    public String headerAuthorization(); // a2FpcXVlOjEyMw==, qWH0ygAAAAAAwrD5AAABVsnmP6w
    public String signatureMethod();
    public String encode(String value); 
    public void token(Token oauthToken);
    
}
