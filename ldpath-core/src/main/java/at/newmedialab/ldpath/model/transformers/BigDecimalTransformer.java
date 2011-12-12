package at.newmedialab.ldpath.model.transformers;

import java.math.BigDecimal;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;

public class BigDecimalTransformer<Node> implements NodeTransformer<BigDecimal,Node> {

    @Override
    public BigDecimal transform(RDFBackend<Node> backend, Node node) throws IllegalArgumentException {
        if(backend.isLiteral(node)) {
            return backend.decimalValue(node);
        } else {
            throw new IllegalArgumentException("cannot transform node of type "+node.getClass().getCanonicalName()+" to BigDecimal");
        }
    }
}
