/*
 * Copyright (c) 2012 Salzburg Research.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.newmedialab.ldpath.model.functions;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.functions.SelectorFunction;
import at.newmedialab.ldpath.model.transformers.StringTransformer;
import org.jdom2.*;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.input.sax.XMLReaders;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class XPathFunction<Node> implements SelectorFunction<Node> {
    
    private static final Logger log = LoggerFactory.getLogger(XPathFunction.class);

    private final StringTransformer<Node> transformer = new StringTransformer<Node>();


    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    @Override
    public Collection<Node> apply(RDFBackend<Node> rdfBackend, Node context, Collection<Node>... args) throws IllegalArgumentException {
        if (args.length < 1) { throw new IllegalArgumentException("XPath expression is required as first argument."); }
        Set<String> xpaths = new HashSet<String>();
        for (Node xpath : args[0]) {
            try {
                xpaths.add(transformer.transform(rdfBackend,xpath));
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("First argument must not contain anything else than String-Literals!");
            }
        }
        Iterator<Node> it;
        if(args.length < 2){
            log.debug("Use context {} to execute xpaths {}",context,xpaths);
            it = Collections.singleton(context).iterator();
        } else {
            log.debug("execute xpaths {} on parsed parameters",xpaths);
            it = at.newmedialab.ldpath.util.Collections.iterator(1,args);
        }
        List<Node> result = new ArrayList<Node>();
        while (it.hasNext()) {
            Node n = it.next();
            try {
                for (String r : doFilter(transformer.transform(rdfBackend,n), xpaths)) {
                    result.add(rdfBackend.createLiteral(r));
                }
            } catch (IOException e) {
                // This should never happen, since validation is turned off.
            }
        }

        return result;
    }

    private LinkedList<String> doFilter(String in, Set<String> xpaths) throws IOException {
        LinkedList<String> result = new LinkedList<String>();
        try {
            Document doc = new SAXBuilder(XMLReaders.NONVALIDATING).build(new StringReader(in));
            XMLOutputter out = new XMLOutputter();

            for (String xp : xpaths) {
                XPathExpression<Content> xpath = XPathFactory.instance().compile(xp, Filters.content());
                for (Content node : xpath.evaluate(doc)) {
                    if(node instanceof Element)
                        result.add(out.outputString((Element) node));
                    else if(node instanceof Text)
                        result.add(out.outputString((Text) node));
                }
            }
            return result;
        } catch (JDOMException xpe) {
            throw new IllegalArgumentException("error while processing xpath expressions: '" + xpaths + "'", xpe);
        }
    }


    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        return "xpath";
    }
}
