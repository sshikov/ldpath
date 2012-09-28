package at.newmedialab.ldpath.model.functions.math;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.model.Constants;
import at.newmedialab.ldpath.model.transformers.DoubleTransformer;

public class RoundFunction<Node> implements MathFunction<Node> {

	protected final DoubleTransformer<Node> doubleTransformer = new DoubleTransformer<Node>();
	protected final URI intType = URI.create(Constants.NS_XSD + "integer");

	public Collection<Node> apply(RDFBackend<Node> backend, Node context,
			Collection<Node>... args) throws IllegalArgumentException {
		if (args.length != 1) {
			throw new IllegalArgumentException("round takes only one argument");
		}

		ArrayList<Node> result = new ArrayList<Node>();
		for (Node node : args[0]) {
			Node res = calc(backend, node);
			if (res != null) {
				result.add(res);
			}
		}
		return result;
	}

	protected Node calc(RDFBackend<Node> backend, Node node) {
		/* SUM */
		try {
			Double val = doubleTransformer.transform(backend, node);
			return backend.createLiteral(String.valueOf(Math.round(val)), null,
					intType);
		} catch (IllegalArgumentException e) {
			return null;
		}

	}

	public String getDescription() {
		return "Round each argument to the closest int/long value";
	}

	public String getSignature() {
		return "fn:round(LiteralList l) :: IntegerLiteralList";
	}

	public String getPathExpression(RDFBackend<Node> backend) {
		return "round";
	}

}
