package com.thingword.alphonso;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;


/**
 * ”¶”√
 * 
 * @author waylau.com 2014-3-18
 */
public class RestApplication extends ResourceConfig {
	public RestApplication() {
		packages("com.thingword.alphonso");
		register(MultiPartFeature.class);
		register(JacksonJsonProvider.class);	
	}
}