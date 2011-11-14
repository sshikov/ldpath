package at.newmedialab.ldpath;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.exception.LDPathParseException;
import at.newmedialab.ldpath.model.fields.FieldMapping;
import at.newmedialab.ldpath.model.programs.Program;
import at.newmedialab.ldpath.parser.ParseException;
import at.newmedialab.ldpath.parser.RdfPathParser;

import java.io.Reader;
import java.io.StringReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Main class providing query functionality for the different RDF backends.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class LDPath<Node> {

    private RDFBackend<Node> backend;

    /**
     * Initialise a new LDPath instance for querying the backend passed as argument.
     * @param backend
     */
    public LDPath(RDFBackend<Node> backend) {
        this.backend = backend;
    }

    /**
     * Execute a single path query starting from the given context node and return a collection of nodes resulting
     * from the selection. Default namespaces (rdf, rdfs, skos, dc, foaf) are added automatically, further namespaces
     * need to be passed as arguments.
     * <p/>
     * Paths need to conform to the RdfPath Selector syntax described at
     * <a href="http://code.google.com/p/kiwi/wiki/RdfPathLanguage#Path_Selectors">the wiki</a>.
     * For example, the following selection would select the names of all friends:
     * <p/>
     * <code>
     * foaf:knows / foaf:name
     * </code>
     * <p/>
     * Note that since this method returns a collection of nodes, no transformations can be used.
     *
     * @param context the context node where to start the path query
     * @param path  the LDPath path specification
     * @param namespaces an optional map mapping namespace prefixes to URIs (used for lookups of prefixes used in the path);
     *                   can be null
     * @return a collection of nodes
     * @throws LDPathParseException when the path passed as argument is not valid
     */
    public Collection<Node> pathQuery(Node context, String path, Map<String, String> namespaces) throws LDPathParseException {

        RdfPathParser<Node> parser = new RdfPathParser<Node>(backend,new StringReader(path));

        try {
            NodeSelector<Node> selector = parser.parseSelector(namespaces);

            return selector.select(backend,context);

        } catch (ParseException e) {
            throw new LDPathParseException("error while parsing path expression",e);
        }

    }

    /**
     * Evaluate a path program passed as argument starting from the given context node and return a mapping for
     * each field in the program to the selected values.
     *
     * @param context
     * @param program
     * @return
     * @throws LDPathParseException
     */
    public Map<String,Collection<?>> programQuery(Node context, Reader program) throws LDPathParseException {
        RdfPathParser<Node> parser = new RdfPathParser<Node>(backend,program);

        try {
            Program<Node> p = parser.parseProgram();

            Map<String,Collection<?>> result = new HashMap<String, Collection<?>>();

            for(FieldMapping<?,Node> mapping : p.getFields()) {
                result.put(mapping.getFieldName(),mapping.getValues(backend,context));
            }

            return result;

        } catch (ParseException e) {
            throw new LDPathParseException("error while parsing path expression",e);
        }
    }
}
