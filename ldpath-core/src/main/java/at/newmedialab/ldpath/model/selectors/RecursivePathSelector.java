package at.newmedialab.ldpath.model.selectors;

import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecursivePathSelector implements NodeSelector {

	private final NodeSelector delegate;

	public RecursivePathSelector(NodeSelector delegate) {
		this.delegate = delegate;
	}

	@Override
	public Collection<KiWiNode> select(TripleStore tripleStore, KiWiNode context) {
		Set<KiWiNode> result = new HashSet<KiWiNode>();

		subSelect(tripleStore, context, result);

		return result;
	}

	private void subSelect(TripleStore tripleStore, KiWiNode currentContext, Set<KiWiNode> resultSet) {
		Collection<KiWiNode> nodesLeft = delegate.select(tripleStore, currentContext);
		for (KiWiNode n : nodesLeft) {
			if (!resultSet.contains(n)) {
				resultSet.add(n);
				subSelect(tripleStore, n, resultSet);
			}
		}
	}

	@Override
	public String asRdfPathExpression() {
		return String.format("(%s)+", delegate.asRdfPathExpression());
	}

}
