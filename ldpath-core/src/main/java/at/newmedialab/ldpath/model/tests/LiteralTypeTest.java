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
import kiwi.core.model.rdf.KiWiLiteral;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * User: sschaffe
 */
public class LiteralTypeTest implements NodeTest {

    private String typeUri;

    public LiteralTypeTest(String typeUri) {
        this.typeUri = typeUri;
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
        if (args.size() != 1) { throw new IllegalArgumentException("literal type test only takes one parameter"); }
        Collection<? extends KiWiNode> nodes = args.get(0);
        if (nodes.size() != 1) {
            throw new IllegalArgumentException("literal type test can only be applied to a single node");
        }

        KiWiNode node = nodes.toArray(new KiWiNode[1])[0];

        if(node.isLiteral()) {
            KiWiLiteral l = (KiWiLiteral)node;

            if(typeUri != null) {
                return typeUri.equals(l.getType());
            } else {
                return typeUri == l.getType();
            }
        } else {
            return false;
        }

    }

    @Override
    public String asRdfPathExpression() {
        return "^^" + typeUri;
    }
}
