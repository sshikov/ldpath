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

package at.newmedialab.ldpath.backend.sesame;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import org.openrdf.model.*;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Generic implementation of a Sesame backend for LDPath. A Sesame repository is passed as argument to the
 * constructor.
 * <p/>
 * Implementatins can either use this class directly or implement their own Sesame-based backend by subclassing
 * and calling the super constructor.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class GenericSesameBackend implements RDFBackend<Value> {

    private static final Logger log = LoggerFactory.getLogger(GenericSesameBackend.class);

    private Repository repository;

    /**
     * Initialise a new sesame backend. Repository needs to be set using setRepository.
     */
    protected GenericSesameBackend() {
    }


    /**
     * Return true if the underlying backend supports the parallel execution of queries.
     *
     * @return
     */
    @Override
    public boolean supportsThreading() {
        return true;
    }

    /**
     * Initialise a new sesame backend using the repository passed as argument.
     *
     * @param repository
     */
    public GenericSesameBackend(Repository repository) {
        this.repository = repository;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }


    /**
     * Test whether the node passed as argument is a literal
     *
     * @param n the node to check
     * @return true if the node is a literal
     */
    @Override
    public boolean isLiteral(Value n) {
        return n instanceof Literal;
    }

    /**
     * Test whether the node passed as argument is a URI
     *
     * @param n the node to check
     * @return true if the node is a URI
     */
    @Override
    public boolean isURI(Value n) {
        return n instanceof org.openrdf.model.URI;
    }

    /**
     * Test whether the node passed as argument is a blank node
     *
     * @param n the node to check
     * @return true if the node is a blank node
     */
    @Override
    public boolean isBlank(Value n) {
        return n instanceof BNode;
    }

    /**
     * Return the language of the literal node passed as argument.
     *
     * @param n the literal node for which to return the language
     * @return a Locale representing the language of the literal, or null if the literal node has no language
     * @throws IllegalArgumentException in case the node is no literal
     */
    @Override
    public Locale getLiteralLanguage(Value n) {
        try {
            if(((Literal)n).getLanguage() != null) {
                return new Locale( ((Literal)n).getLanguage() );
            } else {
                return null;
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+n.stringValue()+" is not a literal" +
                    "but of type "+debugType(n));
        }
    }

    /**
     * Return the URI of the type of the literal node passed as argument.
     *
     * @param n the literal node for which to return the typer
     * @return a URI representing the type of the literal content, or null if the literal is untyped
     * @throws IllegalArgumentException in case the node is no literal
     */
    @Override
    public URI getLiteralType(Value n) {
        try {
            if(((Literal)n).getDatatype() != null) {
                try {
                    return new URI(((Literal)n).getDatatype().stringValue());
                } catch (URISyntaxException e) {
                    log.error("literal datatype was not a valid URI: {}",((Literal) n).getDatatype());
                    return null;
                }
            } else {
                return null;
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+n.stringValue()+" is not a literal" +
                    "but of type "+debugType(n));
        }
    }

    /**
     * Create a literal node with the content passed as argument
     *
     * @param content string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    @Override
    public Value createLiteral(String content) {
        log.debug("creating literal with content \"{}\"",content);
        return repository.getValueFactory().createLiteral(content);
    }

    /**
     * Create a literal node with the content passed as argument
     *
     * @param content string content to represent inside the literal
     * @return a literal node in using the model used by this backend
     */
    @Override
    public Value createLiteral(String content, Locale language, URI type) {
        log.debug("creating literal with content \"{}\", language {}, datatype {}",new Object[]{content,language,type});
        if(language == null && type == null) {
            return repository.getValueFactory().createLiteral(content);
        } else if(type == null) {
            return repository.getValueFactory().createLiteral(content,language.getLanguage());
        } else  {
            return repository.getValueFactory().createLiteral(content, repository.getValueFactory().createURI(type.toString()));
        }
    }

    /**
     * Create a URI mode with the URI passed as argument
     *
     * @param uri URI of the resource to create
     * @return a URI node using the model used by this backend
     */
    @Override
    public Value createURI(String uri) {
        return repository.getValueFactory().createURI(uri);
    }

    /**
     * Return the string value of a node. For a literal, this will be the content, for a URI node it will be the
     * URI itself, and for a blank node it will be the identifier of the node.
     *
     * @param value
     * @return
     */
    @Override
    public String stringValue(Value value) {
        return value.stringValue();
    }
    @Override
    public BigDecimal decimalValue(Value node) {
        try {
            return ((Literal)node).decimalValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public BigInteger integerValue(Value node) {
        try {
            return ((Literal)node).integerValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Boolean booleanValue(Value node) {
        try {
            return ((Literal)node).booleanValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Date dateTimeValue(Value node) {
        try {
            XMLGregorianCalendar cal = ((Literal)node).calendarValue();
            //TODO: check if we need to deal with timezone and Local here
            return cal.toGregorianCalendar().getTime();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Date dateValue(Value node) {
        try {
            XMLGregorianCalendar cal = ((Literal)node).calendarValue();
            return new GregorianCalendar(cal.getYear(), cal.getMonth(), cal.getDay()).getTime();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Date timeValue(Value node) {
        //TODO: Unless someone knwos how to create a Date that only has the time
        //      from a XMLGregorianCalendar
        return dateTimeValue(node);
    }
    @Override
    public Long longValue(Value node) {
        try {
            return ((Literal)node).longValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Double doubleValue(Value node) {
        try {
            return ((Literal)node).doubleValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Float floatValue(Value node) {
        try {
            return ((Literal)node).floatValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }
    @Override
    public Integer intValue(Value node) {
        try {
            return ((Literal)node).intValue();
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Value "+node.stringValue()+" is not a literal" +
                    "but of type "+debugType(node));
        }
    }

    /**
     * List the objects of triples in the triple store underlying this backend that have the subject and
     * property given as argument.
     *
     * @param subject  the subject of the triples to look for
     * @param property the property of the triples to look for
     * @return all objects of triples with matching subject and property
     */
    @Override
    public Collection<Value> listObjects(Value subject, Value property) {
        //This checks for ClassCastException now
//        if(!isURI(property) || !(isURI(subject) || isBlank(subject))) {
//            throw new IllegalArgumentException("Subject needs to be a URI or blank node, property a URI node");
//        }

        try {
            RepositoryConnection connection = repository.getConnection();
            final RepositoryResult<Statement> qResult = connection.getStatements((Resource) subject, (org.openrdf.model.URI) property, null, true);

            try {
                return ImmutableSet.copyOf(
                        Iterators.transform(
                                new Iterator<Statement>() {
                                    @Override
                                    public boolean hasNext() {
                                        try {
                                            return qResult.hasNext();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }

                                    @Override
                                    public Statement next() {
                                        try {
                                            return qResult.next();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }

                                    @Override
                                    public void remove() {
                                        try {
                                            qResult.remove();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }
                                },
                                new Function<Statement, Value>() {
                                    @Override
                                    public Value apply(Statement input) {
                                        return input.getObject();
                                    }
                                }
                        )
                );
            } finally {
                qResult.close();
                connection.close();
            }

        } catch (RepositoryException e) {
            throw new RuntimeException("error while querying Sesame repository!",e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format(
                    "Subject needs to be a URI or blank node, property a URI node " +
                            "(types: [subject: %s, property: %s])",
                    debugType(subject),debugType(property)),e);
        }

    }
    /**
     * List the subjects of triples in the triple store underlying this backend that have the object and
     * property given as argument.
     *
     * @param object   the object of the triples to look for
     * @param property the property of the triples to look for
     * @return all subjects of triples with matching object and property
     * @throws UnsupportedOperationException in case reverse selection is not supported (e.g. when querying Linked Data)
     */
    @Override
    public Collection<Value> listSubjects(Value property, Value object) {
        //this checks now for ClassCastException
//        if(!isURI(property)) {
//            throw new IllegalArgumentException("Property needs to be a URI node");
//        }

        try {
            RepositoryConnection connection = repository.getConnection();

            final RepositoryResult<Statement> qResult = connection.getStatements(null, (org.openrdf.model.URI)property, object, true);
            try {
                return ImmutableSet.copyOf(
                        Iterators.transform(
                                new Iterator<Statement>() {
                                    @Override
                                    public boolean hasNext() {
                                        try {
                                            return qResult.hasNext();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }

                                    @Override
                                    public Statement next() {
                                        try {
                                            return qResult.next();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }

                                    @Override
                                    public void remove() {
                                        try {
                                            qResult.remove();
                                        } catch (RepositoryException e) {
                                            throw new RuntimeException("error while querying Sesame repository!",e);
                                        }
                                    }
                                },
                                new Function<Statement, Value>() {
                                    @Override
                                    public Value apply(Statement input) {
                                        return input.getSubject();
                                    }
                                }
                        )
                );
            } finally {
                qResult.close();
                connection.close();
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("error while querying Sesame repository!",e);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(String.format(
                    "Property needs to be a URI node (property type: ",
                    isURI(property)?"URI":isBlank(property)?"bNode":"literal"),e);
        }

    }

    /**
     * Prints the type (URI,bNode,literal) by inspecting the parsed {@link Value}
     * to improve error messages and other loggings. In case of literals 
     * also the {@link #getLiteralType(Value) literal type} is printed
     * @param value the value or <code>null</code> 
     * @return the type as string.
     */
    private String debugType(Value value){
        return value == null ? "null":isURI(value)?"URI":isBlank(value)?"bNode":
                "literal ("+getLiteralType(value)+")";
    }

}
