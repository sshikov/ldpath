package at.newmedialab.ldpath.model.tests.functions;

import java.util.Collection;
import java.util.List;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.model.tests.TestFunction;
import at.newmedialab.ldpath.model.transformers.DoubleTransformer;

public abstract  class BinaryNumericTest<Node> extends TestFunction<Node> {

	
	private final NodeSelector<Node> left;
	private final NodeSelector<Node> right;
	
	protected final DoubleTransformer<Node> transformer = new DoubleTransformer<Node>();

	public BinaryNumericTest(List<NodeSelector<Node>> arguments) {
		super(arguments);
		
		if (arguments.size() != 2) throw new IllegalArgumentException(getLocalName() + " is a binary test");
		left = arguments.get(0);
		right = arguments.get(1);
	}

	public Boolean apply(RDFBackend<Node> backend, Node context,
			Node node) throws IllegalArgumentException {
		
		Collection<Node> leftArgs = left.select(backend, node, null, null);
		Collection<Node> rightArgs = right.select(backend, node, null, null);
		
		for (Node lN : leftArgs) {
			for (Node rN : rightArgs) {
				if (!test(backend, lN, rN)) return false;
			}
		}
		return true;
	}
	
	protected boolean test(RDFBackend<Node> backend, Node leftNode, Node rightNode) {
		try {
			return test(transformer.transform(backend, leftNode), transformer.transform(backend, rightNode));
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
	
	protected abstract boolean test(Double left, Double right);
	
	public String getSignature() {
		return "fn:"+getLocalName()+"(NumericNode a, NumericNode b) :: Boolean";
	}

}
