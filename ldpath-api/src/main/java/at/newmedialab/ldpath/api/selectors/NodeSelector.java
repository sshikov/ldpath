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

package at.newmedialab.ldpath.api.selectors;

import at.newmedialab.ldpath.api.backend.RDFBackend;

import java.util.Collection;

/**
 * A node selector takes as argument a KiWiNode and returns a collection of selected
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public interface NodeSelector<Node> {

	/**
	 * Apply the selector to the context node passed as argument and return the collection
	 * of selected nodes in appropriate order.
	 *
	 * @param context the node where to start the selection
	 * @return the collection of selected nodes
	 */
	public Collection<Node> select(RDFBackend<Node> backend, Node context);

    /**
     * Return the name of the NodeSelector for registration in the selector registry
     *
     * @return
     * @param backend
     */
    public String getPathExpression(RDFBackend<Node> backend);

}
