# LDPath / RDF Path has moved to [Apache Marmotta](http://wiki.apache.org/marmotta/LDPath). #
All further changes and development will be documented at
http://wiki.apache.org/marmotta/LDPath.



The path language follows a syntax similar to XPath that can be used for selecting the values of properties. It has originally been developed for configuring the semantic search component of the [Linked Media Framework](http://code.google.com/p/kiwi/), but has been moved out into a separate, generic module. It still contains some syntactical constructs that are only relevant for configuring semantic search; these are marked explicitly below.

> The path language supports the following constructs:

# Namespace Definitions #

Define shortcut names for URI prefixes, like in SPARQL or N3.

**Syntax:**

```
@prefix PREFIX : <URI> ;
```

where PREFIX is a shortcut name for the uri URI.

**Examples:**

Define "foaf" as the label for the prefix "http://xmlns.com/foaf/0.1/"
```
  @prefix foaf : <http://xmlns.com/foaf/0.1/>;
```

**Defaults:**

Some common prefixes are predefined and always available without being specified. These are:
```
  rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
  rdfs: <http://www.w3.org/2000/01/rdf-schema#>
  owl: <http://www.w3.org/2002/07/owl#>
  skos: <http://www.w3.org/2004/02/skos/core#>
  dc: <http://purl.org/dc/elements/1.1/>
  xsd: <http://www.w3.org/2001/XMLSchema#>           (LMF base index datatypes/XML Schema)
  lmf: <http://www.newmedialab.at/lmf/types/1.0/>    (LMF extended index datatypes)
  fn: <http://www.newmedialab.at/lmf/functions/1.0/> (LMF index functions)
```

# Filter Definition (Search Indexing only) #
```
  @filter TEST
```

The search index will only contain nodes (documents) that pass the defined filter.

See [Value Testing](#Value_Testing.md)

# Boost Definition (Search Indexing only) #
```
  @boost PATH
```

apply a document boost based on the given `PATH`. Must evaluate to float, default is `1.0`.

# Field Definitions #

Define fields in the search index to map to path definitions.

```
FIELDNAME = PATH :: FIELDTYPE FIELDCONF
```

where `PATH` is an `RDF` path and `FIELDTYPE` is one of the available [field types](#Index_Types.md). `FIELDCONF` is an optional [field configuration](#Field_Configuration.md).

**Example:**

Select the name of the person
```
  title = foaf:name :: xsd:string ;
```

# Path Selectors #

The path language supports a number of path selectors that start at the current "context node" and return a collection of nodes when applied. The following is a short overview over the different selectors, detailed documentation follows below:
  * Property Selections (`URI` or `prefix:local`): select the values of a property
  * Reverse Property Selections (`^URI` or `^prefix:local`)
  * Wildcard Selections (`*`): select the values of all properties
  * Self Selector (`.`): select the current context node
  * Path Traversal (`/`): follow a path of selectors recursively
  * Unions (`|`): join the results of two selections in one collection
  * Intersections (`&`): build the intersection of the results of two selections
  * Recursive Selections (`(PATH)+`)
  * Tests (`[...]`): filter the collection based on test criteria
    * Language Test (`@language`): only literals of a certain language
    * Type Test (`^^xsdtype`): only literals of a certain type
    * Path Value Test (`is`): only resources with a subpath yielding a given value
    * Path Existance Test (`PATH`): only resources where a subpath yields some value
  * Functions (`f(...)`): apply a function on the values of the selections passed as argument

## Property Selections ##

A path definition selecting the value of a property. Either a URI enclosed in <> or a namespace prefix and a local name separated by ":"

```
<URI> | PREFIX:LOCAL
```

**Example:**

Select the foaf:name property using namespace prefix and local name:
```
  title = foaf:name :: xsd:string ;
```

Select the foaf:name property using the fully qualified URI
```
  title = <http://xmlns.com/foaf/0.1/name> :: xsd:string;
```

## Reverse Property Selections ##
_coming soon..._

## Wildcard Selections ##

Wildcard selections allow to select the values of all properties of a node. They are expressed by a "`*`":
```
  *
```

**Example:**

Select all properties of the resource:
```
  all = * :: xsd:string ;
```

## Path Traversal ##

Traverse a path by following several edges in the RDF graph. Each step is separated by a "/".
```
  PATH / PATH
```

Where PATH is an arbitrary path selector (e.g. a property selection or test)

**Example:**

Select the names of all friends:
```
  friend = foaf:knows/foaf:name :: xsd:string;
```

## Unions ##

Several alternative paths can be merged by using a union "|" between path elements
```
  PATH | PATH
```

Where PATH is an arbitrary path selector.

**Example:**

Select the labels or names of all friends:
```
  friend = foaf:knows/foaf:name | foaf:knows/rdfs:label :: xsd:string;
```

## Intersections ##

The intersection of several paths can be computed by using an intersection "&" between path elements
```
  PATH & PATH
```

Where PATH is an arbitrary path selector.

**Example**:

Select values that are both defined for foaf:interest and foaf:topic\_interest:
```
  topic_interests = foaf:interest & foaf:topic_interest :: xsd:anyURI;
```

## Recursive Selections ##
_coming soon..._

## Groupings ##

Path expressions can be grouped to change precedence or to improve readability by including them in braces:
```
  ( PATH )
```
where PATH is an arbitrary path selector.

**Example:**

Select the labes or names of friends:
```
  friend = foaf:knows/(foaf:name | rdfs:label) :: xsd:string ;
```


## Value Testing ##

The values of selections can be tested and filtered by adding test conditions in square brackets `[]` after a path selection:
```
   PATH [TEST]
```
where PATH is an arbitrary path selector and TEST is a test condition (see below).


### Literal Language Test ###

Literal language tests allow to select literal values of only the specified language. They can be expressed by '@' followed by the ISO language tag or the special value `none` to select literals without language definition.
```
@LANGUAGE
```
where LANGUAGE is the ISO language tag or the value "none".

**Example:**

Select labels with either German language or no defined language:
```
  title = rdfs:label[@de] | rdfs:label[@none] :: xsd:string ;
```

### Literal Type Test ###

Literal type tests allow to select only literals of a specified type, e.g. to ensure that only decimal values are indexed:
```
^^TYPE
```
where TYPE is the XML Schema type to select.

**Example:**

Select all literal values of type xsd:decimal:
```
  decimals = *[^^xsd:decimal] :: xsd:decimal
```

### Resource Path Value Tests ###

Resource path value tests only allow resources where a subpath selection matches a certain value condition:
```
PATH is VALUE
```
where PATH is an arbitrary path selection and VALUE is a URI, prefix:local, or literal value definition.

**Example:**

Select all interests of type ex:Food (path condition):
```
  food = foaf:interest[rdf:type is ex:Food] :: xsd:anyURI;
```

### Resource Path Existance Tests ###

Resource path existance tests only allow resources where a subpath selection selects at least some value:
```
PATH
```
where PATH is an arbitrary path selection.

**Example:**

Select all friends with a foaf:name defined:
```
  friends = foaf:knows[foaf:name] :: xsd:anyURI;
```


### Test Conjunction and Disjunction ###

Several tests can be connected using "&" (for conjunction/and) or "|" (for disjunction/or).

**Example:**

Select all interests of type ex:Food or type ex:Drink:
```
  foodstuff = foaf:interest[rdf:type is ex:Food | rdf:type is ex:Drink] :: xsd:anyURI ;
```

Select all interests of type ex:Food and type ex:Drink:
```
  fluidfood = foaf:interest[rdf:type is ex:Food & rdf:type is ex:Drink] :: xsd:anyURI ;
```


### Combinations of Tests ###

A path traversal can contain several tests.

**Example:**

Select the Spanish label of all interests of type ex:Food
```
  foodstuff = foaf:interest[rdf:type is ex:Food]/rdfs:label[@es] :: xsd:string ;
```


## Functions ##

Functions can be used inside the path to transform the results of path queries. A function takes the form
```
  f(PARAMETERS)
```
where PARAMETERS is a list of path expressions or direct literals. A function selector returns no result if any of its parameters returns no result.

### Builtin: String Concatenation ###

String concatenation is a very frequently needed function and is already built in:

Concatenate the foaf:given and foaf:surname of all friends:
```
  friends = foaf:knows/fn:concat(foaf:given," ",foaf:surname) :: xsd:string ;
```

Combination of concatenation and union:
```
  friends = foaf:knows/(fn:concat(foaf:given," ",foaf:surname) | foaf:name) :: xsd:string ;
```

### Builtin: List Operation (First and Last) ###

Selects the first (last) non-empty argument.

```
  label = fn:first(skos:prefLabel[@de], skos:prefLabel) :: xsd:string ;
```


### Builtin: String Cleansing ###
Removes all html/xml tags from the string representations of the arguments.

```
  content = fn:removeTags(ex:hasHtmlContent) :: xsd:string ;
```

### Builtin: XPath ###
Nodes are transformed into string representation and interpreted as XML. Given XPath expression is evaluated on these XML-Docs.

```
  title = fn:xpath("//head/title/text()", ex:hasHtmlContent) :: xsd:string ;
```

### Builtin: Resource Content ###
In some cases you will want to retrieve the human-readable content (e.g. in HTML) that is associated with a resource, e.g. a website. The RDF Path language has a builtin function to achieve this goal:

```
content = fn:content(.) :: lmf:text_en ;
```

would retrieve the content of the current resource and index it as English text. Arbitrary paths can be used, so this function can also be used to retrieve the content of resource selected by more complex path selections:

```
homepage_content = fn:content(foaf:homepage) :: lmf:text_en ;
```

would retrieve the homepage of a foaf:Person and index it as English text.

# Index Types (Search Indexing only) #

All XML Schema built-in datatypes (http://www.w3.org/TR/xmlschema-2/) are supported as field types in the SOLR index.
In particular, the following field types are frequently used:

  * xsd:anyURI - URI values, indexed without further processing like tokenization
  * xsd:string - String values, indexed without further processing like tokenization
  * xsd:decimal - decimal numbers, indexed as long values with range querying
  * xsd:double - double (floating point) numbers, indexed as double values with range querying
  * xsd:dateTime - dates and timestamps, indexed as dates with range querying

In addition to the XML Schema base types, the following extended field types are supported:

  * lmf:text\_en - English language text, indexed with tokenization and English stopwords, lowercase and stemming
  * lmf:text\_de - German language text, indexed with tokenization and German stopwords, lowercase and stemming
  * lmf:text\_es - Spanish language text, indexed with tokenization and Spanish stopwords, lowercase and stemming
  * lmf:text\_fr - French language text, indexed with tokenization and French stopwords, lowercase and stemming
  * lmf:text\_it - Italian language text, indexed with tokenization and Italian stopwords, lowercase and stemming
  * lmf:text\_cz - Czech language text, indexed with tokenization and Czech stopwords, lowercase and stemming

  * lmf:location - convert node into [solr.LatLonType](http://wiki.apache.org/solr/SpatialSearch#LatLonType), using `wgs84:lat,wgs84:long` as properties
  * lmf:geohash - convert node into [solr.GeoHashField](http://wiki.apache.org/solr/SpatialSearchDev#Geohash), using `wgs84:lat,wgs84:long` as properties
  * lmf:location\_s - interpret evaluated string as [solr.LatLonType](http://wiki.apache.org/solr/SpatialSearch#LatLonType)
  * lmf:geohash\_s - interpret evaluated string as [solr.GeoHashField](http://wiki.apache.org/solr/SpatialSearchDev#Geohash)

# Field Configuration (Search Indexing only) #
Field Configuration allows additional index configuration for each field.

```
  ( PROP="VAL", ... )
```

`PROP`s are currently used for the generation of the solr schema.xml (see also [SOLR Field options](http://wiki.apache.org/solr/SchemaXml#Common_field_options)).
The following properties are interpreted by the LMF:

  * `indexed` - (default: `true`) True if this field should be "indexed", required for searching, sorting, and faceting
  * `stored` - (default: `true`) True if the value of the field should be retrievable during a search
  * `compressed` - (default: `false`) True if the value of the field should be retrievable during a search
  * `compressThreshold`
  * `multiValued` - (default: `true`, except for `LatLonType`s) True if this field may contain multiple values
  * `omitNorms`
  * `omitTermFreqAndPositions`
  * `termVectors`
  * `termPositions`
  * `termOffsets`

  * `copy` - (default: `text_all`) Comma-separated list of fields to copy this fields value to. (see [SOLR Copy Fields](http://wiki.apache.org/solr/SchemaXml#Copy_Fields))


# Examples #


## FOAF ##

```
@prefix foaf : <http://xmlns.com/foaf/0.1/> ;
@prefix geo : <http://www.w3.org/2003/01/geo/wgs84_pos#> ;
title      = foaf:name | fn:concat(foaf:givename," ",foaf:surname) :: xsd:string ;
summary    = dc:description :: lmf:text ;
geo        = foaf:based_near :: lmf:location;
interest   = foaf:interest / (rdfs:label[@en] | rdfs:label[@none] | <http://rdf.freebase.com/ns/type.object.name>[@en]) :: xsd:string;
friends    = foaf:knows / (foaf:name | fn:concat(foaf:givename," ",foaf:surname)) :: xsd:string;   
contrycode = foaf:based_near / <http://www.geonames.org/ontology#countryCode> :: xsd:string ;
type       = rdf:type :: xsd:anyURI ;
```