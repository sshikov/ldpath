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

import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.lmf.search.rdfpath.model.tests.NodeTest;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.Collections;

/**
 * A node selector that wraps a node test around the selection and delegates the selection to another selector.
 * The result set will be filtered based on the node test.
 * <p/>
 * User: sschaffe
 */
public class TestingSelector implements NodeSelector {

    private NodeSelector delegate;
    private NodeTest test;


    public TestingSelector(NodeSelector delegate, NodeTest test) {
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
    public Collection<KiWiNode> select(final TripleStore tripleStore, final KiWiNode context) {
        Predicate<KiWiNode> predicate = new Predicate<KiWiNode>() {
            @Override
            public boolean apply(KiWiNode input) {
                return test.apply(tripleStore, Collections.singletonList(Collections.singleton(input)));
            }
        };

        return Collections2.filter(delegate.select(tripleStore,context),predicate);
    }

    @Override
    public String asRdfPathExpression() {
        return String.format("%s[%s]", delegate.asRdfPathExpression(), test.asRdfPathExpression());
    }
}
