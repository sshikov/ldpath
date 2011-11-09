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

package at.newmedialab.ldpath.model.tests;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.api.tests.NodeTest;

import java.util.Collection;
import java.util.List;

/**
 * Checks whether a path selector contains a certain node.  Used for the syntax construct
 *
 * <path> is <node>
 *
 * e.g.
 *
 * rdf:type is skos:Concept
 *
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class PathEqualityTest<Node> implements NodeTest<Node> {

    private NodeSelector<Node> path;
    private Node node;


    public PathEqualityTest(NodeSelector<Node> path, Node node) {
        this.node = node;
        this.path = path;
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
    public Boolean apply(RDFBackend<Node> rdfBackend, Collection<Node>... args) throws IllegalArgumentException {
        if (args.length != 1) { throw new IllegalArgumentException("path equality test only takes one parameter"); }
        List<Node> nodes = at.newmedialab.ldpath.util.Collections.concat(args);
        if (nodes.size() != 1) {
            throw new IllegalArgumentException("path equality test can only be applied to a single node");
        }

        Node candidate = nodes.iterator().next();
        return path.select(rdfBackend, candidate).contains(node);
    }

    /**
     * Return the representation of the NodeFunction or NodeSelector in the RDF Path Language
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        if (rdfBackend.isURI(node)) {
            return String.format("%s is <%s>", path.getPathExpression(rdfBackend), rdfBackend.stringValue(node));
        } else {
            // TODO Can this happen?
            return String.format("%s is %s", path.getPathExpression(rdfBackend), rdfBackend.stringValue(node));
        }
    }
}
