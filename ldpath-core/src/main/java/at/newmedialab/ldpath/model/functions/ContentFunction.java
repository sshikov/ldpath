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

import kiwi.core.api.content.ContentService;
import kiwi.core.api.triplestore.TripleStore;
import kiwi.core.model.rdf.KiWiNode;
import kiwi.core.model.rdf.KiWiResource;
import kiwi.core.model.rdf.KiWiStringLiteral;
import kiwi.core.util.KiWiContext;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class ContentFunction implements SelectorFunction {

    private ContentService contentService;

    private String[] allowedTypes = new String[] {
        "text/.*", "application/([a-z]+\\+)?xml", "application/([a-z]+\\+)?json"
    };

    public ContentFunction() {
    }

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a list of KiWiNodes
     * @return
     */
    @Override
    public Collection<? extends KiWiNode> apply(TripleStore tripleStore, List<? extends Collection<KiWiNode>> args) throws IllegalArgumentException {
        List<KiWiNode> result = new LinkedList<KiWiNode>();

        for(Collection<? extends KiWiNode> nodes : args) {
            for(KiWiNode n : nodes) {
                if(n instanceof KiWiResource) {
                    KiWiResource r = (KiWiResource)n;

                    if(contentService == null) {
                        contentService = KiWiContext.getInstance(ContentService.class);
                    }

                    String type = contentService.getContentType(r);

                    for(String allowedType : allowedTypes) {
                        if(type.matches(allowedType)) {
                            byte[] data = contentService.getContentData(r,type);
                            String content = new String(data);
                            result.add(new KiWiStringLiteral(content));
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Convert the {@link at.newmedialab.lmf.search.rdfpath.model.functions.NodeFunction} into a {@link at.newmedialab.lmf.search.rdfpath.parser.RdfPathParser}-compatible
     * expression.
     *
     * @return
     */
    @Override
    public String asRdfPathExpression() {
        return "content";
    }
}
