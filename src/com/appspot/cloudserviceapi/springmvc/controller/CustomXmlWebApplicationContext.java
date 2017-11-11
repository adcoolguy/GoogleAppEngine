package com.appspot.cloudserviceapi.springmvc.controller;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.XmlWebApplicationContext;

// import com.google.appengine.api.utils.SystemProperty;
//=== to avoidd Error: java.lang.NoClassDefFoundError: com/google/appengine/api/utils/SystemProperty
/*
 * https://cloud.google.com/appengine/articles/spring_optimization
 */
public class CustomXmlWebApplicationContext extends XmlWebApplicationContext {
	protected void initBeanDefinitionReader(
			XmlBeanDefinitionReader beanDefinitionReader) {
		super.initBeanDefinitionReader(beanDefinitionReader);
		// if (SystemProperty.environment.value() == SystemProperty.Environment.Value.Production) {
			beanDefinitionReader.setValidating(false);
			beanDefinitionReader.setNamespaceAware(true);
		// }
	}
}