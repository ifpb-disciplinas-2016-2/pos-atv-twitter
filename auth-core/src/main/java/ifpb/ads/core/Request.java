package ifpb.ads.core;

import ifpb.ads.Credentials;
import ifpb.ads.Pair;
import java.util.List;
import org.immutables.value.Value;

/**
 * @author Ricardo Job
 * @mail ricardo.job@ifpb.edu.br
 * @since 26/08/2016, 23:52:29
 */
@Value.Immutable
public interface Request {
    String url();
    String method();
    Credentials credentials();
    List<Pair> header();
    List<Pair> param();
    List<Pair> query();
    
    
}
