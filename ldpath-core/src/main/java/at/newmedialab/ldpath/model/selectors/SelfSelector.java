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

/**
 * 
 */
package at.newmedialab.ldpath.model.selectors;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.selectors.NodeSelector;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Jakob Frank <jakob.frank@salzburgresearch.at>
 * 
 */
public class SelfSelector<Node> implements NodeSelector<Node> {


    /**
     * Apply the selector to the context node passed as argument and return the collection
     * of selected nodes in appropriate order.
     *
     * @param context     the node where to start the selection
     * @param path        the path leading to but not including the context node in the current evaluation of LDPath; may be null,
     *                    in which case path tracking is disabled
     * @param resultPaths a map where each of the result nodes maps to a path leading to the result node in the LDPath evaluation;
     *                    if null, path tracking is disabled and the path argument is ignored
     * @return the collection of selected nodes
     */
    @Override
    public Collection<Node> select(RDFBackend<Node> nodeRDFBackend, Node context, List<Node> path, Map<Node, List<Node>> resultPaths) {
        return Collections.singleton(context);
    }

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        return ".";
    }

    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     */
    @Override
    public String getName(RDFBackend<Node> nodeRDFBackend) {
        throw new UnsupportedOperationException("cannot use self selections in unnamed field definitions because the name is ambiguous");
    }



}
