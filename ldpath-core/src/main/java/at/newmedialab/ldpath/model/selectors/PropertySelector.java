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
import java.util.Collections;

/**
 * A path definition selecting the value of a property. Either a URI enclosed in <> or a namespace prefix and a
 * local name separated by ":"
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class PropertySelector<Node> implements NodeSelector<Node> {

	private Node property;


	public PropertySelector(Node property) {
		this.property = property;
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
		if(rdfBackend.isURI(context) || rdfBackend.isBlank(context)) {
			return rdfBackend.listObjects(context,property);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public String getPathExpression(RDFBackend<Node> backend) {
		if (property != null) {
			return String.format("<%s>", backend.stringValue(property));
		} else {
			return "*";
		}
	}
}
