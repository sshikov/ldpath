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

import java.util.Collection;
import java.util.List;

/**
 * Tests whether the path given as argument for the constructor yields at least one node when evaluated
 * from the context node to which the test is applied.
 * <p/>
 * User: sschaffe
 */
public class PathTest implements NodeTest {

    private NodeSelector path;

    public PathTest(NodeSelector path) {
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
        if (args.size() != 1) { throw new IllegalArgumentException("path test only takes one parameter"); }
        Collection<? extends KiWiNode> nodes = args.get(0);
        if (nodes.size() != 1) {
            throw new IllegalArgumentException("path test can only be applied to a single node");
        }

        KiWiNode node = nodes.toArray(new KiWiNode[1])[0];

        if (node.isAnonymousResource() || node.isUriResource()) {
            Collection<KiWiNode> testResult = path.select(tripleStore, node);
            return testResult.size() > 0;
        } else {
            return false;
        }

    }

    @Override
    public String asRdfPathExpression() {
        return path.asRdfPathExpression();
    }
}
