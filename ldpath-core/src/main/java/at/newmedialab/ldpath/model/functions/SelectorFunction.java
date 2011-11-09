package at.newmedialab.ldpath.model.functions;

import at.newmedialab.lmf.search.rdfpath.model.selectors.FunctionSelector;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;

/**
 * Intermediate Interface for {@link NodeFunction}s used in the
 * {@link FunctionSelector}
 * 
 * @author jfrank
 * 
 */
public interface SelectorFunction extends NodeFunction<Collection<? extends KiWiNode>> {

}
