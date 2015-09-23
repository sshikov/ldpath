<div>
<b>WARNING: This project has been <a href='http://marmotta.incubator.apache.org'>merged into Apache Marmotta</a>, so alll the information and code here may not be up-to-date.</b> <a href='http://marmotta.incubator.apache.org'><img src='http://marmotta.incubator.apache.org/images/Marmotta_Logo_64.png' /></a>
</div>



&lt;hr/&gt;



LD Path is a simple path-based query language similar to XPath or SPARQL Property Paths that is particularly well-suited for querying and retrieving resources from the Linked Data Cloud by following RDF links between resources and servers. For example, the following path query would select the names of all friends of the context resource:

```
foaf:knows / foaf:name :: xsd:string
```

# Introduction #

The LDPath project is a collection of generic libraries that are independent of the underlying RDF implementation. Currently, there are backends for sesame, for RDF files, and for Linked Data. You can easily implement your own backends by implementing a straightforward interface (RDFBackend).

LDPath can serve many different purposes. It can e.g. serve as
  * a simple query language for selecting nodes or values in your own triple store programmatically from Java
  * a query language for transparently querying resources in the Linked Data Cloud and following links between datasets
  * a foundation for templating languages to render results based on RDF or Linked Data
  * a foundation for building a semantic search index (used e.g. in the Linked Media Framework and in Apache Stanbol)
  * a query language for experimenting with the Linked Data Cloud

# Modules #

The LDPath project consists of a collection of modules that can be combined as needed. We have grouped the modules in core modules, backends, and extensions. Modules are available in our Maven repository, see MavenArtifacts.

## Core Modules ##

The following modules are the core modules of LDPath and are needed in every situation:
  * **ldpath-api**: contains interfaces used by the language; needed for custom extensions
  * **ldpath-core**: contains the core, backend-independent implementation of the LDPath language, including parser and evaluator; expects some backend implementation to be present.

## Backend Modules ##

We provide a number of backends that are ready-to-use in your own projects. Implementing a backend is usually straightforward and involves mainly implementing the RDFBackend interface. RDFBackend makes use of Java Generics , so you are able to always use the data model of your backend directly. The following backends are provided by the distribution:
  * **ldpath-backend-sesame**: a generic backend implementation for Sesame repositories. A Sesame repository can be passed over on initialisation
  * **ldpath-backend-jena**: a generic backend implementation for Jena models. A Jena model can be passed over on initialisation
  * **ldpath-backend-file**: a file-based backend implementation allowing you to query the contents of an RDF file
  * **ldpath-backend-linkeddata**: a sophisticated backend implementation that queries and caches resources on the Linked Data Cloud

## Extension Modules ##

Based on LDPath, we have implemented a number of extension modules. Some are part of other projects like the [Linked Media Framework](http://code.google.com/p/kiwi) or [Apache Stanbol](http://incubator.apache.org/stanbol/). Part of the LDPath project itself are currently the following modules:
  * **ldpath-template**: implements an extension of the FreeMarker template engine that allows constructing templates with LDPath statements for inserting and iterating over values ([example template](http://code.google.com/p/ldpath/source/browse/ldpath-template-linkeddata/src/test/resources/city-html.ftl)); this module is backend-independent
  * **ldpath-template-linkeddata**: a backend implementation for ldpath-template that allows querying over the Linked Data Cloud; provides only a command-line tool for processing templates

# Downloading #

LDPath is meant as a library; there are several ways of downloading/installing the modules
  * through Maven/Gradle; the page MavenArtifacts describes which modules to add as dependencies
  * through our prepared build environment based on Gradle; the page BuildEnvironment describes this approach
  * by downloading one of the standalone jar distributions (mainly useful for testing); see the [Downloads Section](http://code.google.com/p/ldpath/downloads/list)