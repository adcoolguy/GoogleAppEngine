package com.appspot.cloudserviceapi;

import io.swagger.jaxrs.config.BeanConfig;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import cloudserviceapi.app.controller.CrudService;

/**
 * http://thebesthacker.com/question/unable-to-configure-swagger-with-embedded-jetty-programmatically.html
 */
public class MyServices extends Application  {

    private static Set<CrudService> services = new HashSet<>(); 

    public  MyServices() {     

        System.out.println( "Initializing Swagger BeanConfig" );
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setDescription("This is a app.");
        beanConfig.setTitle("Swagger Petstore");
        beanConfig.setHost("127.0.0.1:8002");
        beanConfig.setBasePath("/api");
        beanConfig.setFilterClass("io.swagger.sample.util.ApiAuthorizationFilterImpl");
        beanConfig.setResourcePackage("io.swagger.sample.resource");
        beanConfig.setContact("apiteam@swagger.io");
        beanConfig.setLicense("Apache 2.0");
        beanConfig.setLicenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html");
        
        services.add(new CrudService());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public  Set getSingletons() {
        return services;
    }  

    @SuppressWarnings("rawtypes")
    public  static Set getServices() {  
        return services;
    }

	@Override
	public Set<Class<?>> getClasses() {
		// TODO Auto-generated method stub
		return null;
	} 
}