/*
 * Copyright (c) 2011, Salzburg NewMediaLab
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the KiWi Project nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package at.newmedialab.ldpath.model.selectors;

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
