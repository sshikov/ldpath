package at.newmedialab.ldpath.model.functions.date;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.model.transformers.DateTransformer;

public class EarliestDateFunction<Node> implements DateFunction<Node> {

	private final DateTransformer<Node> transformer = new DateTransformer<Node>();
	
	public Collection<Node> apply(RDFBackend<Node> backend, Node context,
			Collection<Node>... args) throws IllegalArgumentException {
		if (args.length != 1) {
			throw new IllegalArgumentException("earliest requires exactly one argument");
		}
		
		Node result = null;
		Date earliest = null;
		
		for (Node node : args[0]) {
			try {
				Date d = transformer.transform(backend, node);
				if (earliest == null || d.before(earliest)) {
					earliest = d;
				}
			} catch (IllegalArgumentException e) {
				// Non-Date Literals are just ignored
			}
		}
		
		return result!=null?Collections.singleton(result):Collections.<Node>emptyList();
	}

	public String getSignature() {
		return "fn:earliest(DateLiteralList): DateLiteral";
	}

	public String getDescription() {
		return "select the earliest date (min)";
	}

	public String getPathExpression(RDFBackend<Node> backend) {
		return "earliest";
	}

}
