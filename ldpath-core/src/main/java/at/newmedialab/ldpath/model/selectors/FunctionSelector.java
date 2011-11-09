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

import at.newmedialab.lmf.search.rdfpath.model.functions.NodeFunction;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * User: sschaffe
 */
public class FunctionSelector implements NodeSelector {

    private List<NodeSelector> selectors;
    private NodeFunction<Collection<KiWiNode>> function;


    public FunctionSelector(NodeFunction<Collection<KiWiNode>> function, List<NodeSelector> selectors) {
        this.function  = function;
        this.selectors = selectors;
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
        ArrayList<Collection<KiWiNode>> args = new ArrayList<Collection<KiWiNode>>();
        for(NodeSelector selector : selectors) {
            Collection<KiWiNode> param = selector.select(tripleStore, context);
            args.add(param);
        }
        return function.apply(tripleStore, args);
    }

    @Override
    public String asRdfPathExpression() {

        final StringBuilder format = new StringBuilder();
        format.append(String.format("fn:%s(", function.asRdfPathExpression()));
        boolean first = true;
        for (NodeSelector ns : selectors) {
            if (!first) {
                format.append(", ");
            }
            format.append(ns.asRdfPathExpression());
            first = false;
        }
        return format.append(")").toString();
    }
}
