package at.newmedialab.ldpath.model.tests.functions;

import java.util.List;

import at.newmedialab.ldpath.api.selectors.NodeSelector;

public class NotEqualTest<Node> extends BinaryNumericTest<Node> {

	public NotEqualTest(List<NodeSelector<Node>> arguments) {
		super(arguments);
	}

	@Override
	protected boolean test(Double left, Double right) {
		return left.compareTo(right) != 0;
	}

	public String getDescription() {
		return "Check whether the two arguments are (numerical) different";
	}

	@Override
	public String getLocalName() {
		return "ne";
	}

}
