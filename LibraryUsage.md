# Introduction #

The LDPath library consists of a modular set of submodules that you can use in your own projects to evaluate path queries over an RDF model. At the very minimum, you will need the following modules
  * **ldpath-api**: contains the interfaces for accessing and extending the LDPath language
  * **ldpath-core**: contains the generic (backend-independent) functionality of LDPath, especially the parser and evaluator
  * some RDF backend implementation, either one of the distributed backends (the distribution contains a generic sesame backend, a linked data backend, and a file-based backend) or your custom backend (see CustomBackends)




# Download #

At the moment, the LDPath libraries are only available via Maven/Gradle. Please refer to the following pages:
  * MavenArtifacts: describes how to access the maven artifacts for the LDPath libraries
  * BuildEnvironment: describes how to set up your own LDPath-based project using a preconfigured build environment

# Basic Usage #

The main class to access the LDPath functionality is called `LDPath` and can be found in the package `at.newmedialab.ldpath` in the ldpath-core module. It needs to be initialised with an RDF backend and then offers the following basic methods:

## Simple Path Queries ##

**Method**:  `pathQuery(context,path,namespaces)`

Execute a single path query starting from the given context node and return a collection of nodes resulting from the selection in the node format of the underlying backend. Default namespaces (rdf, rdfs, skos, dc, foaf) are added automatically, further namespaces can be passed as arguments.

Paths need to conform to the [Path Selector](http://code.google.com/p/ldpath/wiki/PathLanguage#Path_Selectors) syntax.

Example (all English labels of all types of the resource `http://dbpedia.org/resource/Berlin`) :
```
LDPath<Value> ldpath = new LDPath<Value>(new LDMemoryBackend());
Resource context = backend.getRepository().getValueFactory().createURI("http://dbpedia.org/resource/Berlin");
for(Value v : ldpath.pathQuery(context,"rdf:type / rdfs:label[@en]", null)) {
    // do something
}

```


## Simple Path Transformations ##


**Method**:  `pathTransform(context,path,namespaces)`

Similar to pathQuery, but transforms the resulting values into Java base types according to the [type specification](http://code.google.com/p/ldpath/wiki/PathLanguage#Field_Definitions) in the path expression and the registered transformer functions.

Example (all English labels of all types of the resource `http://dbpedia.org/resource/Berlin` as string) :
```
LDPath<Value> ldpath = new LDPath<Value>(new LDMemoryBackend());
Resource context = backend.getRepository().getValueFactory().createURI("http://dbpedia.org/resource/Berlin");
for(String s : ldpath.pathTransform(context,"rdf:type / rdfs:label[@en] :: xsd:string", null)) {
    // do something
}

```


## Program Queries ##


**Method**:  `programQuery(context,program)`

In addition to evaluating simple paths, LDPath also supports so-called path programs, which are essentially a collection of paths mapped to fields plus some configuration instructions. The result will be a map mapping field names to collections of Java base types, according to the transformers configured for LDPath.

Path programs are described in the [language documentation](http://code.google.com/p/ldpath/wiki/PathLanguage).

Example (read a program from a reader and apply it to the resource `http://dbpedia.org/resource/Berlin`) :
```
Reader program = ...;
LDPath<Value> ldpath = new LDPath<Value>(new LDMemoryBackend());
Resource context = backend.getRepository().getValueFactory().createURI("http://dbpedia.org/resource/Berlin");
Map<String,Collection<?>> result = ldpath.programQuery(program);
for(String s : result.keySet()) {
    Collection<?> field = result.get(s);
    // do something
}
```

A program could look as follows (example is for FOAF):
```
@prefix foaf : <http://xmlns.com/foaf/0.1/> ;
@prefix geo : <http://www.w3.org/2003/01/geo/wgs84_pos#> ;
title      = foaf:name | fn:concat(foaf:givename," ",foaf:surname) :: xsd:string ;
summary    = dc:description :: lmf:text ;
lng        = foaf:based_near / geo:long :: xsd:double ;
lat        = foaf:based_near / geo:lat :: xsd:double ;
interest   = foaf:interest / (rdfs:label[@en] | rdfs:label[@none] | <http://rdf.freebase.com/ns/type.object.name>[@en]) :: xsd:string;
friends    = foaf:knows / (foaf:name | fn:concat(foaf:givename," ",foaf:surname)) :: xsd:string;   
contrycode = foaf:based_near / <http://www.geonames.org/ontology#countryCode> :: xsd:string ;
type       = rdf:type :: xsd:anyURI ;
```


# Extending LDPath #

LDPath is very extensible with custom functions and transformers. The LDPath class offers methods for registering your own functions and transformers:
  * `registerFunction((function)`: registers a new SelectorFunction that allows manipulation of selected nodes, e.g. string concatenation, projection, etc.
  * `registerTransformer(typeUri, transformer)`: registers a new NodeTransformer that allows transforming a node result into a Java type. The function takes a typeUri that is used in path expressions to specify the transformer

## Custom Functions ##

Custom functions can be used to manipulate the outcomes of a selection, e.g. by concatenating strings into a single literal, cleaning up literal content, by projecting the first result, or by ordering the results according to some order.

Custom functions need to implement the SelectorFunction interface of lmf-api, which defines a single method `apply` that takes one or more selection results as arguments and returns a collection of nodes as result.

## Custom Transformers ##

Transformers are used to transform node results into Java types, e.g. into a Date or into your custom Java objects.

A transformer needs to implement the NodeTransformer interface of lmf-api, which defines a single method called `transform` that takes a single node and returns some type T.

Transformers need to be registered with a type URI. The core LDPath comes with a number of built-in transformers that map the XML Schema types to Java base types.