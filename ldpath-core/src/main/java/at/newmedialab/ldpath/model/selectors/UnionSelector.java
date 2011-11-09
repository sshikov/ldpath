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

/**
 * Builds the union of two node selectors. Will eliminate duplicates.
 * <p/>
 * User: sschaffe
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
    public Collection<Node> select(RDFBackend<Node> rdfBackend, Node context) {
		Set<Node> result = new HashSet<Node>();
		result.addAll(left.select(rdfBackend,context));
		result.addAll(right.select(rdfBackend,context));
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
}
