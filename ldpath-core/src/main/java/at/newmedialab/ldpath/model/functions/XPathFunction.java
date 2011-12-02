/*
 * Copyright (c) 2011 Salzburg Research.
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;
import sun.security.pkcs.ParsingException;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

public class XPathFunction<Node> implements SelectorFunction<Node> {

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
    public Collection<Node> apply(RDFBackend<Node> rdfBackend, Collection<Node>... args) throws IllegalArgumentException {
        if (args.length < 1) { throw new IllegalArgumentException("XPath expression is required as first argument."); }
        Set<String> xpaths = new HashSet<String>();
        for (Node xpath : args[0]) {
            try {
                xpaths.add(transformer.transform(rdfBackend,xpath));
            } catch (IllegalArgumentException iae) {
                throw new IllegalArgumentException("First argument must not contain anything else than String-Literals!");
            }
        }

        List<Node> result = new ArrayList<Node>();
        for (int i = 1; i < args.length; i++) {
            for (Node n : args[i]) {
                try {
                    for (String r : doFilter(transformer.transform(rdfBackend,n), xpaths)) {
                        result.add(rdfBackend.createLiteral(r));
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

    private LinkedList<String> doFilter(String in, Set<String> xpaths) throws IOException {
        LinkedList<String> result = new LinkedList<String>();
        try {
            Document doc = new SAXBuilder(false).build(new StringReader(in));
            XMLOutputter out = new XMLOutputter();

            for (String xp : xpaths) {
                List<?> nodes = XPath.selectNodes(doc,xp);
                for (Object node : nodes) {
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
