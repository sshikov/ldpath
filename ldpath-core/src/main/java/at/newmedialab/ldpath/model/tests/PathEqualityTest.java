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

import at.newmedialab.ldpath.api.tests.NodeTest;
import at.newmedialab.lmf.search.rdfpath.model.selectors.NodeSelector;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiUriResource;

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
 * User: sschaffe
 */
public class PathEqualityTest implements NodeTest {

    private NodeSelector path;
    private KiWiNode node;


    public PathEqualityTest(NodeSelector path, KiWiNode node) {
        this.node = node;
        this.path = path;
    }

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param nodes a list of KiWiNodes
     * @return
     */
    @Override
    public Boolean apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        if (args.size() != 1) { throw new IllegalArgumentException("path equality test only takes one parameter"); }
        Collection<? extends KiWiNode> nodes = args.get(0);
        if (nodes.size() != 1) {
            throw new IllegalArgumentException("path equality test can only be applied to a single node");
        }

        KiWiNode candidate = nodes.iterator().next();
        return path.select(tripleStore, candidate).contains(node);
    }

    @Override
    public String asRdfPathExpression() {
        if (node.isUriResource()) { return String.format("%s is <%s>", path.asRdfPathExpression(), ((KiWiUriResource) node).getUri()); }
        // TODO Can this happen?
        return String.format("%s is %s", path.asRdfPathExpression(), node);
    }
}
