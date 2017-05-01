package ifpb.ads;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 01:01:23
 */
public class AuthenticatorException extends RuntimeException {

    public AuthenticatorException(Throwable e) {
        super(e);
    }

    public AuthenticatorException(String msg) {
        super(msg);
    }

}
