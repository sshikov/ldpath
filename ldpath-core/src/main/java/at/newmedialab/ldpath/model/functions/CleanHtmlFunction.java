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
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.CompactXmlSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Function to clean up HTML and remove all script and style elements from the content.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class CleanHtmlFunction<Node> implements SelectorFunction<Node> {

    private HtmlCleaner cleaner;

    private final StringTransformer<Node> transformer = new StringTransformer<Node>();

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
    public Collection<Node> apply(RDFBackend<Node> backend, Node context, Collection<Node>... args) throws IllegalArgumentException {
        Iterator<Node> it;
        if(args.length < 1){
            log.debug("clean HTML from context {}",context);
            it = java.util.Collections.singleton(context).iterator();
        } else {
            log.debug("clean HTML from parameters");
            it = Collections.iterator(args);
        }
        List<Node> result = new ArrayList<Node>();
        while(it.hasNext()) {
            Node node = it.next();
            TagNode tagNode = cleaner.clean(transformer.transform(backend,node));
            try {
                result.add(backend.createLiteral(new CompactXmlSerializer(cleaner.getProperties()).getAsString(tagNode)));
            } catch (IOException e) {
                log.warn("I/O error while serializing to string",e);
            }
        }
        return result;
    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @return
     * @param backend
     */
    @Override
    public String getPathExpression(RDFBackend<Node> backend) {
        return "cleanHtml";
    }
}
