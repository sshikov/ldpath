package at.newmedialab.ldpath.model.tests.functions;

import java.util.List;

import at.newmedialab.ldpath.api.selectors.NodeSelector;

public class GreaterThanTest<Node> extends BinaryNumericTest<Node> {

	public GreaterThanTest(List<NodeSelector<Node>> arguments) {
		super(arguments);
	}

	@Override
	protected boolean test(Double left, Double right) {
		return left.compareTo(right) > 0;
	}

	public String getDescription() {
		return "Check whether the first argument is greater than the second";
	}

	@Override
	public String getLocalName() {
		return "gt";
	}

}
