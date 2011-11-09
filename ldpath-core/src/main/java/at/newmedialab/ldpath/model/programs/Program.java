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

package at.newmedialab.ldpath.model.programs;

import at.newmedialab.lmf.search.rdfpath.model.fields.FieldMapping;
import at.newmedialab.lmf.search.rdfpath.model.tests.NodeTest;

import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Add file description here!
 * <p/>
 * User: sschaffe
 */
public class Program {

    public static final Map<String, String> DEFAULT_NAMESPACES;
    static {
        HashMap<String, String> defNS = new HashMap<String, String>();
        defNS.put("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        defNS.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        defNS.put("owl", "http://www.w3.org/2002/07/owl#");
        defNS.put("skos", "http://www.w3.org/2004/02/skos/core#");
        defNS.put("dc", "http://purl.org/dc/elements/1.1/");
        defNS.put("xsd", "http://www.w3.org/2001/XMLSchema#");
        defNS.put("lmf", "http://www.newmedialab.at/lmf/types/1.0/");
        defNS.put("fn", "http://www.newmedialab.at/lmf/functions/1.0/");
        DEFAULT_NAMESPACES = Collections.unmodifiableMap(defNS);
    }

    public static final String DOCUMENT_BOOST_TYPE = "http://www.w3.org/2001/XMLSchema#float";

    /**
     * A map mapping from namespace prefix to namespace URI
     */
    private Map<String, String> namespaces;

    /**
     * An (optional) filter to use for checking which resources should be
     * indexed.
     */
    private NodeTest filter;

    /**
     * An (optional) selector to resolve a document boost factor.
     */
    private FieldMapping<Float> booster;

    /**
     * The field mappings contained in this program.
     */
    private Set<FieldMapping<?>> fields;

    public Program() {
        namespaces = new HashMap<String, String>();
        fields = new HashSet<FieldMapping<?>>();
    }

    public void addNamespace(String prefix, String uri) {
        namespaces.put(prefix, uri);
    }

    public void addMapping(FieldMapping<?> mapping) {
        fields.add(mapping);
    }

    public Set<FieldMapping<?>> getFields() {
        return fields;
    }

    public void setFields(Set<FieldMapping<?>> fields) {
        this.fields = fields;
    }

    public NodeTest getFilter() {
        return filter;
    }

    public void setFilter(NodeTest filter) {
        this.filter = filter;
    }

    public FieldMapping<Float> getBooster() {
        return booster;
    }

    public void setBooster(FieldMapping<Float> boost) {
        this.booster = boost;
    }

    public Map<String, String> getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Map<String, String> namespaces) {
        this.namespaces = namespaces;
    }

    public String asRdfPathExpression() {
        StringBuilder sb = new StringBuilder();
        // Filter (?)
        if (filter != null) {
            sb.append(String.format("@filter %s ;%n", filter.asRdfPathExpression()));
        }

        // Booster (?)
        if (booster != null) {
            sb.append(String.format("@boost %s ;%n", booster.getSelector().asRdfPathExpression()));
        }

        // Field-Definitions
        for (FieldMapping<?> field : fields) {
            sb.append(String.format("  %s%n", field.asRdfPathExpression()));
        }
        String progWithoutNamespace = sb.toString();

        // Definded Namespaces (reverse sorted, to give longer prefixes precedence over shorter)
        final StringBuilder prefixes = new StringBuilder();
        final TreeSet<Entry<String, String>> sortedNamespaces = new TreeSet<Entry<String,String>>(new Comparator<Entry<String, String>>() {
            @Override
            public int compare(Entry<String, String> e1, Entry<String, String> e2) {
                return e2.getValue().compareTo(e1.getValue());
            }
        });
        sortedNamespaces.addAll(namespaces.entrySet());
        for (Entry<String, String> ns : sortedNamespaces) {
            progWithoutNamespace = progWithoutNamespace.replaceAll("<" + Pattern.quote(ns.getValue()) + "([^>]*)>", Matcher.quoteReplacement(ns.getKey())
                    + ":$1");
            prefixes.append(String.format("@prefix %s : <%s> ;%n", ns.getKey(), ns.getValue()));
        }

        // Also resolve default namespaces...
        for (Entry<String, String> ns : DEFAULT_NAMESPACES.entrySet()) {
            if (!namespaces.containsKey(ns.getKey())) {
                progWithoutNamespace = progWithoutNamespace.replaceAll("<" + Pattern.quote(ns.getValue()) + "([^>]*)>",
                        Matcher.quoteReplacement(ns.getKey()) + ":$1");
            }
        }

        return prefixes.append(progWithoutNamespace).toString();
    }
}
