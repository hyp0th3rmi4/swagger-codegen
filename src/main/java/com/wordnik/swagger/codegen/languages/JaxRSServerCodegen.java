package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.models.Operation;
import com.wordnik.swagger.models.Path;
import com.wordnik.swagger.util.Json;
import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;

import java.util.*;
import java.io.File;

public class JaxRSServerCodegen extends JavaClientCodegen implements CodegenConfig {
  protected String invokerPackage = "com.wordnik.api";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-server";
  protected String artifactVersion = "1.0.0";
  protected String sourceFolder = "src/main/java";
  protected String title = "Swagger Server";

  public String getName() {
    return "jaxrs";
  }

  public String getHelp() {
    return "Generates a Java JAXRS Server application.";
  }

  public JaxRSServerCodegen() {
    super();
    outputFolder = "generated-code/javaJaxRS";
    modelTemplateFiles.put("model.mustache", ".java");
    apiTemplateFiles.put("api.mustache", ".java");
    templateDir = "JavaJaxRS";
    apiPackage = "com.wordnik.api";
    modelPackage = "com.wordnik.model";
    
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
    

  }
  
  /*** [CV] BEGIN CODE PATCH ***/
  
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
	    
	    if (additionalProperties.containsKey("title") == true) {
	    	
	    	title = (String) additionalProperties.get("title");
	    	
	    } else {
	    	
	        additionalProperties.put("title", title);
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
	    supportingFiles.add(new SupportingFile("pom.mustache", "", "pom.xml"));
	    supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
	    supportingFiles.add(new SupportingFile("ApiException.mustache", 
	    		
	    		
	      (sourceFolder + File.separator + apiPackage).replace(".", java.io.File.separator), "ApiException.java"));
	    supportingFiles.add(new SupportingFile("ApiOriginFilter.mustache", 
	      (sourceFolder + File.separator + apiPackage).replace(".", java.io.File.separator), "ApiOriginFilter.java"));
	    supportingFiles.add(new SupportingFile("ApiResponseMessage.mustache", 
	      (sourceFolder + File.separator + apiPackage).replace(".", java.io.File.separator), "ApiResponseMessage.java"));
	    supportingFiles.add(new SupportingFile("NotFoundException.mustache", 
	      (sourceFolder + File.separator + apiPackage).replace(".", java.io.File.separator), "NotFoundException.java"));
	    supportingFiles.add(new SupportingFile("web.mustache", 
	      ("src/main/webapp/WEB-INF"), "web.xml"));
	    

  }
  
  /*** [CV] END CODE PATCH ***/

  @Override
  public void addOperationToGroup(String tag, String resourcePath, Operation operation, CodegenOperation co, Map<String, List<CodegenOperation>> operations) {
    String basePath = resourcePath;
    if(basePath.startsWith("/"))
      basePath = basePath.substring(1);
    int pos = basePath.indexOf("/");
    if(pos > 0)
      basePath = basePath.substring(0, pos);

    if(basePath == "")
      basePath = "default";
    else {
      if(co.path.startsWith("/" + basePath))
        co.path = co.path.substring(("/" + basePath).length());
        co.subresourceOperation = !co.path.isEmpty();
    }
    List<CodegenOperation> opList = operations.get(basePath);
    if(opList == null) {
      opList = new ArrayList<CodegenOperation>();
      operations.put(basePath, opList);
    }
    opList.add(co);
    co.baseName = basePath;
  }

  public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
    Map<String, Object> operations = (Map<String, Object>)objs.get("operations");
    if(operations != null) {
      List<CodegenOperation> ops = (List<CodegenOperation>) operations.get("operation");
      for(CodegenOperation operation : ops) {
        if(operation.returnType == null)
          operation.returnType = "Void";
        else if(operation.returnType.startsWith("List")) {
          String rt = operation.returnType;
          int end = rt.lastIndexOf(">");
          if(end > 0) {
            operation.returnType = rt.substring("List<".length(), end);
            operation.returnContainer = "List";
          }
        }
        else if(operation.returnType.startsWith("Map")) {
          String rt = operation.returnType;
          int end = rt.lastIndexOf(">");
          if(end > 0) {
            operation.returnType = rt.substring("Map<".length(), end);
            operation.returnContainer = "Map";
          }
        }
        else if(operation.returnType.startsWith("Set")) {
          String rt = operation.returnType;
          int end = rt.lastIndexOf(">");
          if(end > 0) {
            operation.returnType = rt.substring("Set<".length(), end);
            operation.returnContainer = "Set";
          }
        }
      }
    }
    return objs;
  }
  

}