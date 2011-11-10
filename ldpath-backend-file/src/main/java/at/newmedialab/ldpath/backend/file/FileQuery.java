package at.newmedialab.ldpath.backend.file;

import at.newmedialab.ldpath.LDPath;
import at.newmedialab.ldpath.exception.LDPathParseException;
import org.apache.commons.cli.*;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Command line application for querying input from files.
 * <p/>
 * Author: Sebastian Schaffert
 */
public class FileQuery {

    private static final Logger log = LoggerFactory.getLogger(FileQuery.class);

    public static void main(String[] args) {
        Options options = buildOptions();

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse( options, args);

            String format = null;
            if(cmd.hasOption("format")) {
                format = cmd.getOptionValue("format");
            }

            FileBackend backend = null;
            if(cmd.hasOption("file")) {
                backend  = new FileBackend(cmd.getOptionValue("file"),format);
            } else if(cmd.hasOption("url")) {
                URL url = new URL(cmd.getOptionValue("url"));
                backend  = new FileBackend(url,format);
            }

            Resource context = null;
            if(cmd.hasOption("context")) {
                context = backend.getRepository().getValueFactory().createURI(cmd.getOptionValue("context"));
            }

            if(backend != null && context != null) {
                LDPath<Value> ldpath = new LDPath<Value>(backend);

                if(cmd.hasOption("path")) {
                    String path = cmd.getOptionValue("path");

                    for(Value v : ldpath.pathQuery(context,path,null)) {
                        System.out.println(v.stringValue());
                    }
                } else if(cmd.hasOption("program")) {
                    File file = new File(cmd.getOptionValue("program"));

                    Map<String,Collection<Object>> result = ldpath.programQuery(context,new FileReader(file));

                    for(String field : result.keySet()) {
                        StringBuilder line = new StringBuilder();
                        line.append(field);
                        line.append(" = ");
                        line.append("{");
                        for(Iterator it = result.get(field).iterator(); it.hasNext(); ) {
                            line.append(it.next().toString());
                            if(it.hasNext()) {
                                line.append(", ");
                            }
                        }
                        line.append("}");
                        System.out.println(line);

                    }
                }
            }


        } catch (ParseException e) {
            System.err.println("invalid arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp( "FileQuery", options, true );
        } catch (MalformedURLException e) {
            System.err.println("url could not be parsed");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("FileQuery", options, true);
        } catch (LDPathParseException e) {
            System.err.println("path or program could not be parsed");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.err.println("file or program could not be found");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("FileQuery", options, true);
        }


    }

    private static Options buildOptions() {
        Options result = new Options();

        OptionGroup input = new OptionGroup();
        Option file = OptionBuilder.withArgName("file").hasArg().withDescription("query the contents of a file").create("file");
        Option url = OptionBuilder.withArgName("url").hasArg().withDescription("query the contents of a remote URL").create("url");

        input.addOption(file);
        input.addOption(url);
        input.setRequired(true);

        result.addOptionGroup(input);

        Option format = OptionBuilder.withArgName("mimetype").hasArg().withDescription("MIME type of the input document").create("format");
        result.addOption(format);

        OptionGroup query = new OptionGroup();
        Option path = OptionBuilder.withArgName("path").hasArg().withDescription("LD Path to evaluate on the file starting from the context").create("path");
        Option program = OptionBuilder.withArgName("file").hasArg().withDescription("LD Path program to evaluate on the file starting from the context").create("program");
        query.addOption(path);
        query.addOption(program);
        query.setRequired(true);
        result.addOptionGroup(query);

        Option context = OptionBuilder.withArgName("uri").hasArg().withDescription("URI of the context node to start from").create("context");
        context.setRequired(true);
        result.addOption(context);

        return result;
    }

}
