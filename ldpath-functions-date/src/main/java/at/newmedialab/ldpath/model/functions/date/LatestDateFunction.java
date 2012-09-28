package at.newmedialab.ldpath.model.functions.date;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.model.transformers.DateTransformer;

public class LatestDateFunction<Node> implements DateFunction<Node> {

	private final DateTransformer<Node> transformer = new DateTransformer<Node>();
	
	public Collection<Node> apply(RDFBackend<Node> backend, Node context,
			Collection<Node>... args) throws IllegalArgumentException {
		if (args.length != 1) {
			throw new IllegalArgumentException("latest requires exactly one argument");
		}
		
		Node result = null;
		Date latest = null;
		
		for (Node node : args[0]) {
			try {
				Date d = transformer.transform(backend, node);
				if (latest == null || d.after(latest)) {
					latest = d;
				}
			} catch (IllegalArgumentException e) {
				// Non-Date Literals are just ignored
			}
		}
		
		return result!=null?Collections.singleton(result):Collections.<Node>emptyList();
	}

	public String getSignature() {
		return "fn:latest(DateLiteralList): DateLiteral";
	}

	public String getDescription() {
		return "select the latest date (max)";
	}

	public String getPathExpression(RDFBackend<Node> backend) {
		return "latest";
	}

}
