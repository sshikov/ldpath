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

package at.newmedialab.ldpath.template;

import at.newmedialab.ldpath.backend.linkeddata.LDMemoryBackend;
import at.newmedialab.ldpath.backend.linkeddata.LDPersistentBackend;
import at.newmedialab.ldpath.backend.sesame.GenericSesameBackend;
import at.newmedialab.ldpath.template.engine.TemplateEngine;
import ch.qos.logback.classic.Level;
import freemarker.template.TemplateException;
import org.apache.commons.cli.*;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Add file description here!
 * <p/>
 * Author: Sebastian Schaffert
 */
public class LDTemplate {


    private static final Logger log = LoggerFactory.getLogger(LDTemplate.class);

    public static void main(String[] args) {
        Options options = buildOptions();

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse( options, args);

            Level logLevel = Level.WARN;

            if(cmd.hasOption("loglevel")) {
                String logLevelName = cmd.getOptionValue("loglevel");
                if("DEBUG".equals(logLevelName.toUpperCase())) {
                    logLevel = Level.DEBUG;
                } else if("INFO".equals(logLevelName.toUpperCase())) {
                    logLevel = Level.INFO;
                } else if("WARN".equals(logLevelName.toUpperCase())) {
                    logLevel = Level.WARN;
                } else if("ERROR".equals(logLevelName.toUpperCase())) {
                    logLevel = Level.ERROR;
                } else {
                    log.error("unsupported log level: {}",logLevelName);
                }
            }

            if(logLevel != null) {
                for(String logname : new String [] {"at","org","net","com"}) {

                    ch.qos.logback.classic.Logger logger =
                            (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(logname);
                    logger.setLevel(logLevel);
                }
            }

            File template = null;
            if(cmd.hasOption("template")) {
                template = new File(cmd.getOptionValue("template"));
            }


            GenericSesameBackend backend;
            if(cmd.hasOption("store")) {
                backend = new LDPersistentBackend(new File(cmd.getOptionValue("store")));
            } else {
                backend = new LDMemoryBackend();
            }

            Resource context = null;
            if(cmd.hasOption("context")) {
                context = backend.getRepository().getValueFactory().createURI(cmd.getOptionValue("context"));
            }

            BufferedWriter out = null;
            if(cmd.hasOption("out")) {
                File of = new File(cmd.getOptionValue("out"));
                if(of.canWrite()) {
                    out = new BufferedWriter(new FileWriter(of));
                } else {
                    log.error("cannot write to output file {}",of);
                    System.exit(1);
                }
            } else {
                out = new BufferedWriter(new OutputStreamWriter(System.out));
            }

            if(backend != null && context != null && template != null) {
                TemplateEngine<Value> engine = new TemplateEngine<Value>(backend);

                engine.setDirectoryForTemplateLoading(template.getParentFile());
                engine.processFileTemplate(context,template.getName(),out);
                out.flush();
                out.close();
            }

            if(backend instanceof LDPersistentBackend) {
                ((LDPersistentBackend) backend).shutdown();
            }


        } catch (ParseException e) {
            System.err.println("invalid arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "LDQuery", options, true );
        } catch (FileNotFoundException e) {
            System.err.println("file or program could not be found");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("LDQuery", options, true);
        } catch (IOException e) {
            System.err.println("could not access file");
            e.printStackTrace(System.err);
        } catch (TemplateException e) {
            System.err.println("error while processing template");
            e.printStackTrace(System.err);
        }


    }

    private static Options buildOptions() {
        Options result = new Options();


        Option context = OptionBuilder.withArgName("uri").hasArg().withDescription("URI of the context node to start from").create("context");
        context.setRequired(true);
        result.addOption(context);

        Option template = OptionBuilder.withArgName("file").hasArg().withDescription("the template file to apply to the context resource").create("template");
        template.setRequired(true);
        result.addOption(template);

        Option out = OptionBuilder.withArgName("file").hasArg().withDescription("file where to write the output; if not given, will write to stdout").create("out");
        out.setRequired(false);
        result.addOption(out);

        Option loglevel = OptionBuilder.withArgName("level").hasArg().withDescription("set the log level; default is 'warn'").create("loglevel");
        result.addOption(loglevel);

        Option store = OptionBuilder.withArgName("dir").hasArg().withDescription("cache the retrieved data in this directory").create("store");
        result.addOption(store);


        return result;
    }

}
