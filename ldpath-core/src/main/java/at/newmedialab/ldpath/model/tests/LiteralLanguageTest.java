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

package at.newmedialab.ldpath.model.tests;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.tests.NodeTest;
import at.newmedialab.ldpath.util.Collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Tests if the language of the literal node matches the language configured for the test. If the language of the test
 * is null, only allows literal nodes without language definition.
 *
 * <p/>
 * Author: Sebastian Schaffert <sebastian.schaffert@salzburgresearch.at>
 */
public class LiteralLanguageTest<Node> implements NodeTest<Node> {

    private String lang;


    public LiteralLanguageTest(String lang) {
        this.lang = lang;
    }

    /**
     * Apply the function to the list of nodes passed as arguments and return the result as type T.
     * Throws IllegalArgumentException if the function cannot be applied to the nodes passed as argument
     * or the number of arguments is not correct.
     *
     * @param args a nested list of KiWiNodes
     * @return
     */
    @Override
    public Boolean apply(RDFBackend<Node> rdfBackend, Collection<Node>... args) throws IllegalArgumentException {
        if (args.length != 1 || args[0].isEmpty()) { 
            throw new IllegalArgumentException("language test only takes one parameter");
        }
        if (args[0].size() != 1) {
            throw new IllegalArgumentException("language test can only be applied to a single node");
        }
        Node node = args[0].iterator().next();

        if(rdfBackend.isLiteral(node)) {
            if(lang != null && !lang.toLowerCase().equals("none")) {
                return new Locale(lang).equals(rdfBackend.getLiteralLanguage(node));
            } else {
                return rdfBackend.getLiteralLanguage(node) == null;
            }
        } else {
            return false;
        }

    }

    /**
     * Return the name of the NodeFunction for registration in the function registry
     *
     * @param rdfBackend
     * @return
     */
    @Override
    public String getPathExpression(RDFBackend<Node> rdfBackend) {
        return "@" + lang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        @SuppressWarnings("rawtypes")
        LiteralLanguageTest that = (LiteralLanguageTest) o;

        if (lang != null ? !lang.equals(that.lang) : that.lang != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lang != null ? lang.hashCode() : 0;
    }
}
