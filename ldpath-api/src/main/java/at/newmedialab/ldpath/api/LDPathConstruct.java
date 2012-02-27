package at.newmedialab.ldpath.api;

import at.newmedialab.ldpath.api.backend.RDFBackend;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface LDPathConstruct<Node> {
    /**
     * Return the representation of the NodeFunction or NodeSelector in the RDF Path Language
     *
     * @return
     * @param backend
     */
    public String getPathExpression(RDFBackend<Node> backend);

}
