package at.newmedialab.ldpath.model.functions.math;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.model.Constants;
import at.newmedialab.ldpath.model.transformers.DoubleTransformer;

public class SumFunction<Node> implements MathFunction<Node> {

	protected final DoubleTransformer<Node> doubleTransformer = new DoubleTransformer<Node>();
	protected final URI doubleType = URI.create(Constants.NS_XSD + "double");

	public Collection<Node> apply(RDFBackend<Node> backend, Node context,
			Collection<Node>... args) throws IllegalArgumentException {

		ArrayList<Node> result = new ArrayList<Node>();
		for (Collection<Node> arg : args) {
			Node res = calc(backend, arg);
			if (res != null) {
				result.add(res);
			}
		}
		return result;
	}

	protected Node calc(RDFBackend<Node> backend, Collection<Node> arg) {
		/* SUM */
		Double d = 0d;
		for (Node n : arg) {
			try {
				Double val = doubleTransformer.transform(backend, n);
				d += val.doubleValue();
			} catch (IllegalArgumentException e) {
				// we just ignore non-numeric nodes
			}
		}

		return backend.createLiteral(String.valueOf(d), null, doubleType);
	}

	public String getSignature() {
		return "fn:sum(LiteralList l [, ...]) :: NumberLiteral(s)";
	}

	public String getDescription() {
		return "Sum up each argument";
	}

	public String getPathExpression(RDFBackend<Node> backend) {
		return "sum";
	}

}
