package at.newmedialab.ldpath.model.functions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Selector.SelectorParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.functions.SelectorFunction;
import at.newmedialab.ldpath.model.transformers.StringTransformer;

public class CssSelectFunction<KiWiNode> implements SelectorFunction<KiWiNode> {

    private Logger log = LoggerFactory.getLogger(CssSelectFunction.class);

    private final StringTransformer<KiWiNode> transformer = new StringTransformer<KiWiNode>();

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as
     * argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    @Override
    public Collection<KiWiNode> apply(RDFBackend<KiWiNode> rdfBackend, KiWiNode context, Collection<KiWiNode>... args)
            throws IllegalArgumentException {
        if (args.length < 1) throw new IllegalArgumentException("CSS-Selector is required as first argument.");
        Set<String> jsoupSelectors = new HashSet<String>();
        for (KiWiNode xpath : args[0]) {
            try {
                jsoupSelectors.add(transformer.transform(rdfBackend, xpath));
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("First argument must not contain anything else than String-Literals!");
            }
        }
        Iterator<KiWiNode> it;
        if (args.length < 2) {
            log.debug("Use context {} to apply css-selector {}", context, jsoupSelectors);
            it = Collections.singleton(context).iterator();
        } else {
            log.debug("apply css-selector {} on parsed parameters", jsoupSelectors);
            it = at.newmedialab.ldpath.util.Collections.iterator(1, args);
        }
        List<KiWiNode> result = new ArrayList<KiWiNode>();
        while (it.hasNext()) {
            KiWiNode n = it.next();
            try {
                final String string = transformer.transform(rdfBackend, n);
                final Document jsoup = Jsoup.parse(string);
                if (rdfBackend.isURI(context)) {
                    jsoup.setBaseUri(rdfBackend.stringValue(context));
                }
                for (String r : doFilter(jsoup, jsoupSelectors)) {
                    result.add(rdfBackend.createLiteral(r));
                }
            } catch (IOException e) {
                // This should never happen, since validation is turned off.
            }
        }

        return result;
    }

    private LinkedList<String> doFilter(Document jsoup, Set<String> jsoupSelectors) throws IOException {
        LinkedList<String> result = new LinkedList<String>();
        for (String jsoupSel : jsoupSelectors) {
            try {
                for (Element e : jsoup.select(jsoupSel)) {
                    result.add(e.outerHtml());
                }
            } catch (SelectorParseException xpe) {
                throw new IllegalArgumentException("error while processing jsoup selector: '" + jsoupSel + "'", xpe);
            }
        }
        return result;
    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<KiWiNode> backend) {
        return "css";
    }

    /**
     * A string describing the signature of this node function, e.g.
     * "fn:content(uris : Nodes) : Nodes". The
     * syntax for representing the signature can be chosen by the implementer. This method is for
     * informational
     * purposes only.
     *
     * @return
     */
    @Override
    public String getSignature() {
        return "fn:css(jsoup: String [, nodes: XMLLiteralList]) : LiteralList";
    }

    /**
     * A short human-readable description of what the node function does.
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Evaluate an JSoup CSS selector on either the value of the context node or the values of the nodes passed as arguments.";
    }
}

