package at.newmedialab.ldpath.model.selectors;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiTriple;
import kiwi.core.model.rdf.KiWiUriResource;

import java.util.Collection;
import java.util.Collections;

public class ReversePropertySelector implements NodeSelector {

	private final KiWiUriResource property;

	public ReversePropertySelector(KiWiUriResource property) {
		this.property = property;
	}

	@Override
	public Collection<KiWiNode> select(TripleStore tripleStore, KiWiNode context) {
		if (context.isUriResource() || context.isAnonymousResource()) {
			final Function<KiWiTriple, KiWiNode> getSubjectFkt = new Function<KiWiTriple, KiWiNode>() {
				@Override
				public KiWiNode apply(KiWiTriple input) {
					return input.getSubject();
				}
			};

			return Collections2.transform(tripleStore.listTriples(null, property, context, null, true), getSubjectFkt);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public String asRdfPathExpression() {
		if (property != null) {
			return String.format("^<%s>", property.getUri());
		} else {
			return "^*";
		}
	}

}
