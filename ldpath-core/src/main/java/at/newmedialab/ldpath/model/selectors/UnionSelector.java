/*
 * Copyright (c) 2012 Salzburg Research.
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Builds the union of two node selectors. Will eliminate duplicates.
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class UnionSelector<Node> implements NodeSelector<Node> {

    private NodeSelector<Node> left;
    private NodeSelector<Node> right;

    public UnionSelector(NodeSelector<Node> left, NodeSelector<Node> right) {
        this.left = left;
        this.right = right;
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
        final Set<Node> result = new HashSet<Node>();

        if(rdfBackend.supportsThreading()) {
            // start thread pool of size 2 and schedule each subselection in a separate thread
            ExecutorService workers = Executors.newFixedThreadPool(4);
            workers.submit(new Runnable() {
                @Override
                public void run() {
                    result.addAll(left.select(rdfBackend,context));
                }
            });
            workers.submit(new Runnable() {
                @Override
                public void run() {
                    result.addAll(right.select(rdfBackend,context));
                }
            });
            // wait for thread pool to terminate
            workers.shutdown();
        } else {
            result.addAll(left.select(rdfBackend,context));
            result.addAll(right.select(rdfBackend,context));
        }
        return result;
    }

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        return String.format("(%s | %s)", left.getPathExpression(rdfBackend), right.getPathExpression(rdfBackend));
    }

    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     */
    @Override
    public String getName(RDFBackend<Node> nodeRDFBackend) {
        throw new UnsupportedOperationException("cannot use unions in unnamed field definitions because the name is ambiguous");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UnionSelector that = (UnionSelector) o;

        if (left != null ? !left.equals(that.left) : that.left != null) return false;
        if (right != null ? !right.equals(that.right) : that.right != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + (right != null ? right.hashCode() : 0);
        return result;
    }
}
