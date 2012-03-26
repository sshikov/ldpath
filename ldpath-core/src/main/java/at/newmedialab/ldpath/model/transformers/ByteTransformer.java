package at.newmedialab.ldpath.model.transformers;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;

public class ByteTransformer<Node> implements NodeTransformer<Byte,Node> {

    @Override
    public Byte transform(RDFBackend<Node> backend, Node node) throws IllegalArgumentException {
        if(backend.isLiteral(node)) {
            return backend.decimalValue(node).byteValueExact();
        } else {
            throw new IllegalArgumentException("cannot transform node of type "+
                node.getClass().getCanonicalName()+" to byte");
        }
    }

}
