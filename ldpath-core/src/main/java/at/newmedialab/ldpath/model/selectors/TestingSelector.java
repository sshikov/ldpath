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

package at.newmedialab.ldpath.model.selectors;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.api.tests.NodeTest;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Collection;
import java.util.Collections;

/**
 * A node selector that wraps a node test around the selection and delegates the selection to another selector.
 * The result set will be filtered based on the node test.
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class TestingSelector<Node> implements NodeSelector<Node> {

    private NodeSelector<Node> delegate;
    private NodeTest<Node> test;


    public TestingSelector(NodeSelector<Node> delegate, NodeTest<Node> test) {
        this.delegate = delegate;
        this.test = test;
    }

    /**
     * Apply the selector to the context node passed as argument and return the collection
     * of selected nodes in appropriate order.
     *
     * @param context the node where to start the selection
     * @return the collection of selected nodes
     */
    @Override
    public Collection<Node> select(final RDFBackend<Node> rdfBackend, final Node context) {
        Predicate<Node> predicate = new Predicate<Node>() {
            @Override
            public boolean apply(Node input) {
                return test.apply(rdfBackend, context, Collections.singleton(input));
            }
        };

        return Collections2.filter(delegate.select(rdfBackend,context),predicate);
    }

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        return String.format("%s[%s]", delegate.getPathExpression(rdfBackend), test.getPathExpression(rdfBackend));
    }

    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     */
    @Override
    public String getName(RDFBackend<Node> nodeRDFBackend) {
        return delegate.getName(nodeRDFBackend);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestingSelector that = (TestingSelector) o;

        if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;
        if (test != null ? !test.equals(that.test) : that.test != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = delegate != null ? delegate.hashCode() : 0;
        result = 31 * result + (test != null ? test.hashCode() : 0);
        return result;
    }
}
