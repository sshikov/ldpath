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

package at.newmedialab.ldpath.model.fields;

import at.newmedialab.ldpath.api.selectors.NodeSelector;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import org.openrdf.model.Value;
import org.openrdf.repository.RepositoryConnection;

import java.util.Collection;
import java.util.Map;

/**
 * A field mapping maps a field name to a node selection and transforms it into the appropriate type.
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class FieldMapping<T> {

    /**
     * The name of the field in the search index
     */
    private String fieldName;

    /**
     * The type of the field in the search index
     */
    private String fieldType;

    /**
     * The selector to use for selecting nodes
     */
    private NodeSelector selector;

    /**
     * The transformer to use for generating values
     */
    private NodeTransformer<T> transformer;

    /**
     * Additional config params for the (solr) field.
     */
    private Map<String, String> fieldConfig;

    public FieldMapping() {
    }

    public FieldMapping(String fieldName, String fieldType, NodeSelector selector, NodeTransformer<T> transformer, Map<String, String> fieldConfig) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.selector = selector;
        this.transformer = transformer;
        this.fieldConfig = fieldConfig;
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getFieldType() {
        return fieldType;
    }

    public NodeSelector getSelector() {
        return selector;
    }

    public void setSelector(NodeSelector selector) {
        this.selector = selector;
    }

    public NodeTransformer<T> getTransformer() {
        return transformer;
    }

    public void setTransformer(NodeTransformer<T> transformer) {
        this.transformer = transformer;
    }

    public Map<String, String> getFieldConfig() {
        return fieldConfig;
    }

    public void setFieldConfig(Map<String, String> fieldConfig) {
        this.fieldConfig = fieldConfig;
    }

    /**
     * Get the values of this mapping for the context node passed as argument, transformed into the
     * datatype generated by the transformer.
     *
     * @param tripleStore
     * @param context
     * @return
     */
    public Collection<T> getValues(final RepositoryConnection tripleStore, final Value context) {
        Function<Value,T> function = new Function<Value, T>() {
            @Override
            public T apply(Value input) {
                return transformer.transform(input, tripleStore);
            }
        };

        return Collections2.transform(selector.select(tripleStore,context),function);
    }

    public String asRdfPathExpression() {
        StringBuilder fc = new StringBuilder();
        if (fieldConfig != null) {
            fc.append("(");
            boolean first = true;
            for (Map.Entry<String, String> e : fieldConfig.entrySet()) {
                if (!first) {
                    fc.append(", ");
                }
                fc.append(e.getKey()).append("=\"").append(e.getValue()).append("\"");
                first = false;
            }
            fc.append(")");
        }
        return String.format("%s = %s :: <%s>%s ;", fieldName, selector.getPathExpression(backend), fieldType, fc.toString());
    }

}
