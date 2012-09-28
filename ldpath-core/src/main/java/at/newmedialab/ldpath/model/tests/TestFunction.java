package at.newmedialab.ldpath.model.tests;

import java.util.Collection;
import java.util.List;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.functions.NodeFunction;
import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.api.tests.NodeTest;

public abstract class TestFunction<Node> implements NodeTest<Node>, NodeFunction<Boolean, Node> {

	protected final List<NodeSelector<Node>> arguments;

	public TestFunction(List<NodeSelector<Node>> arguments) {
		this.arguments = arguments;
	}

	public Boolean apply(RDFBackend<Node> backend, Node context,
			Collection<Node>... args) throws IllegalArgumentException {
		if (args.length != 1 || args[0].isEmpty()) {
			throw new IllegalArgumentException("function test '" + getLocalName() + "' only takes one parameter");
		}
		if (args[0].size() != 1) {
			throw new IllegalArgumentException("function test '" + getLocalName() + "' can only be applied to a single node");
		}

		return apply(backend, context, args[0].iterator().next());
	};
	
	public abstract Boolean apply(RDFBackend<Node> backend, Node context, Node node);

	public String getPathExpression(RDFBackend<Node> backend) {
		final StringBuilder format = new StringBuilder("fn:");
		format.append(getLocalName()).append("(");
		boolean first = true;
		for (NodeSelector<Node> arg : arguments) {
			if (!first) {
				format.append(", ");
			}
			format.append(arg.getPathExpression(backend));
			first = false;
		}
		return format.append(")").toString();
	}

	public abstract String getLocalName();

}
