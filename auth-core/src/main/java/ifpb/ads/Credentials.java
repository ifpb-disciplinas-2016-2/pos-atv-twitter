package ifpb.ads;

import org.immutables.value.Value;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 00:28:37
 */
@Value.Immutable
public interface Credentials {

    String acessKey();

    String acessSecret();

    static Credentials empty() {
        return new Credentials() {
            @Override
            public String acessKey() {
                return "";
            }

            @Override
            public String acessSecret() {
                return "";
            }
        };
    }

}
