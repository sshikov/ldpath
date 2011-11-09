package at.newmedialab.ldpath.api.backend;

import java.util.Collection;

/**
 * A generic API for RDF models and triple stores; provides the testing and navigation functions needed for LDPath.
 * Implementations exist for Sesame, LMF, Jena and Clerezza. The API is somewhat typesafe by making use of a generic
 * for nodes.
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface RDFBackend<Node> {

    /**
     * Test whether the node passed as argument is a literal
     * @param n the node to check
     * @return true if the node is a literal
     */
    public boolean isLiteral(Node n);

    /**
     * Test whether the node passed as argument is a URI
     * @param n the node to check
     * @return true if the node is a URI
     */
    public boolean isURI(Node n);

    /**
     * Test whether the node passed as argument is a blank node
     * @param n the node to check
     * @return true if the node is a blank node
     */
    public boolean isBlank(Node n);


    /**
     * Create a literal node with the content passed as argument
     * @param content  string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    public Node createLiteral(String content);


    /**
     * Create a URI mode with the URI passed as argument
     * @param uri  URI of the resource to create
     * @return a URI node using the model used by this backend
     */
    public Node createURI(String uri);


    /**
     * Return the string value of a node. For a literal, this will be the content, for a URI node it will be the
     * URI itself, and for a blank node it will be the identifier of the node.
     * @param node
     * @return
     */
    public String stringValue(Node node);

    /**
     * List the objects of triples in the triple store underlying this backend that have the subject and
     * property given as argument.
     *
     * @param subject  the subject of the triples to look for
     * @param property the property of the triples to look for
     * @return all objects of triples with matching subject and property
     */
    public Collection<Node> listObjects(Node subject, Node property);


    /**
     * List the subjects of triples in the triple store underlying this backend that have the object and
     * property given as argument.
     *
     * @param object  the object of the triples to look for
     * @param property the property of the triples to look for
     * @return all dubjects of triples with matching object and property
     * @throws UnsupportedOperationException in case reverse selection is not supported (e.g. when querying Linked Data)
     */
    public Collection<Node> listSubjects(Node property, Node object);

}
