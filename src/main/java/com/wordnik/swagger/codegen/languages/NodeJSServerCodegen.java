package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class NodeJSServerCodegen extends DefaultCodegen implements CodegenConfig {
  protected String invokerPackage = "com.wordnik.client";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";

  public String getName() {
    return "nodejs";
  }

  public String getHelp() {
    return "Generates a node.js server application compatible with the 1.2 swagger specification.";
  }

  public NodeJSServerCodegen() {
    super();
    outputFolder = "generated-code/nodejs";
    apiTemplateFiles.put("api.mustache", ".js");
    templateDir = "nodejs";
    apiPackage = "app.apis";
    modelPackage = "app";

    languageSpecificPrimitives = new HashSet<String>(
      Arrays.asList(
        "String",
        "boolean",
        "Boolean",
        "Double",
        "Integer",
        "Long",
        "Float")
      );
    typeMapping.put("array", "array");
    

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
    supportingFiles.add(new SupportingFile("models.mustache", modelPackage, "models.js"));
    supportingFiles.add(new SupportingFile("main.mustache", "", "main.js"));
    supportingFiles.add(new SupportingFile("README.mustache", "", "README.js"));
  }

  @Override
  public String escapeReservedWord(String name) {
    return "_" + name;
  }

  @Override
  public String apiFileFolder() {
    return outputFolder + File.separator + apiPackage().replaceAll("\\.", File.separator);
  }

  public String modelFileFolder() {
    return outputFolder + File.separator + modelPackage().replaceAll("\\.", File.separator);
  }

  @Override
  public String getSwaggerType(Property p) {
    String swaggerType = super.getSwaggerType(p);
    String type = null;
    if(typeMapping.containsKey(swaggerType)) {
      return typeMapping.get(swaggerType);
    }
    else
      type = swaggerType;
    return toModelName(type);
	  
  }
}