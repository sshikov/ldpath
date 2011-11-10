package at.newmedialab.ldpath.backend.sesame;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import org.openrdf.model.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Generic implementation of a Sesame backend for LDPath. A Sesame repository is passed as argument to the
 * constructor.
 * <p/>
 * Implementatins can either use this class directly or implement their own Sesame-based backend by subclassing
 * and calling the super constructor.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class GenericSesameBackend implements RDFBackend<Value> {

    private static final Logger log = LoggerFactory.getLogger(GenericSesameBackend.class);

    private Repository repository;

    /**
     * Initialise a new sesame backend. Repository needs to be set using setRepository.
     */
    protected GenericSesameBackend() {
    }

    /**
     * Initialise a new sesame backend using the repository passed as argument.
     *
     * @param repository
     */
    public GenericSesameBackend(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }


    /**
     * Test whether the node passed as argument is a literal
     *
     * @param n the node to check
     * @return true if the node is a literal
     */
    @Override
    public boolean isLiteral(Value n) {
        return n instanceof Literal;
    }

    /**
     * Test whether the node passed as argument is a URI
     *
     * @param n the node to check
     * @return true if the node is a URI
     */
    @Override
    public boolean isURI(Value n) {
        return n instanceof org.openrdf.model.URI;
    }

    /**
     * Test whether the node passed as argument is a blank node
     *
     * @param n the node to check
     * @return true if the node is a blank node
     */
    @Override
    public boolean isBlank(Value n) {
        return n instanceof BNode;
    }

    /**
     * Return the language of the literal node passed as argument.
     *
     * @param n the literal node for which to return the language
     * @return a Locale representing the language of the literal, or null if the literal node has no language
     * @throws IllegalArgumentException in case the node is no literal
     */
    @Override
    public Locale getLiteralLanguage(Value n) {
        if(isLiteral(n)) {
            if(((Literal)n).getLanguage() != null) {
                return new Locale( ((Literal)n).getLanguage() );
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Value "+n.stringValue()+" is not a literal");
        }
    }

    /**
     * Return the URI of the type of the literal node passed as argument.
     *
     * @param n the literal node for which to return the typer
     * @return a URI representing the type of the literal content, or null if the literal is untyped
     * @throws IllegalArgumentException in case the node is no literal
     */
    @Override
    public URI getLiteralType(Value n) {
        if(isLiteral(n)) {
            if(((Literal)n).getDatatype() != null) {
                try {
                    return new URI(((Literal)n).getDatatype().stringValue());
                } catch (URISyntaxException e) {
                    log.error("literal datatype was not a valid URI: {}",((Literal) n).getDatatype());
                    return null;
                }
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("Value "+n.stringValue()+" is not a literal");
        }
    }

    /**
     * Create a literal node with the content passed as argument
     *
     * @param content string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    @Override
    public Value createLiteral(String content) {
        log.debug("creating literal with content \"{}\"",content);
        return repository.getValueFactory().createLiteral(content);
    }

    /**
     * Create a literal node with the content passed as argument
     *
     * @param content string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    @Override
    public Value createLiteral(String content, Locale language, URI type) {
        log.debug("creating literal with content \"{}\", language {}, datatype {}",new Object[]{content,language,type});
        if(language == null && type == null) {
            return repository.getValueFactory().createLiteral(content);
        } else if(type == null) {
            return repository.getValueFactory().createLiteral(content,language.getLanguage());
        } else  {
            return repository.getValueFactory().createLiteral(content, repository.getValueFactory().createURI(type.toString()));
        }
    }

    /**
     * Create a URI mode with the URI passed as argument
     *
     * @param uri URI of the resource to create
     * @return a URI node using the model used by this backend
     */
    @Override
    public Value createURI(String uri) {
        return repository.getValueFactory().createURI(uri);
    }

    /**
     * Return the string value of a node. For a literal, this will be the content, for a URI node it will be the
     * URI itself, and for a blank node it will be the identifier of the node.
     *
     * @param value
     * @return
     */
    @Override
    public String stringValue(Value value) {
        return value.stringValue();
    }

    /**
     * Return the double value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException, indicating that the value cannot be represented as double, and an
     * IllegalArgumentException, indicating that the passed node is not a literal
     *
     * @param value the literal node for which to return the double value
     * @return double value of the literal node
     * @throws NumberFormatException    in case the literal cannot be represented as double value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    @Override
    public double doubleValue(Value value) {
        if(isLiteral(value)) {
            return ((Literal)value).doubleValue();
        } else {
            throw new IllegalArgumentException("Value "+value.stringValue()+" is not a literal");
        }
    }

    /**
     * Return the long value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException, indicating that the value cannot be represented as long, and an
     * IllegalArgumentException, indicating that the passed node is not a literal
     *
     * @param value the literal node for which to return the long value
     * @return long value of the literal node
     * @throws NumberFormatException    in case the literal cannot be represented as long value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    @Override
    public long longValue(Value value) {
        if(isLiteral(value)) {
            return ((Literal)value).longValue();
        } else {
            throw new IllegalArgumentException("Value "+value.stringValue()+" is not a literal");
        }
    }

    /**
     * List the objects of triples in the triple store underlying this backend that have the subject and
     * property given as argument.
     *
     * @param subject  the subject of the triples to look for
     * @param property the property of the triples to look for
     * @return all objects of triples with matching subject and property
     */
    @Override
    public Collection<Value> listObjects(Value subject, Value property) {
        if(!isURI(property) || !(isURI(subject) || isBlank(subject))) {
            throw new IllegalArgumentException("Subject needs to be a URI or blank node, property a URI node");
        }

        try {
            RepositoryConnection connection = repository.getConnection();

            RepositoryResult<Statement> qResult = connection.getStatements((Resource)subject, (org.openrdf.model.URI)property, null, true, (Resource)null);
            Set<Value> result = new HashSet<Value>();
            while(qResult.hasNext()) {
                Statement stmt = qResult.next();
                result.add(stmt.getObject());
            }
            qResult.close();
            connection.close();

            return result;

        } catch (RepositoryException e) {
            throw new RuntimeException("error while querying Sesame repository!",e);
        }

    }

    /**
     * List the subjects of triples in the triple store underlying this backend that have the object and
     * property given as argument.
     *
     * @param object   the object of the triples to look for
     * @param property the property of the triples to look for
     * @return all dubjects of triples with matching object and property
     * @throws UnsupportedOperationException in case reverse selection is not supported (e.g. when querying Linked Data)
     */
    @Override
    public Collection<Value> listSubjects(Value property, Value object) {
        if(!isURI(property)) {
            throw new IllegalArgumentException("Property needs to be a URI node");
        }

        try {
            RepositoryConnection connection = repository.getConnection();

            RepositoryResult<Statement> qResult = connection.getStatements(null, (org.openrdf.model.URI)property, object, true, null);
            Set<Value> result = new HashSet<Value>();
            while(qResult.hasNext()) {
                Statement stmt = qResult.next();
                result.add(stmt.getSubject());
            }
            qResult.close();
            connection.close();

            return result;

        } catch (RepositoryException e) {
            throw new RuntimeException("error while querying Sesame repository!",e);
        }

    }
}
