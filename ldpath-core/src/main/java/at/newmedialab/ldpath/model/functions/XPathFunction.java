package at.newmedialab.ldpath.model.functions;

import at.newmedialab.lmf.search.rdfpath.model.transformers.StringTransformer;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiStringLiteral;
import nu.xom.*;

import java.io.IOException;
import java.util.*;

public class XPathFunction implements SelectorFunction {

    private static StringTransformer transformer = new StringTransformer();

    @Override
    public Collection<? extends KiWiNode> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        if (args.size() < 1) { throw new IllegalArgumentException("XPath expression is required as first argument."); }
        Set<String> xpaths = new HashSet<String>();
        for (KiWiNode xpath : args.get(0)) {
            try {
                xpaths.add(transformer.transform(xpath, null));
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("First argument must not contain anything else than String-Literals!");
            }
        }

        Set<KiWiStringLiteral> result = new HashSet<KiWiStringLiteral>();
        for (int i = 1; i < args.size(); i++) {
            for (KiWiNode n : args.get(i)) {
                try {
                    for (String r : doFilter(transformer.transform(n, tripleStore), xpaths)) {
                        result.add(new KiWiStringLiteral(r));
                    }
                } catch (ParsingException e) {
                    // Ignoring ParsingeException -- not all Nodes transform
                    // into valid XML and those are silently ignored.
                } catch (IOException e) {
                    // This should never happen, since validation is turned off.
                }
            }
        }

        return result;
    }

    private LinkedList<String> doFilter(String in, Set<String> xpaths) throws ValidityException, ParsingException, IOException {
        LinkedList<String> result = new LinkedList<String>();
        Document doc = new Builder(false).build(in, null);

        for (String xp : xpaths) {
            try {
                final Nodes nodes = doc.query(xp);
                for (int i = 0; i < nodes.size(); i++) {
                    result.add(nodes.get(i).toXML());
                }
            } catch (XPathException xpe) {
                throw new IllegalArgumentException("Invalid XPath Expression: '" + xp + "'", xpe);
            }
        }
        return result;
    }


    @Override
    public String asRdfPathExpression() {
        return "xpath";
    }

}
