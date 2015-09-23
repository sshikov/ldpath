# Introduction #

The LD Path libraries are meant as a generic platform for RDF and Linked Data Querying. We therefore provide a complete build environment that allows you to build your own custom projects based on the core services of LDPath.

# Step-by-Step Preparation #

For setting up your own project, follow the following steps:

## Download the Build Environment ##

To simplify setting up your own projects, we have created a complete build environment that will download the necessary components from our Maven repository. The build environment is contained in a tar archive available at:

[ldpath-buildenv.zip](http://ldpath.googlecode.com/files/ldpath-buildenv.zip)

Unpack this archive and rename the directory to your own project name.


## Download Gradle 1.0 Milestone 6 ##

The prepared build environment uses the Gradle system for configuring, building and deploying your project. At the time of this writing, you need **Gradle 1.0 Milestone 6**. Older versions will very likely not work. You can download the Gradle system at

> [gradle-1.0-milestone-6-all.zip](http://repo.gradle.org/gradle/distributions/gradle-1.0-milestone-6-all.zip)

Unzip this file to a directory of your choice (e.g. /usr/local/gradle) and make the ./bin directory accessible in your path (e.g. by symlinking YOURDIR/bin/gradle to /usr/local/bin/gradle).

For running Gradle, it is useful to set the gradle runtime options for more memory and IPv4 usage. You can do this under Unix using the command

```
export GRADLE_OPTS="-Xmx2048m -XX:MaxPermSize=256m -Djava.net.preferIPv4Stack=true"
```

## Configure Build Environment ##

The build environment comes with three configuration files:
  * build.gradle contains the build configuration; depending on how your project will look like, you might want to extend it beyond the base configuration
  * project-spec.groovy contains project-specific configurations; this file is custom to each project and should be committed to the source repository
  * userConfig.properties contains user-specific configurations that override the default settings in project-spec.groovy; this file should be custom for each user in the project and probably not committed to a source repository

In order to start your own project, you should at the very least do the following changes to the configuration:

### project-spec.groovy ###

Change the following settings:
  * name: name of your project
  * group: Maven group of your project (used when uploading to a repository)
  * version: version of your project
  * versions: library versions of dependencies

### userConfig.properties ###

Allows overriding the default settings. You can change the same parameters as for project-spec.groovy.


## Test Compile ##

Now you are ready to perform a new test compile; enter

```
gradle jar
```

The console log output will tell you under which URL you can access the system when startup has completed


# Configure IDE (Intellij IDEA or Eclipse) #

When the configuration has been performed correctly, the build environment can set up the complete project definition for Intellij IDEA or Eclipse for you. You can do this by running

```
gradle idea
```

for Idea or for Eclipse

```
gradle eclipse
```