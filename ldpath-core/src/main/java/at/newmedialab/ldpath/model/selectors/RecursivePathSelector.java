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

	private final NodeSelector delegate;

	public RecursivePathSelector(NodeSelector delegate) {
		this.delegate = delegate;
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

		subSelect(rdfBackend, context, result);

		return result;
	}

	private void subSelect(RDFBackend<Node> rdfBackend, Node currentContext, Set<Node> resultSet) {
		Collection<Node> nodesLeft = delegate.select(rdfBackend, currentContext);
		for (Node n : nodesLeft) {
			if (!resultSet.contains(n)) {
				resultSet.add(n);
				subSelect(rdfBackend, n, resultSet);
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
		return String.format("(%s)+", delegate.getPathExpression(rdfBackend));
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

        RecursivePathSelector that = (RecursivePathSelector) o;

        if (delegate != null ? !delegate.equals(that.delegate) : that.delegate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return delegate != null ? delegate.hashCode() : 0;
    }
}
