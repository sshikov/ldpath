package at.newmedialab.ldpath.model.tests;

import java.util.Collection;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.tests.NodeTest;

public class NotTest<Node> implements NodeTest<Node> {

	private final NodeTest<Node> delegate;

	public NotTest(NodeTest<Node> delegate) {
		this.delegate = delegate;
	}
	
	@Override
	public Boolean apply(RDFBackend<Node> backend, Node context, Collection<Node>... args) throws IllegalArgumentException {
		Boolean result = delegate.apply(backend, context, args);
		return result != null?new Boolean(!result.booleanValue()): Boolean.FALSE;
	}

	@Override
	public String getPathExpression(RDFBackend<Node> backend) {
		if (delegate instanceof ComplexTest<?>)
			return String.format("!(%s)", delegate.getPathExpression(backend));
		else
			return String.format("!%s", delegate.getPathExpression(backend));
	}

    /**
     * A string describing the signature of this node function, e.g. "fn:content(uris : Nodes) : Nodes". The
     * syntax for representing the signature can be chosen by the implementer. This method is for informational
     * purposes only.
     *
     * @return
     */
    @Override
    public String getSignature() {
        return "!test :: Boolean -> Boolean";
    }

    /**
     * A short human-readable description of what the node function does.
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Negates the test given as argument";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        @SuppressWarnings("rawtypes")
        NotTest notTest = (NotTest) o;

        if (delegate != null ? !delegate.equals(notTest.delegate) : notTest.delegate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }

}
