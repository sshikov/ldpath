package at.newmedialab.ldpath.model.tests.functions;

import java.util.List;

import at.newmedialab.ldpath.api.selectors.NodeSelector;

public class EqualTest<Node> extends BinaryNumericTest<Node> {

	public EqualTest(List<NodeSelector<Node>> arguments) {
		super(arguments);
	}

	@Override
	protected boolean test(Double left, Double right) {
		return left.compareTo(right) == 0;
	}

	public String getDescription() {
		return "Check whether the two arguments are (numerical) equal";
	}

	@Override
	public String getLocalName() {
		return "eq";
	}

}
