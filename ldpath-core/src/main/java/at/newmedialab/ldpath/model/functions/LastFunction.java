package at.newmedialab.ldpath.model.functions;

import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Selects the <code>last</code> node in the argument list.
 * 
 * @author jfrank
 * 
 */
public class LastFunction implements SelectorFunction {

    @Override
    public Collection<? extends KiWiNode> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        for (int i = args.size() - 1; i >= 0; i--) {
            if (args.get(i).size() > 0) { return args.get(i); }
        }
        return Collections.emptyList();
    }

    @Override
    public String asRdfPathExpression() {
        return "last";
    }

}
