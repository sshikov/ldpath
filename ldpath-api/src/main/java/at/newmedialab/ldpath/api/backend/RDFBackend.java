/*
 * Copyright (c) 2012 Salzburg Research.
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

package at.newmedialab.ldpath.api.backend;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * A generic API for RDF models and triple stores; provides the testing and navigation functions needed for LDPath.
 * Implementations exist for Sesame, LMF, Jena and Clerezza. The API is somewhat typesafe by making use of a generic
 * for nodes.
 * <p/>
 * Author: Sebastian Schaffert
 */
public interface RDFBackend<Node> {


    /**
     * Return true if the underlying backend supports the parallel execution of queries.
     *
     * @return
     */
    public boolean supportsThreading();


    /**
     * In case the backend supports threading, this method should return the ExecutorService representing the
     * thread pool. LDPath lets the backend manage the thread pool to avoid excessive threading.
     * @return
     */
    public ThreadPoolExecutor getThreadPool();

    /**
     * Test whether the node passed as argument is a literal
     * @param n the node to check
     * @return true if the node is a literal
     */
    public boolean isLiteral(Node n);

    /**
     * Test whether the node passed as argument is a URI
     * @param n the node to check
     * @return true if the node is a URI
     */
    public boolean isURI(Node n);

    /**
     * Test whether the node passed as argument is a blank node
     * @param n the node to check
     * @return true if the node is a blank node
     */
    public boolean isBlank(Node n);

    /**
     * Return the language of the literal node passed as argument.
     *
     * @param n the literal node for which to return the language
     * @return a Locale representing the language of the literal, or null if the literal node has no language
     * @throws IllegalArgumentException in case the node is no literal
     */
    public Locale getLiteralLanguage(Node n);


    /**
     * Return the URI of the type of the literal node passed as argument.
     *
     * @param n the literal node for which to return the typer
     * @return a URI representing the type of the literal content, or null if the literal is untyped
     * @throws IllegalArgumentException in case the node is no literal
     */
    public URI getLiteralType(Node n);



    /**
     * Create a literal node with the content passed as argument
     * @param content  string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    public Node createLiteral(String content);

    /**
     * Create a literal node with the content passed as argument
     * @param content  string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    public Node createLiteral(String content, Locale language, URI type);

    /**
     * Create a URI mode with the URI passed as argument
     * @param uri  URI of the resource to create
     * @return a URI node using the model used by this backend
     */
    public Node createURI(String uri);


    /**
     * Return the lexial representation of a node. For a literal, this will be the content, for a URI node it will be the
     * URI itself, and for a blank node it will be the identifier of the node.
     * @param node
     * @return
     */
    public String stringValue(Node node);

    /**
     * Return the double value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as double, and an
     * IllegalArgumentException, indicating that the passed node is not a literal
     *
     * @param node the literal node for which to return the double value
     * @return double value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as double value
     * @throws ArithmeticException in case the literal cannot be represented as double value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Double doubleValue(Node node);


    /**
     * Return the long value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as long, and an
     * IllegalArgumentException, indicating that the passed node is not a literal
     *
     * @param node the literal node for which to return the long value
     * @return long value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as long value
     * @throws ArithmeticException in case the literal cannot be represented as long value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Long longValue(Node node);

    
    /**
     * Return the boolean value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. 
     * TODO: Define:<ul>
     * <li> Do we also support '0' '1', 'yes', 'no'; whats about case insensitive 
     *      such as TRUE, False
     * <li> should we throw an RuntimeException of not an boolean value or return
     *      false as {@link Boolean#parseBoolean(String)}
     * </ul>
     * @param node the literal node for which to return the boolean value
     * @return long value of the literal node
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Boolean booleanValue(Node node);

    /**
     * TODO
     * @param node the literal node for which to return the dateTime value
     * @return long value of the literal node
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Date dateTimeValue(Node node);
    
    /**
     * TODO
     * @param node the literal node for which to return the date value
     * @return long value of the literal node
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Date dateValue(Node node);
    
    /**
     * TODO
     * @param node the literal node for which to return the time value
     * @return long value of the literal node
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Date timeValue(Node node);
    
    /**
     * Return the float value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as float, and an
     * IllegalArgumentException, indicating that the passed node is not a literal
     * 
     * @param node the literal node for which to return the float value
     * @return long value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as float value
     * @throws ArithmeticException in case the literal cannot be represented as float value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Float floatValue(Node node);
    
    /**
     * Return the 32bit integer value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as integer, and an
     * IllegalArgumentException, indicating that the passed node is not a literal.
     * <p>
     * Note that this is restricted to 32bit singed integer values as defined by
     * xsd:int and {@link Integer}. For bigger nuber one might want to use
     * xsd:integer represented by {@link BigInteger}.
     * 
     * @param node the literal node for which to return the Integer (xsd:int) value
     * @return long value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as 32 bit integer value
     * @throws ArithmeticException in case the literal cannot be represented as 32 bit integer value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public Integer intValue(Node node);
    
    /**
     * Return the arbitrary length integer value of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as integer, and an
     * IllegalArgumentException, indicating that the passed node is not a literal.
     *
     * @param node the literal node for which to return the {@link BigInteger xsd:integer} value
     * @return long value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as integer value
     * @throws ArithmeticException in case the literal cannot be represented as long value
     * @throws IllegalArgumentException in case the node passed as argument is integer a literal
     */
    public BigInteger integerValue(Node node);
    
    /**
     * Return the decimal number of a literal node. Depending on the backend implementing this method,
     * the value can be retrieved directly or must be parsed from the string representation. The method can throw
     * a NumberFormatException or ArithmeticException indicating that the value cannot be represented as decimal, and an
     * IllegalArgumentException, indicating that the passed node is not a literal.
     *
     * @param node the literal node for which to return the xsd:decimal value
     * @return long value of the literal node
     * @throws NumberFormatException in case the literal cannot be represented as decimal value
     * @throws ArithmeticException in case the literal cannot be represented as decimal value
     * @throws IllegalArgumentException in case the node passed as argument is not a literal
     */
    public BigDecimal decimalValue(Node node);
    
    /**
     * List the objects of triples in the triple store underlying this backend that have the subject and
     * property given as argument.
     *
     * @param subject  the subject of the triples to look for
     * @param property the property of the triples to look for, <code>null</code> is interpreted as wildcard
     * @return all objects of triples with matching subject and property
     */
    public Collection<Node> listObjects(Node subject, Node property);


    /**
     * List the subjects of triples in the triple store underlying this backend that have the object and
     * property given as argument.
     *
     * @param object  the object of the triples to look for
     * @param property the property of the triples to look for, <code>null</code> is interpreted as wildcard
     * @return all subjects of triples with matching object and property
     * @throws UnsupportedOperationException in case reverse selection is not supported (e.g. when querying Linked Data)
     */
    public Collection<Node> listSubjects(Node property, Node object);

}
