package com.wordnik.swagger.codegen;

import com.wordnik.swagger.models.Swagger;

import io.swagger.parser.SwaggerParser;

import org.apache.commons.cli.*;

import java.io.File;
/*** [CV] NOTE: BEGIN PATCH CODE ***/
import java.io.FileInputStream;
/*** [CV] NOTE: END PATCH CODE ***/
import java.util.*;

public class Codegen extends DefaultGenerator {
  static String debugInfoOptions = "\nThe following additional debug options are available for all codegen targets:" +
    "\n -DdebugSwagger prints the swagger specification as interpreted by the codegen" +
    "\n -DdebugModels prints models passed to the template engine" +
    "\n -DdebugOperations prints operations passed to the template engine" +
    "\n -DdebugSupportingFiles prints additional data passed to the template engine";
  public static void main(String[] args) {
	    List<CodegenConfig> extensions = getExtensions();
	    Map<String, CodegenConfig> configs = new HashMap<String, CodegenConfig>();

	    StringBuilder sb = new StringBuilder();
	    for(CodegenConfig config : extensions) {
	      if(sb.toString().length() != 0)
	        sb.append(", ");
	      sb.append(config.getName());
	      configs.put(config.getName(), config);
	    }

	    Options options = new Options();
	    options.addOption("h", "help", false, "shows this message");
	    options.addOption("l", "lang", true, "client language to generate.\nAvailable languages include:\n\t[" + sb.toString() + "]");
	    options.addOption("o", "output", true, "where to write the generated files");
	    options.addOption("i", "input-spec", true, "location of the swagger spec, as URL or file");
	    options.addOption("t", "template-dir", true, "folder containing the template files");
	    options.addOption("d", "debug-info", false, "prints additional info for debugging");
	    
	    /*** [CV] NOTE: BEGIN PATCH CODE ***/
	    options.addOption("p", "properties-file", true, "location of the properties file containing additional properties for the generator");
	    
	    if (args.length == 0) {
	    	
	    	usage(options);
	    	return;
	    }
	    
	    /*** [CV] NOTE: END PATCH CODE ***/
	      
	    ClientOptInput clientOptInput = new ClientOptInput();
	    ClientOpts clientOpts = new ClientOpts();
	    Swagger swagger = null;


	    
	    CommandLine cmd = null;
	    try {
	      CommandLineParser parser = new BasicParser();
	      CodegenConfig config = null;

	      cmd = parser.parse(options, args);
	      if (cmd.hasOption("d")) {
	        usage(options);
	        System.out.println(debugInfoOptions);
	        return;
	      }
	      if (cmd.hasOption("l"))
	        clientOptInput.setConfig(getConfig(cmd.getOptionValue("l"), configs));
	      if (cmd.hasOption("o"))
	        clientOptInput.getConfig().setOutputDir(cmd.getOptionValue("o"));
	      if (cmd.hasOption("h")) {
	        if(cmd.hasOption("l")) {
	          config = getConfig(String.valueOf(cmd.getOptionValue("l")), configs);
	          if(config != null) {
	            options.addOption("h", "help", true, config.getHelp());
	            usage(options);
	            return;
	          }
	        }
	        usage(options);
	        return;
	      }
	      if (cmd.hasOption("i"))
	        swagger = new SwaggerParser().read(cmd.getOptionValue("i"));
	      if (cmd.hasOption("t"))
	        clientOpts.getProperties().put("templateDir", String.valueOf(cmd.getOptionValue("t")));
	       
	      /*** [CV] NOTE: BEGIN PATCH CODE ***/
	        
	      // we add additional properties that are read from a properties
	      // file, in this way we can customise code generation rather than
	      // using default wordnik values. These properties are dependent
	      // on the specific code generator used and can be several, therefore
	      // it makes sense to wrap them into a properties file.
	      //
	      if (cmd.hasOption("p")) {
	        String propertiesFilePath = String.valueOf(cmd.getOptionValue("p"));
	        File propertiesFile = new File(propertiesFilePath);
	        if (propertiesFile.exists()) {
	          Properties customProperties = new Properties();
	          customProperties.load(new FileInputStream(propertiesFile));
	          for(String key : customProperties.stringPropertyNames()) {
	             String value = customProperties.getProperty(key);
	             clientOpts.getProperties().put(key, value);
	             System.out.println("Added custom property: " + key + " = " + value );
	          }
	              
	        } else {
	            System.out.println("Warning custom properties file '" + propertiesFilePath + "'' does not exist." );
	        }
	      }
	        
	      /*** [CV] NOTE: END PATCH CODE ***/
	        
	    }
	    catch (Exception e) {
	      usage(options);
	      return;
	    }
	    try{
	      clientOptInput
	        .opts(clientOpts)
	        .swagger(swagger);
	      new Codegen().opts(clientOptInput).generate();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
  }

  public static List<CodegenConfig> getExtensions() {
    ServiceLoader<CodegenConfig> loader = ServiceLoader.load(CodegenConfig.class);
    List<CodegenConfig> output = new ArrayList<CodegenConfig>();
    Iterator<CodegenConfig> itr = loader.iterator();
    while(itr.hasNext()) {
      output.add(itr.next());
    }
    return output;
  }
  
  static void usage(Options options) {
    HelpFormatter formatter = new HelpFormatter();
    formatter.printHelp( "Codegen", options );
  }

  static CodegenConfig getConfig(String name, Map<String, CodegenConfig> configs) {
    if(configs.containsKey(name)) {
      return configs.get(name);
    }
    else {
      // see if it's a class
      try {
        System.out.println("loading class " + name);
        Class customClass = Class.forName(name);
        System.out.println("loaded");
        return (CodegenConfig)customClass.newInstance();
      }
      catch (Exception e) {
        throw new RuntimeException("can't load class " + name);
      }
    }
  }
  
}
