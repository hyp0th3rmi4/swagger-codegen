package com.wordnik.swagger.codegen.languages;


import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class StaticDocCodegen extends DefaultCodegen implements CodegenConfig {
  protected String invokerPackage = "com.wordnik.client";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";
  protected String sourceFolder = "docs";

  public String getName() {
    return "dynamic-html";
  }

  public String getHelp() {
    return "Generates a dynamic HTML site.";
  }

  public StaticDocCodegen() {
    super();

    sourceFolder = "docs";
    modelTemplateFiles.put("model.mustache", ".html");
    apiTemplateFiles.put("operation.mustache", ".html");
    templateDir = "swagger-static";


    instantiationTypes.put("array", "ArrayList");
    instantiationTypes.put("map", "HashMap");
  }
  
  @Override
  public void init() {
	  
    if (additionalProperties.containsKey("invokerPackage") == true) {
    	
    	invokerPackage = (String) additionalProperties.get("invokerPackage");
    	
    } else {
    	
        additionalProperties.put("invokerPackage", invokerPackage);
    }
    
    if (additionalProperties.containsKey("groupId") == true) {
    	
    	groupId = (String) additionalProperties.get("groupId");
    	
    } else {
    	
        additionalProperties.put("groupId", groupId);
    }
    if (additionalProperties.containsKey("artifactId") == true) {
    	
    	artifactId = (String) additionalProperties.get("artifactId");
    	
    } else {
    	
        additionalProperties.put("artifactId", artifactId);
    }
    
    if (additionalProperties.containsKey("artifactVersion") == true) {
    	
    	artifactVersion = (String) additionalProperties.get("artifactVersion");
    	
    } else {
    	
        additionalProperties.put("artifactVersion", artifactVersion);
    }
    
    if (additionalProperties.containsKey("modelPackage") == true) {
    	
    	modelPackage = (String) additionalProperties.get("modelPackage");
    	
    } else {
    	
        additionalProperties.put("modelPackage", modelPackage);
    }
    
    if (additionalProperties.containsKey("apiPackage") == true) {
    	
    	apiPackage = (String) additionalProperties.get("apiPackage");
    	
    } else {
    	
        additionalProperties.put("apiPackage", apiPackage);
    }

    supportingFiles.clear();
    supportingFiles.add(new SupportingFile("package.mustache", "", "package.json"));
    supportingFiles.add(new SupportingFile("main.mustache", "", "main.js"));
    supportingFiles.add(new SupportingFile("assets/css/bootstrap-responsive.css",
      sourceFolder + "/assets/css", "bootstrap-responsive.css"));
    supportingFiles.add(new SupportingFile("assets/css/bootstrap.css",
      sourceFolder + "/assets/css", "bootstrap.css"));
    supportingFiles.add(new SupportingFile("assets/css/style.css",
      sourceFolder + "/assets/css", "style.css"));
    supportingFiles.add(new SupportingFile("assets/images/logo.png",
      sourceFolder + "/assets/images", "logo.png"));
    supportingFiles.add(new SupportingFile("assets/js/bootstrap.js",
      sourceFolder + "/assets/js", "bootstrap.js"));
    supportingFiles.add(new SupportingFile("assets/js/jquery-1.8.3.min.js",
      sourceFolder + "/assets/js", "jquery-1.8.3.min.js"));
    supportingFiles.add(new SupportingFile("assets/js/main.js",
      sourceFolder + "/assets/js", "main.js"));
    supportingFiles.add(new SupportingFile("index.mustache",
      sourceFolder, "index.html"));
  }

  @Override
  public String escapeReservedWord(String name) {
    return "_" + name;
  }

  @Override
  public String apiFileFolder() {
    return outputFolder + File.separator + sourceFolder + File.separator + "operations";
  }

  public String modelFileFolder() {
    return outputFolder + File.separator + sourceFolder + File.separator + "models";
	  
  }
}