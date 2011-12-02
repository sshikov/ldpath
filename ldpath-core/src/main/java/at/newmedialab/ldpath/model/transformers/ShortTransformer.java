package at.newmedialab.ldpath.model.transformers;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;

public class ShortTransformer<Node> implements NodeTransformer<Short,Node> {

    @Override
    public Short transform(RDFBackend<Node> backend, Node node) throws IllegalArgumentException {
        if(backend.isLiteral(node)) {
            return backend.decimalValue(node).shortValueExact();
        } else {
            throw new IllegalArgumentException("cannot transform node of type "+
                node.getClass().getCanonicalName()+" to short");
        }
    }

}
