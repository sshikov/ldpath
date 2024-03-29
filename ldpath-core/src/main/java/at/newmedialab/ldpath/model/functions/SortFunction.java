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
import at.newmedialab.ldpath.model.transformers.DateTimeTransformer;
import at.newmedialab.ldpath.model.transformers.StringTransformer;

import java.text.Collator;
import java.util.*;

/**
 * Allow sorting of the selection passed as first argument. Usage:
 *
 * <ul>
 *   <li><b>fn:sort(path-expression)</b>: sorts the results according to ascending string order</li>
 *   <li><b>fn:sort(path-expression, order)</b>: sorts the results according to the given order in
 *        ascending direction; order can be one of "string", "number" or "date"</li>
 *   <li><b>fn:sort(path-expression, order, direction)</b>: sorts the results according to the
 *        given order in the specified direction; order can be one of "string", "number" or "date";
 *        direction can be one of "asc" or "desc"</li>
 *
 * </ul>
 * <p/>
 * Author: Sebastian Schaffert
 */
public class SortFunction<Node> implements SelectorFunction<Node> {

    private StringTransformer<Node> transformer;
    private DateTimeTransformer<Node> dateTransformer;


    public SortFunction() {
        transformer = new StringTransformer<Node>();
        dateTransformer = new DateTimeTransformer<Node>();
    }

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    @Override
    public Collection<Node> apply(final RDFBackend<Node> nodeRDFBackend, Node context, Collection<Node>... args) throws IllegalArgumentException {
        String order     = "string";
        String direction = "asc";

        // parse arguments
        if(args.length > 1) {
            order     = transformer.transform(nodeRDFBackend,args[1].iterator().next());
        }
        if(args.length > 2) {
            direction = transformer.transform(nodeRDFBackend,args[2].iterator().next());
        }

        Comparator<Node> comparator = null;

        // some local classes for carrying out the comparison
        if("string".equalsIgnoreCase(order)) {
            comparator = new Comparator<Node>() {

                Collator stringCollator = Collator.getInstance(Locale.getDefault());

                @Override
                public int compare(Node o1, Node o2) {
                    return stringCollator.compare(transformer.transform(nodeRDFBackend,o1),
                                                  transformer.transform(nodeRDFBackend,o2));
                }
            };
        } else if("number".equalsIgnoreCase(order)) {
            comparator = new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return (int)Math.signum(nodeRDFBackend.doubleValue(o2) - nodeRDFBackend.doubleValue(o1));
                }
            };
        } else if("date".equalsIgnoreCase(order)) {
            comparator = new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return  (int) (dateTransformer.transform(nodeRDFBackend,o2).getTime() -
                                   dateTransformer.transform(nodeRDFBackend,o1).getTime());
                }
            };
        }

        if("desc".equalsIgnoreCase(direction) && comparator != null) {
            final Comparator<Node> comparator2 = comparator;
            comparator = new Comparator<Node>() {

                @Override
                public int compare(Node o1, Node o2) {
                    return comparator2.compare(o2,o1);
                }
            };
        }

        List<Node> result = new ArrayList<Node>(args[0]);
        if(comparator != null) {
            java.util.Collections.sort(result,comparator);
        }

        return result;
    }

    /**
     * Return the representation of the NodeFunction or NodeSelector in the RDF Path Language
     *
     * @param nodeRDFBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> nodeRDFBackend) {
        return "sort";
    }


    /**
     * A string describing the signature of this node function, e.g. "fn:content(uris : Nodes) : Nodes". The
     * syntax for representing the signature can be chosen by the implementer. This method is for informational
     * purposes only.
     *
     * @return
     */
    @Override
    public String getSignature() {
        return "fn:sort(nodes : NodeList [, (\"string\"|\"number\"|\"date\") [, (\"asc\"|\"desc\") ] ]) : NodeList ";
    }

    /**
     * A short human-readable description of what the node function does.
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Sort the node list passed as first argument. The second argument can be used to determine the " +
               "sort method, the third argument to determine the sort direction.";
    }
}
