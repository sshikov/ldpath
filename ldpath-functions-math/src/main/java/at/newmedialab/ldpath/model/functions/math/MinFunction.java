package at.newmedialab.ldpath.model.functions.math;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.model.Constants;
import at.newmedialab.ldpath.model.transformers.DoubleTransformer;

public class MinFunction<Node> implements MathFunction<Node>  {

	protected final DoubleTransformer<Node> doubleTransformer = new DoubleTransformer<Node>();
	protected final URI doubleType = URI.create(Constants.NS_XSD + "double");


	public Collection<Node> apply(RDFBackend<Node> backend, Node context, Collection<Node>... args)
			throws IllegalArgumentException {
				
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
		/* MIN */
		double d = Double.MAX_VALUE;
		Node min = null;
		for (Node n : arg) {
			try {
				Double val = doubleTransformer.transform(backend, n);
				if (val < d) {
					d = val;
					min = n;
				}
			} catch (IllegalArgumentException e) {
				// we just ignore non-numeric nodes
			}
		}
		
		return min;
	}
	
	public String getDescription() {
		return "select the min of each argument";
	}

	public String getSignature() {
		return "fn:min(LiteralList l [, ...]) :: NumberLiteral(s)";
	}

	public String getPathExpression(RDFBackend<Node> backend) {
		return "min";
	}

}
