package at.newmedialab.ldpath.model.functions;

import at.newmedialab.lmf.search.rdfpath.model.transformers.StringTransformer;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiStringLiteral;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class RemoveXmlTagsFunction implements SelectorFunction {

    private static StringTransformer transformer = new StringTransformer();

    private static Pattern XML_TAG = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^>])*>", Pattern.MULTILINE);

    @Override
    public Set<KiWiStringLiteral> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        Set<KiWiStringLiteral> result = new HashSet<KiWiStringLiteral>();
        for (Collection<? extends KiWiNode> nodes : args) {
            for (KiWiNode n : nodes) {
                result.add(new KiWiStringLiteral(doFilter(transformer.transform(n, tripleStore))));
            }
        }

        return result;
    }

    private String doFilter(String in) {
        return XML_TAG.matcher(in).replaceAll("");
    }

    @Override
    public String asRdfPathExpression() {
        return "removeTags";
    }

}
