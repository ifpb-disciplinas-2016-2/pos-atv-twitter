package ifpb.ads;

import ifpb.ads.core.Response;
import java.util.List;
import org.immutables.value.Value;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 27/08/2016, 00:28:37
 */
@Value.Immutable
public interface Token {

    String oauth();

    String verifier();
    
    static Token empty() {
        return new Token() {
            @Override
            public String oauth() {
                return "";
            }

            @Override
            public String verifier() {
                return "";
            } 
        };
    }
 

}
