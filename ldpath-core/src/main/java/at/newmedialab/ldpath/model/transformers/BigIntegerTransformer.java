package at.newmedialab.ldpath.model.transformers;

import java.math.BigInteger;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;

public class BigIntegerTransformer<Node> implements NodeTransformer<BigInteger,Node> {

    @Override
    public BigInteger transform(RDFBackend<Node> backend, Node node) throws IllegalArgumentException {
        if(backend.isLiteral(node)) {
            return backend.integerValue(node);
        } else {
            throw new IllegalArgumentException("cannot transform node of type "+node.getClass().getCanonicalName()+" to BigInteger");
        }
    }
}
