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

package at.newmedialab.ldpath.model.functions;

import at.newmedialab.lmf.search.rdfpath.model.transformers.StringTransformer;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiStringLiteral;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A node function concatenating a list of nodes interpreted as strings.
 * <p/>
 * User: sschaffe
 */
public class ConcatenateFunction implements SelectorFunction {

    private static StringTransformer transformer = new StringTransformer();

    /**
     * Concatenate the String-Representation of all provided arguments.
     * 
     * @param args
     *            the argument list, a series of {@link java.util.Collection}
     * @return
     */
    @Override
    public Set<KiWiStringLiteral> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        StringBuilder result = new StringBuilder();
        for (Collection<? extends KiWiNode> nodes : args) {
            if (nodes.size() == 0) {
                return Collections.emptySet();
            }
            for(KiWiNode n : nodes) {
                result.append(transformer.transform(n, tripleStore));
            }
        }

        return Collections.singleton(new KiWiStringLiteral(result.toString()));
    }

    @Override
    public String asRdfPathExpression() {
        return "concat";
    }
}
