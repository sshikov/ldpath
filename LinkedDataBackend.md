# Introduction #

The LDPath Linked Data Backend allows querying resources on the Linked Data Cloud and following paths across different servers that implement the Linked Data Principles. Note that besides the Linked Data Backend, LDPath also offers simpler backends for local usage.

The LDPath Linked Data Backend can be used either as a library or on the command line. Command Line usage is intended primarily for testing and debugging. Library usage is intended for usage inside your own projects.

# Command Line Usage #


## Download ##

The LDPath command line client can be used by downloading one of the "standalone" binaries from the [Downloads](http://code.google.com/p/ldpath/downloads/list) section. The current snapshot version is available as

http://code.google.com/p/ldpath/downloads/detail?name=ldpath-backend-linkeddata-0.9-SNAPSHOT-standalone.jar&can=2&q=

## Usage ##

The standalone JAR file can be started from the command line using "java -jar":

```
java -jar ldpath-backend-linkeddata-0.9-SNAPSHOT-standalone.jar
```

will give the following usage description:

```
usage: LDQuery -context <uri> [-loglevel <level>] -path <path> | -program
       <file>  [-store <dir>]
 -context <uri>      URI of the context node to start from
 -loglevel <level>   set the log level; default is 'warn'
 -path <path>        LD Path to evaluate on the file starting from the
                     context
 -program <file>     LD Path program to evaluate on the file starting from
                     the context
 -store <dir>        cache the retrieved data in this directory
```


# Library Usage #

## Maven Repository ##

The LDPath Linked Data Backend is available as Maven artifacts in the following repositories:

Releases:
http://devel.kiwi-project.eu:8080/nexus/content/repositories/releases/

Snapshots:
http://devel.kiwi-project.eu:8080/nexus/content/repositories/snapshots/

| Group ID | at.newmedialab.ldpath |
|:---------|:----------------------|
| Module   | ldpath-backend-linkeddata |
| Version  | 0.9.1-SNAPSHOT        |