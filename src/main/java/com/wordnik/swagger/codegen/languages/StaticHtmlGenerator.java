package com.wordnik.swagger.codegen.languages;

import com.wordnik.swagger.codegen.*;
import com.wordnik.swagger.models.properties.*;
import com.wordnik.swagger.util.Json;

import java.util.*;
import java.io.File;

public class StaticHtmlGenerator extends DefaultCodegen implements CodegenConfig {
  protected String invokerPackage = "com.wordnik.client";
  protected String groupId = "com.wordnik";
  protected String artifactId = "swagger-client";
  protected String artifactVersion = "1.0.0";
  protected String sourceFolder = "src/main/scala";

  public String getName() {
    return "html";
  }

  public String getHelp() {
    return "Generates a static HTML file.";
  }

  public StaticHtmlGenerator() {
    super();
    outputFolder = "docs";
    templateDir = "htmlDocs";

    defaultIncludes = new HashSet<String>();

    reservedWords = new HashSet<String>();

    languageSpecificPrimitives = new HashSet<String>();
    importMapping = new HashMap<String, String> ();
  }
  
  @Override
  public void init() {
	  
    String partner = "our Partner";

    if(System.getProperty("partner") != null)
      partner = System.getProperty("partner");
    
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
    
    if (additionalProperties.containsKey("partner") == true) {
    	
    	partner = (String) additionalProperties.get("partner");
    
    } else {

        additionalProperties.put("partner", partner);
    }
    
    if (additionalProperties.containsKey("appName") == false) {

        additionalProperties.put("appName", "Swagger Sample");
    }
    
    if (additionalProperties.containsKey("appDescription") == false) {

        additionalProperties.put("appDescription", "A sample swagger server");
    }    
    
    if (additionalProperties.containsKey("infoUrl") == false) {

        additionalProperties.put("infoUrl", "http://developers.helloreverb.com");
    }
    
    if (additionalProperties.containsKey("infoEmail") == false) {

        additionalProperties.put("infoEmail", "hello@helloreverb.com");
    }
    
    if (additionalProperties.containsKey("licenseInfo") == false) {

        additionalProperties.put("licenseInfo", "All rights reserved");
    }
    
    
    if (additionalProperties.containsKey("licenseUrl") == false) {
    	
        additionalProperties.put("licenseUrl", "http://apache.org/licenses/LICENSE-2.0.html");
    }
  
    supportingFiles.clear();
    supportingFiles.add(new SupportingFile("index.mustache", "", "index.html")); 
  }

  @Override
  public String getTypeDeclaration(Property p) {
    if(p instanceof ArrayProperty) {
      ArrayProperty ap = (ArrayProperty) p;
      Property inner = ap.getItems();
      return getSwaggerType(p) + "[" + getTypeDeclaration(inner) + "]";
    }
    else if (p instanceof MapProperty) {
      MapProperty mp = (MapProperty) p;
      Property inner = mp.getAdditionalProperties();

      return getSwaggerType(p) + "[String, " + getTypeDeclaration(inner) + "]";
    }
    return super.getTypeDeclaration(p);
  }

  @Override
  public Map<String, Object> postProcessOperations(Map<String, Object> objs) {
    Map<String, Object> operations = (Map<String, Object>) objs.get("operations");
    List<CodegenOperation> operationList = (List<CodegenOperation>) operations.get("operation");
    for(CodegenOperation op: operationList) {
      op.httpMethod = op.httpMethod.toLowerCase();
    }
    return objs;
	  
  }
}