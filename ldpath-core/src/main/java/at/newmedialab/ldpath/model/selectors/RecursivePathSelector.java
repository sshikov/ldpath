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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RecursivePathSelector<Node> implements NodeSelector<Node> {

	private final NodeSelector<Node> delegate;
	private final int minRecursions, maxRecursions;

	public RecursivePathSelector(NodeSelector<Node> delegate, int min, int max) {
		this.delegate = delegate;
		minRecursions = min;
		maxRecursions = max;
	}

    /**
     * Apply the selector to the context node passed as argument and return the collection
     * of selected nodes in appropriate order.
     *
     * @param context the node where to start the selection
     * @return the collection of selected nodes
     */
    @Override
    public Collection<Node> select(RDFBackend<Node> rdfBackend, Node context) {
		Set<Node> result = new HashSet<Node>();

		if (minRecursions <= 0) {
			result.add(context);
		}
		subSelect(context, 0, rdfBackend, result);

		return result;
	}

	private void subSelect(Node currentContext, int depth, RDFBackend<Node> rdfBackend, Set<Node> resultSet) {
		Collection<Node> nodesLeft = delegate.select(rdfBackend, currentContext);
		for (Node n : nodesLeft) {
			if (!resultSet.contains(n)) {
				if (depth >= minRecursions){
					resultSet.add(n);
				}
				if (depth < maxRecursions) {
					subSelect(n, depth +1, rdfBackend, resultSet);
				}
			}
		}
	}

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
    	if (maxRecursions != Integer.MAX_VALUE) {
    		if (minRecursions <= 0) {
    	    	return String.format("(%s){,%d}", delegate.getPathExpression(rdfBackend), maxRecursions);
    		} else {
    	    	return String.format("(%s){%d,%d}", delegate.getPathExpression(rdfBackend), minRecursions, maxRecursions);
    		}
    	} else {
    		if (minRecursions <= 0) {
    	    	return String.format("(%s)*", delegate.getPathExpression(rdfBackend));
    		} else if (minRecursions == 1) {
    	    	return String.format("(%s)+", delegate.getPathExpression(rdfBackend));
    		} else {
    	    	return String.format("(%s){%d,}", delegate.getPathExpression(rdfBackend), minRecursions);
    		}
    	}
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

    /**
     * <code>(delegate)*</code>
     * @param delegate the delegate
     */
    public static <N> RecursivePathSelector<N> getPathSelectorStared(NodeSelector<N> delegate) {
    	return new RecursivePathSelector<N>(delegate, 0, Integer.MAX_VALUE);
    }

    /**
     * <code>(delegate)+</code>
     * @param delegate the delegate
     */
    public static <N> RecursivePathSelector<N> getPathSelectorPlused(NodeSelector<N> delegate) {
    	return new RecursivePathSelector<N>(delegate, 1, Integer.MAX_VALUE);
    }
    
    /**
     * <code>(delegate){m,}</code>
     * @param delegate the delegate
     * @param minBound <code>m</code>
     */
    public static <N> RecursivePathSelector<N> getPathSelectorMinBound(NodeSelector<N> delegate, int minBound) {
    	return new RecursivePathSelector<N>(delegate, minBound, Integer.MAX_VALUE);
    }

    /**
     * <code>(delegate){,n}</code>
     * @param delegate the delegate
     * @param maxBound <code>n</code>
     */
    public static <N> RecursivePathSelector<N> getPathSelectorMaxBound(NodeSelector<N> delegate, int maxBound) {
    	return new RecursivePathSelector<N>(delegate, 0, maxBound);
    }

    /**
     * <code>(delegate){m,n}</code>
     * @param delegate the delegate
     * @param minBound <code>m</code>
     * @param maxBound <code>n</code>
     */
    public static <N> RecursivePathSelector<N> getPathSelectorMinMaxBound(NodeSelector<N> delegate, int minBound, int maxBound) {
    	return new RecursivePathSelector<N>(delegate, minBound, maxBound);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecursivePathSelector<Node> that = (RecursivePathSelector<Node>) o;

        if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }
}
