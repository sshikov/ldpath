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
import com.google.common.collect.ImmutableList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Perform a reverse navigation step over the property wrapped by this selector
 *
 * @param <Node>
 */
public class ReversePropertySelector<Node> implements NodeSelector<Node> {

	private final Node property;

	public ReversePropertySelector(Node property) {
		this.property = property;
	}

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
    public Collection<Node> select(RDFBackend<Node> rdfBackend, Node context, List<Node> path, Map<Node, List<Node>> resultPaths) {
        if(rdfBackend.isURI(context) || rdfBackend.isBlank(context)) {
            if(path != null && resultPaths != null) {
                Collection<Node> results = rdfBackend.listSubjects(context, property);
                for(Node n :results) {
                    resultPaths.put(n, new ImmutableList.Builder<Node>().addAll(path).add(context).add(n).build());
                }
                return results;
            } else {
			    return rdfBackend.listSubjects(property, context);
            }
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public String getPathExpression(RDFBackend<Node> backend) {
		if (property != null) {
			return String.format("^<%s>", backend.stringValue(property));
		} else {
			return "^*";
		}
	}

    /**
     * Return a name for this selector to be used as the name for the whole path if not explicitly
     * specified. In complex selector expressions, this is typically delegated to the first
     * occurrence of an atomic selector.
     */
    @Override
    public String getName(RDFBackend<Node> nodeRDFBackend) {
        return nodeRDFBackend.stringValue(property);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        @SuppressWarnings("rawtypes")
		ReversePropertySelector that = (ReversePropertySelector) o;

        if (property != null ? !property.equals(that.property) : that.property != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return property != null ? property.hashCode() : 0;
    }
}
