/*
 * Copyright 2011-2015 Amazon Technologies, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *    http://aws.amazon.com/apache2.0
 *
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jets3t.service.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for rules of cross origin configuration.
 */
public class SS3CORSRule {

    private String id;
    private List<String> allowedMethods;
    private List<String> allowedOrigins;
    private int maxAgeSeconds;
    private List<String> exposedHeaders;
    private List<String> allowedHeaders;
    
    /**
     * Sets the ID of this rule. Rules must be less than 255 alphanumeric
     * characters, and must be unique for a bucket. If you do not assign an
     * ID, one will be generated.
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /**
     * Returns the Id of this rule.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the ID of this rule and returns a reference to this object for
     * method chaining.
     * 
     * @see SS3CORSRule#setId(String)
     */
    public SS3CORSRule withId(String id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the allowed methods of the rule.
     */
    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }
    
    /**
     * Returns the allowed methods of this rule.
     */
    public List<String> getAllowedMethods() {
        if(null == allowedMethods)
        {
            return allowedMethods = new ArrayList<String>();
        }
        return allowedMethods;
    }

    /**
     * Sets the allowed methods of this rule and returns a reference to this object for
     * method chaining.
     * 
     * @see SS3CORSRule#setAllowedMethods(List)
     */
    public SS3CORSRule withAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
        return this;
    }
    
    /**
     * Sets the allowed origins of the rule.
     */
    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }
    
    /**
     * Returns the allowed origins of this rule and returns a reference to this object for
     * method chaining.
     */
    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    /**
     * Sets the max age in seconds of the rule.
     */
    public void setMaxAgeSeconds(int maxAgeSeconds) {
    	this.maxAgeSeconds = maxAgeSeconds;
    }
    
    /**
     * Sets the ID of this rule and returns a reference to this object for
     * method chaining.
     * 
     * @see SS3CORSRule#setId(String)
     */
    public int getMaxAgeSeconds() {
    	return maxAgeSeconds;
    }
 
    /**
     * Sets the expose headers of the rule.
     */
    public void setExposedHeaders(List<String> exposedHeaders) {
    	this.exposedHeaders = exposedHeaders;
    }
    
    /**
     * Returns expose headers of this rule and returns a reference to this object for
     * method chaining.
     */
    public List<String> getExposedHeaders() {
        if(null == exposedHeaders)
        {
            return exposedHeaders = new ArrayList<String>();
        }
    	return exposedHeaders;
    }
    
    /**
     * Sets the allowed headers for the rule.
     */
    public void setAllowedHeaders(List<String> allowedHeaders) {
    	this.allowedHeaders = allowedHeaders;
    }
                     
    /**
     * Returns allowed headers of this rule.
     */
    public List<String> getAllowedHeaders() {
        if(null == allowedHeaders)
        {
            return allowedHeaders = new ArrayList<String>();
        }
    	return allowedHeaders;
    }
    
    public String toXML()
    {
        String xml = "";
        if (this.id != null){
            xml += "<ID>" + this.id + "</ID>";
        }
        xml += listToXML("AllowedMethod", this.allowedMethods);
        xml += listToXML("AllowedOrigin", this.allowedOrigins);
        xml += listToXML("AllowedHeader", this.allowedHeaders);
        xml += "<MaxAgeSeconds>" + this.maxAgeSeconds + "</MaxAgeSeconds>";
        xml += listToXML("ExposeHeader", this.exposedHeaders);
        return xml;
    }
    
    private String listToXML(String root,List<String>list)
    {
        String xml = "";
        
        if (list == null || root == null){
            return xml;
        }
        String rootStart = "<" + root + ">";
        String rootEnd  = "</" + root + ">";
        for (int i = 0; i < list.size(); i++)
        {
            xml += rootStart + list.get(i) + rootEnd;
        }
        return xml;   
    }
}
