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
import at.newmedialab.ldpath.api.tests.NodeTest;

import java.util.Collection;

/**
 * Tests the disjunction of two tests.
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class OrTest<Node> implements ComplexTest<Node> {

    private NodeTest<Node> left;
    private NodeTest<Node> right;

    public OrTest(NodeTest<Node> left, NodeTest<Node> right) {
        this.left = left;
        this.right = right;
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
    public Boolean apply(RDFBackend<Node> rdfBackend, Node context, Collection<Node>... args) throws IllegalArgumentException {
        return left.apply(rdfBackend, context, args) || right.apply(rdfBackend, context, args);
    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        return String.format("%s | %s", left.getPathExpression(rdfBackend), right.getPathExpression(rdfBackend));
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
        return "(left  | right ) :: (Boolean, Boolean) -> Boolean";

    }

    /**
     * A short human-readable description of what the node function does.
     *
     * @return
     */
    @Override
    public String getDescription() {
        return "Tests the disjunction of two tests";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        @SuppressWarnings("rawtypes")
        OrTest orTest = (OrTest) o;

        if (left != null ? !left.equals(orTest.left) : orTest.left != null) return false;
        if (right != null ? !right.equals(orTest.right) : orTest.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
