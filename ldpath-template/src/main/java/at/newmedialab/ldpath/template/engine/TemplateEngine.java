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

package at.newmedialab.ldpath.template.engine;

import at.newmedialab.ldpath.LDPath;
import at.newmedialab.ldpath.exception.LDPathParseException;
import freemarker.cache.TemplateLoader;
import freemarker.core.Environment;
import freemarker.template.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class TemplateEngine<Node> {

    private Configuration freemarker;

    private LDPath<Node> ldPath;

    private Node context;

    private Map<String,String> namespaces;

    public TemplateEngine(Node context, LDPath<Node> ldPath) {
        this.context = context;
        this.ldPath  = ldPath;

        this.namespaces = new HashMap<String, String>();

        freemarker = new Configuration();
        freemarker.setObjectWrapper(new DefaultObjectWrapper());

        Map root = new HashMap();
        root.put("namespaces", new NamespaceDirective());
        root.put("ldpath",new LDPathMethod());

    }


    /**
     * Allow setting a different template loader. Custom template loaders can be implemented in addition to
     * those provided by FreeMarker.
     *
     * @param loader
     */
    public void setTemplateLoader(TemplateLoader loader) {
        freemarker.setTemplateLoader(loader);
    }


    /**
     * A class that implements a "namespace" directive for configuring namespaces in this engine. Usage:
     * <@namespaces foaf="http://xmlns.com/foaf/0.1/" sioc="http://rdfs.org/sioc/ns#">
     */
    private class NamespaceDirective implements TemplateDirectiveModel {
        /**
         * Parse key-value pairs passed as parameters to this directive
         *
         * @param env      the current processing environment. Note that you can access
         *                 the output {@link java.io.Writer Writer} by {@link freemarker.core.Environment#getOut()}.
         * @param params   the parameters (if any) passed to the directive as a
         *                 map of key/value pairs where the keys are {@link String}-s and the
         *                 values are {@link freemarker.template.TemplateModel} instances. This is never
         *                 <code>null</code>. If you need to convert the template models to POJOs,
         *                 you can use the utility methods in the {@link freemarker.template.utility.DeepUnwrap} class.
         * @param loopVars an array that corresponds to the "loop variables", in
         *                 the order as they appear in the directive call. ("Loop variables" are out-parameters
         *                 that are available to the nested body of the directive; see in the Manual.)
         *                 You set the loop variables by writing this array. The length of the array gives the
         *                 number of loop-variables that the caller has specified.
         *                 Never <code>null</code>, but can be a zero-length array.
         * @param body     an object that can be used to render the nested content (body) of
         *                 the directive call. If the directive call has no nested content (i.e., it is like
         *                 [@myDirective /] or [@myDirective][/@myDirective]), then this will be
         *                 <code>null</code>.
         * @throws freemarker.template.TemplateException
         *
         * @throws java.io.IOException
         */
        @Override
        public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
            Iterator paramIter = params.entrySet().iterator();
            while (paramIter.hasNext()) {
                Map.Entry ent = (Map.Entry) paramIter.next();

                String paramName = (String) ent.getKey();
                TemplateModel paramValue = (TemplateModel) ent.getValue();

                if(paramValue instanceof TemplateScalarModel) {
                    String uri = ((TemplateScalarModel)paramValue).getAsString();

                    try {
                        URI test = new URI(uri);
                        namespaces.put(paramName,test.toString());
                    } catch (URISyntaxException e) {
                        throw new TemplateModelException("invalid namespace URI '"+uri+"'",e);
                    }
                }
            }
        }
    }


    private class LDPathMethod implements TemplateMethodModel {
        /**
         * Execute a LDPath expression.
         *
         * @param arguments a <tt>List</tt> of <tt>String</tt> objects
         *                  containing the values of the arguments passed to the method.
         * @return the return value of the method, or null. If the returned value
         *         does not implement {@link freemarker.template.TemplateModel}, it will be automatically
         *         wrapped using the {@link freemarker.core.Environment#getObjectWrapper() environment
         *         object wrapper}.
         */
        @Override
        public Object exec(List arguments) throws TemplateModelException {
            if(arguments.size() != 1) {
                throw new TemplateModelException("wrong number of arguments for method call");
            }

            String path = (String)arguments.get(0);

            try {
                return ldPath.pathTransform(context,path,namespaces);
            } catch (LDPathParseException e) {
                throw new TemplateModelException("could not parse path expression '"+path+"'",e);
            }
        }
    }
}
