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

package at.newmedialab.ldpath.api.functions;

import at.newmedialab.ldpath.api.backend.RDFBackend;

import java.util.Collection;

/**
 * A function applied to nodes.
 *
 * @param <T> the return type of the function
 * @param <Node> the node type of the function
 * <p/>
 * User: sschaffe
 */
public interface NodeFunction<T,Node> {

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    public T apply(RDFBackend<Node> backend, Collection<Node>... args) throws IllegalArgumentException;

    /**
     * Return the name of the NodeFunction for registration in the function registry
     * 
     * @return
     * @param backend
     */
    public String getPathExpression(RDFBackend<Node> backend);
}
