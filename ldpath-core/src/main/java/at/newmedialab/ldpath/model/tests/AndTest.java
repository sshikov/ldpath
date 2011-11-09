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
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.List;

/**
 * Tests the conjunction of two tests.
 * <p/>
 * User: sschaffe
 */
public class AndTest implements NodeTest {

    private NodeTest left;
    private NodeTest right;


    public AndTest(NodeTest left, NodeTest right) {
        this.left = left;
        this.right = right;
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
    public Boolean apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> nodes) throws IllegalArgumentException {
        return left.apply(tripleStore, nodes) && right.apply(tripleStore, nodes);
    }

    @Override
    public String asRdfPathExpression() {
        return String.format("%s & %s", left.asRdfPathExpression(), right.asRdfPathExpression());
    }
}
