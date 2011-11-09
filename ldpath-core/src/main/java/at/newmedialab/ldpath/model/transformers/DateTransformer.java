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

package at.newmedialab.ldpath.model.transformers;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;
import at.newmedialab.ldpath.util.FormatUtils;

import java.text.ParseException;
import java.util.Date;

/**
 * TODO: is this a duplicate of DateTimeTransformer?
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class DateTransformer<Node> implements NodeTransformer<Date,Node> {

    /**
     * Transform the KiWiNode node into the datatype T. In case the node cannot be transformed to
     * the respective datatype, throws an IllegalArgumentException that needs to be caught by the class
     * carrying out the transformation.
     *
     * @param node
     * @return
     */
    @Override
    public Date transform(RDFBackend<Node> rdfBackend, Node node) throws IllegalArgumentException {
        if(rdfBackend.isLiteral(node)) {
            try {
                return FormatUtils.ISO8601FORMAT_DATE.parse( rdfBackend.stringValue(node) );
            } catch (ParseException ex) {
                throw new IllegalArgumentException("could not parse date string, it is not in ISO8601 format");
            }

        } else {
            throw new IllegalArgumentException("could not parse node, because it is not a literal");
        }
    }
}
