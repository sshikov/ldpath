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
import at.newmedialab.ldpath.util.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class RemoveXmlTagsFunction<Node> implements SelectorFunction<Node> {

    private static StringTransformer transformer = new StringTransformer();

    private static Pattern XML_TAG = Pattern.compile("<(\"[^\"]*\"|'[^']*'|[^>])*>", Pattern.MULTILINE);

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
        List<Node> nodes = Collections.concat(args);

        List<Node> result = new ArrayList<Node>(nodes.size());
        for (Node n : nodes) {
            result.add(rdfBackend.createLiteral(doFilter(transformer.transform(rdfBackend, n))));
        }

        return result;
    }

    private String doFilter(String in) {
        return XML_TAG.matcher(in).replaceAll("");
    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        return "removeTags";

    }
}
