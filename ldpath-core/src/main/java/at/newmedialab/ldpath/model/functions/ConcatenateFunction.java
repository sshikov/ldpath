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

package at.newmedialab.ldpath.model.functions;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.functions.SelectorFunction;
import at.newmedialab.ldpath.model.transformers.StringTransformer;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A node function concatenating a list of nodes interpreted as strings.
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class ConcatenateFunction<Node> implements SelectorFunction<Node> {

    private static StringTransformer transformer = new StringTransformer();

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    @Override
    public Collection<Node> apply(RDFBackend<Node> rdfBackend, Collection<Node>... args) throws IllegalArgumentException {
        List<Node> nodes = at.newmedialab.ldpath.util.Collections.concat(args);

        StringBuilder result = new StringBuilder();
        for (Node node : nodes) {
            result.append(transformer.transform(rdfBackend,node));
        }

        return Collections.singleton(rdfBackend.createLiteral(result.toString()));
    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        return "concat";
    }
}
