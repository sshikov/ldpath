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
import kiwi.core.util.KiWiCollections;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Function to clean up HTML and remove all script and style elements from the content.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class CleanHtmlFunction implements SelectorFunction {

    private HtmlCleaner cleaner;

    private static StringTransformer transformer = new StringTransformer();

    private Logger log = LoggerFactory.getLogger(CleanHtmlFunction.class);

    public CleanHtmlFunction() {
        this.cleaner = new HtmlCleaner();
        CleanerProperties p = cleaner.getProperties();
        p.setOmitComments(true);
        p.setTranslateSpecialEntities(true);
        p.setTransResCharsToNCR(true);

        // remove all tags that contain uninteresting content
        p.setPruneTags("style,script,form,object,audio,video");
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
        List<? extends KiWiNode> nodes = KiWiCollections.concat(args);
        List<KiWiStringLiteral> result = new ArrayList<KiWiStringLiteral>(nodes.size());

        for(KiWiNode node : nodes) {
            TagNode tagNode = cleaner.clean(transformer.transform(node,tripleStore));
            try {
                result.add(new KiWiStringLiteral(new CompactXmlSerializer(cleaner.getProperties()).getAsString(tagNode)));
            } catch (IOException e) {
                log.warn("I/O error while serializing to string",e);
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
        return "cleanHtml";
    }
}
