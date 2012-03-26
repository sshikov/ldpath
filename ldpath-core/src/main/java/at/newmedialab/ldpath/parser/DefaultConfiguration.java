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

package at.newmedialab.ldpath.parser;

import at.newmedialab.ldpath.api.functions.SelectorFunction;
import at.newmedialab.ldpath.model.Constants;
import at.newmedialab.ldpath.model.functions.*;
import at.newmedialab.ldpath.model.transformers.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class DefaultConfiguration<Node> extends Configuration<Node> {

    public static final Map<String, String> DEFAULT_NAMESPACES;
    static {
        HashMap<String, String> defNS = new HashMap<String, String>();
        defNS.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        defNS.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        defNS.put("owl", "http://www.w3.org/2002/07/owl#");
        defNS.put("skos", "http://www.w3.org/2004/02/skos/core#");
        defNS.put("foaf", "http://xmlns.com/foaf/0.1/");
        defNS.put("dc", "http://purl.org/dc/elements/1.1/");
        defNS.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        defNS.put("lmf", "http://www.newmedialab.at/lmf/types/1.0/");
        defNS.put("fn", "http://www.newmedialab.at/lmf/functions/1.0/");
        DEFAULT_NAMESPACES = Collections.unmodifiableMap(defNS);
    }


    public DefaultConfiguration() {
        super();
        addDefaultNamespaces();
        addDefaultTransformers();
        addDefaultFunctions();
    }


    private void addDefaultNamespaces() {
        namespaces.putAll(DEFAULT_NAMESPACES);


    }

    private void addDefaultTransformers() {
        addTransformer(Constants.NS_XSD + "decimal", new BigDecimalTransformer<Node>());
        addTransformer(Constants.NS_XSD + "integer", new BigIntegerTransformer<Node>());
        addTransformer(Constants.NS_XSD + "long", new LongTransformer<Node>());
        addTransformer(Constants.NS_XSD + "int", new IntTransformer<Node>());
        addTransformer(Constants.NS_XSD + "short", new ShortTransformer<Node>());
        addTransformer(Constants.NS_XSD + "byte", new ByteTransformer<Node>());
        addTransformer(Constants.NS_XSD + "double", new DoubleTransformer<Node>());
        addTransformer(Constants.NS_XSD + "float", new FloatTransformer<Node>());
        addTransformer(Constants.NS_XSD + "dateTime", new DateTimeTransformer<Node>());
        addTransformer(Constants.NS_XSD + "date", new DateTransformer<Node>());
        addTransformer(Constants.NS_XSD + "time", new TimeTransformer<Node>());
        addTransformer(Constants.NS_XSD + "boolean", new BooleanTransformer<Node>());
        addTransformer(Constants.NS_XSD + "anyURI", new StringTransformer<Node>());
        addTransformer(Constants.NS_XSD + "string", new StringTransformer<Node>());
        addTransformer(Constants.NS_XSD + "duration", new DurationTransformer<Node>());

    }

    private void addDefaultFunctions() {
        addFunction(new ConcatenateFunction<Node>());
        addFunction(new FirstFunction<Node>());
        addFunction(new LastFunction<Node>());
        addFunction(new XPathFunction<Node>());
        addFunction(new RemoveXmlTagsFunction<Node>());
        addFunction(new CleanHtmlFunction<Node>());
        addFunction(new SortFunction<Node>());

    }

    private void addFunction(SelectorFunction<Node> function) {
        addFunction(Constants.NS_LMF_FUNCS + function.getPathExpression(null), function);
    }
}
