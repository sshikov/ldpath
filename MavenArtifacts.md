# Introduction #

All LDPath modules are available as Maven artifacts. The artifacts are regularly uploaded to the following repositories:

Releases:
http://devel.kiwi-project.eu:8080/nexus/content/repositories/releases/

Snapshots:
http://devel.kiwi-project.eu:8080/nexus/content/repositories/snapshots/

Note that there are currently not yet any uploaded releases, so please use the snapshot repository in the meantime.

# Modules #

## ldpath-api ##

Contains the interfaces for implementing extensions to LDPath. Always required.

| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-api            |
| Version  | 0.9.3-SNAPSHOT        |


## ldpath-core ##

Contains the generic backend-independent implementation of the path language. Always required.

| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-core           |
| Version  | 0.9.3-SNAPSHOT        |


## ldpath-backend-sesame ##

Contains a generic Sesame-based backend implementation for LDPath. Can be used in your own projects when you already have an existing Sesame repository that you want to access.


| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-backend-sesame |
| Version  | 0.9.3-SNAPSHOT        |

## ldpath-backend-jena ##

Contains a generic Jena-based backend implementation for LDPath. Can be used in your own projects when you already have an existing Jena model that you want to access.


| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-backend-jena   |
| Version  | 0.9.3-SNAPSHOT        |

## ldpath-backend-file ##

A file-based backend based on Sesame, allows reading and querying the contents of an RDF file. Mainly useful for simple applications or testing.

| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-backend-file   |
| Version  | 0.9.3-SNAPSHOT        |

## ldpath-backend-linkeddata ##

A complete Linked Data backend with caching and both in-memory and persistent caching backend implementations. Allows evaluating LDPath queries over resources in the Linked Data Cloud.


| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-backend-linkeddata |
| Version  | 0.9.3-SNAPSHOT        |


## ldpath-template ##

A generic templating engine based on Freemarker that allows to insert LDPath query expressions in Freemarker templates, e.g. to create XML or HTML documents based on queries. Backend independent.


| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-template       |
| Version  | 0.9.3-SNAPSHOT        |

## ldpath-template-linkeddata ##

A complete templating implementation that uses a Linked Data backend for querying.


| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-template-linkeddata |
| Version  | 0.9.3-SNAPSHOT        |