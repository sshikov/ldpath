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

import at.newmedialab.ldpath.api.LDPathConstruct;
import at.newmedialab.ldpath.api.backend.RDFBackend;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A node selector takes as argument a KiWiNode and returns a collection of selected
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public interface NodeSelector<Node> extends LDPathConstruct<Node> {

	/**
	 * Apply the selector to the context node passed as argument and return the collection
	 * of selected nodes in appropriate order.
	 *
	 * @param context the node where to start the selection
     * @param path    the path leading to but not including the context node in the current evaluation of LDPath; may be null,
     *                in which case path tracking is disabled
     * @param resultPaths a map where each of the result nodes maps to a path leading to the result node in the LDPath evaluation;
     *                 if null, path tracking is disabled and the path argument is ignored
	 * @return the collection of selected nodes
	 */
	public Collection<Node> select(RDFBackend<Node> backend, Node context, List<Node> path, Map<Node,List<Node>> resultPaths);


    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     * <p/>
     * Implementations can throw UnsupportedOperationException in case returning a name is not reasonable or ambiguous.
     *
     * @throws UnsupportedOperationException in case returning a name is not reasonable
     */
    public String getName(RDFBackend<Node> backend);
}
