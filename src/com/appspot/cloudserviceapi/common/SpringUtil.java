package com.appspot.cloudserviceapi.common;

import org.springframework.context.ApplicationContext;

/*
 * Source: http://java.dzone.com/articles/what%E2%80%99s-my-spring-context
 *
 */
public class SpringUtil {

	public static void toString(ApplicationContext applicationContext) {
		String[] beans = applicationContext.getBeanDefinitionNames();
		for (String o : beans) {
			System.out.println("________________________");
			System.out.println("BEAN = " + o);
			System.out.println("\tType = " + applicationContext.getType(o));
			String[] aliases = applicationContext.getAliases(o);
			if (aliases != null && aliases.length > 0) {
				for (String a : aliases) {
					System.out.println("\tAliased as: " + a);
				}
			}
		}

		System.out.println("********************");
		System.out.println("*** Number of Beans = {} *** "
				+ applicationContext.getBeanDefinitionCount());
		System.out.println("********************");
	}
}
