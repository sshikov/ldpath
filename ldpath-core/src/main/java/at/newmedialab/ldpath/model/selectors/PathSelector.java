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
import java.util.concurrent.Future;

/**
 * Traverse a path by following several edges in the RDF graph. Each step is separated by a "/".
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class PathSelector<Node> implements NodeSelector<Node> {

    private NodeSelector left;
    private NodeSelector right;

    public PathSelector(NodeSelector left, NodeSelector right) {
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
    public Collection<Node> select(final RDFBackend<Node> rdfBackend, Node context) {
        Collection<Node> nodesLeft = left.select(rdfBackend,context);
        final Set<Node> result = new HashSet<Node>();

        if(rdfBackend.supportsThreading()) {
            // start thread pool of size 4 and schedule each subselection in a separate thread
            ExecutorService workers = rdfBackend.getThreadPool();
            Set<Future> futures = new HashSet<Future>();
            for(final Node n : nodesLeft) {
                futures.add(workers.submit(new Runnable() {
                    @Override
                    public void run() {
                        result.addAll(right.select(rdfBackend,n));
                    }
                }));
            }
            // wait for thread pool to finish execution
            for(Future future : futures) {
                try {
                    future.get();
                } catch (Exception e) { }
            }
        } else {
            for(Node n : nodesLeft) {
                result.addAll(right.select(rdfBackend,n));
            }
        }
        return result;
    }

    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        return String.format("%s / %s", left.getPathExpression(backend), right.getPathExpression(backend));
    }

    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     */
    @Override
    public String getName(RDFBackend<Node> nodeRDFBackend) {
        return left.getName(nodeRDFBackend);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PathSelector that = (PathSelector) o;

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
