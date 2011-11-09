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
import at.newmedialab.ldpath.api.functions.NodeFunction;
import at.newmedialab.ldpath.api.selectors.NodeSelector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class FunctionSelector<Node> implements NodeSelector<Node> {

    private List<NodeSelector> selectors;
    private NodeFunction<Collection<Node>,Node> function;


    public FunctionSelector(NodeFunction<Collection<Node>,Node> function, List<NodeSelector> selectors) {
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
    public Collection<Node> select(RDFBackend<Node> rdfBackend, Node context) {
        ArrayList<Collection<Node>> args = new ArrayList<Collection<Node>>();
        for(NodeSelector selector : selectors) {
            Collection<Node> param = selector.select(rdfBackend, context);
            args.add(param);
        }
        return function.apply(rdfBackend, args);
    }

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        final StringBuilder format = new StringBuilder();
        format.append(String.format("fn:%s(", function.getPathExpression(backend)));
        boolean first = true;
        for (NodeSelector ns : selectors) {
            if (!first) {
                format.append(", ");
            }
            format.append(ns.getPathExpression(backend));
            first = false;
        }
        return format.append(")").toString();
    }
}
