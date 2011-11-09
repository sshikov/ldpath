package at.newmedialab.ldpath.model.functions;

import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Selects the <code>first</code> node in the argument list.
 * 
 * @author jakob
 * 
 */
public class FirstFunction implements SelectorFunction {

    @Override
    public Collection<? extends KiWiNode> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        for (Collection<? extends KiWiNode> arg : args) {
            if (arg.size() > 0) { return arg; }
        }
        return Collections.emptyList();
    }

    @Override
    public String asRdfPathExpression() {
        return "first";
    }

}
