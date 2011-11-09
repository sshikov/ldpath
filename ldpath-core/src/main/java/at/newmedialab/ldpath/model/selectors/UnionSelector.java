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
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Builds the union of two node selectors. Will eliminate duplicates.
 * <p/>
 * User: sschaffe
 */
public class UnionSelector implements NodeSelector {

	private NodeSelector left;
	private NodeSelector right;

	public UnionSelector(NodeSelector left, NodeSelector right) {
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
	public Collection<KiWiNode> select(TripleStore tripleStore, KiWiNode context) {
		Set<KiWiNode> result = new HashSet<KiWiNode>();
		result.addAll(left.select(tripleStore,context));
		result.addAll(right.select(tripleStore,context));
		return result;
	}

	@Override
	public String asRdfPathExpression() {
		return String.format("(%s | %s)", left.asRdfPathExpression(), right.asRdfPathExpression());
	}
}
