package tapp.services;

import java.math.BigInteger;
import java.security.SecureRandom;

import org.apache.tapestry5.SymbolConstants;
//import org.apache.tapestry5.beanvalidator.BeanValidatorConfigurer;	//gone since 5.3.2
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.SubModule;
import org.apache.tapestry5.services.BeanBlockContribution;
import org.apache.tapestry5.services.HttpServletRequestFilter;
import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.services.Request;

import cloudserviceapi.app.controller.GeniusManagerImpl;

//import tapp.model.eo.CdActivityDetails;
//import tapp.model.eo.College;
//import tapp.model.eo.ParentInput;
//import tapp.model.eo.Student;
//import tapp.model.eo.Subject;




import cloudserviceapi.service.manager.GeniusManager;
import cloudserviceapi.service.manager.SecuredManager;
import cloudserviceapi.service.manager.SecuredManagerImpl;
import cloudserviceapi.service.manager.SocialManager;
import cloudserviceapi.service.manager.SocialManagerImpl;

import com.appspot.cloudserviceapi.data.AppEngine;
//import com.appspot.cloudserviceapi.eo.services.manager.ActivityManager;
//import com.appspot.cloudserviceapi.eo.services.manager.ActivityManagerImpl;
//import com.appspot.cloudserviceapi.eo.services.manager.ParentInputService;
//import com.appspot.cloudserviceapi.eo.services.manager.ParentInputServiceImpl;
import com.appspot.cloudserviceapi.sci.services.manager.FiOSTokenManager;
import com.appspot.cloudserviceapi.sci.services.manager.FiOSTokenManagerImpl;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManager;
import com.appspot.cloudserviceapi.sci.services.manager.ServiceRegistryManagerImpl;
import com.appspot.cloudserviceapi.sci.services.manager.VideoDataManager;
import com.appspot.cloudserviceapi.sci.services.manager.VideoDataManagerImpl;
import com.appspot.cloudserviceapi.service.tapestry.RequiresLoginFilter;
import com.appspot.cloudserviceapi.services.manager.UserManager;
import com.appspot.cloudserviceapi.services.manager.UserManagerImpl;
import com.appspot.cloudserviceapi.sgc.services.manager.ClientManager;
import com.appspot.cloudserviceapi.sgc.services.manager.ClientManagerImpl;
import com.appspot.cloudserviceapi.sgc.services.manager.EmployeeManager;
import com.appspot.cloudserviceapi.sgc.services.manager.EmployeeManagerImpl;
import com.appspot.cloudserviceapi.sgc.services.manager.OrderManager;
import com.appspot.cloudserviceapi.sgc.services.manager.OrderManagerImpl;
import com.troymaxventures.tapestry.gaeutils.GaeDevServerModule;

//https://github.com/plannowtech/tapestry5-ckeditor
//@SubModule({com.plannow.tapestry5.ckeditor.services.CkEditorModule.class, GaeDevServerModule.class})
//@SubModule(com.plannow.tapestry5.ckeditor.services.CkEditorModule.class)
@SubModule(GaeDevServerModule.class)
public class AppModule {

	// private static final Logger log =
	// Logger.getLogger(AppModule.class.getName());
	@Inject
	private Request request;

	public static void bind(ServiceBinder binder) {

		binder.bind(OrderManager.class, OrderManagerImpl.class);
		binder.bind(ClientManager.class, ClientManagerImpl.class);
		binder.bind(EmployeeManager.class, EmployeeManagerImpl.class);
		binder.bind(UserManager.class, UserManagerImpl.class);
		binder.bind(FiOSTokenManager.class, FiOSTokenManagerImpl.class);
//		binder.bind(ActivityManager.class, ActivityManagerImpl.class);
//		binder.bind(ParentInputService.class, ParentInputServiceImpl.class);
		binder.bind(VideoDataManager.class, VideoDataManagerImpl.class);
		binder.bind(ServiceRegistryManager.class,
				ServiceRegistryManagerImpl.class);
		binder.bind(GeniusManager.class, GeniusManagerImpl.class);
		binder.bind(SocialManager.class, SocialManagerImpl.class);
		binder.bind(SecuredManager.class, SecuredManagerImpl.class);
		System.out.println("AppModule bind() done.");
		System.out.println("AppEngine application '" + AppEngine.getName()
				+ "' started.");
	}

	public static void contributeApplicationDefaults(
			MappedConfiguration<String, Object> configuration) {
		// reference:
		// http://tapestry.apache.org/5.3/apidocs/constant-values.html
//		configuration.add(SymbolConstants.PRODUCTION_MODE, false);
//		configuration.add(SymbolConstants.APPLICATION_VERSION, "0.1");
		configuration.add("tapestry.thread-pool-enabled", false);
		configuration.add("tapestry.application-version", 0);
//		configuration.add(Trait.SCRIPTACULOUS, false); 
		// configuration.add(IOCSymbols.THREAD_POOL_ENABLED, false);
		// turn off redirect after post
		// c.f.
		// http://tapestry.1045711.n5.nabble.com/Implication-of-client-side-redirect-td2429849.html
		// config.add(SymbolConstants.SUPPRESS_REDIRECT_FROM_ACTION_REQUESTS,
		// "true");
		// === source: https://github.com/got5/tapestry5-jquery
		// configuration.add(JQuerySymbolConstants.JQUERY_ALIAS,
		// "yourOwnAlias");
		// configuration.add(SymbolConstants.MINIFICATION_ENABLED, "true");
		// //encountered runtime error during startup

		//http://tapestry.1045711.n5.nabble.com/Disabling-HMAC-check-td5718156.html
		// Set a random HMAC key for form signing (not cluster safe) 
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "353t5eferr3453534534534");
        
//        configuration.add(JQuerySymbolConstants.SUPPRESS_PROTOTYPE, "false");

		System.out
				.println("AppModule contributeApplicationDefaults() done. App is ready.");
	}

	/**
	 * Works only from T5.3 beta 9 and onwards (c.f.
	 * https://issues.apache.org/jira/browse/TAP5-1616)
	 * 
	 * @param configuration
	 */
	/*
	 * @Contribute(ServiceOverride.class) public static void
	 * setupApplicationServiceOverrides( MappedConfiguration<Class, Object>
	 * configuration) {
	 * System.out.println("AppModule setupApplicationServiceOverrides() ...");
	 * configuration.add(PeriodicExecutor.class, new PeriodicExecutor() { public
	 * PeriodicJob addJob(Schedule schedule, String name, Runnable job) {
	 * System.out
	 * .println("AppModule createNOOPPeriodicExecutor() returning null.");
	 * return null; } }); }
	 */

	// http://blog.tapestry5.de/index.php/2010/01/04/tapestry-and-jsr-303-bean-validation-api/
	// public static void contributeBeanValidatorSource(
	// OrderedConfiguration<BeanValidatorConfigurer> conf) {
	//
	// BeanValidatorConfigurer configurer = new BeanValidatorConfigurer() {
	// public void configure(javax.validation.Configuration<?> aConf) {
	// aConf.ignoreXmlConfiguration();
	// }
	// };
	// conf.add("MyConfigurer", configurer);
	// }

	public static void contributeDefaultDataTypeAnalyzer(
			MappedConfiguration<Class<?>, String> configuration) {
		// add child support in the model, name part can be anything
//		configuration.add(CdActivityDetails.class, "eoActivityDetails");
//		configuration.add(ParentInput.class, "eoParentInput");
//		configuration.add(Subject.class, "eoSubject");
//		configuration.add(Student.class, "eoStudent");
//		configuration.add(College.class, "eoCollege");
	}

//	@Contribute(HttpServletRequestFilter.class)
	public static void contributeBeanBlockSource(
			Configuration<BeanBlockContribution> configuration) {
		configuration.add(new BeanBlockContribution("eoSubject",
				"eo/ActivitySave", "subject", true));
		configuration.add(new BeanBlockContribution("eoStudent",
				"eo/ActivitySave", "student", true));
		configuration.add(new BeanBlockContribution("eoCollege",
				"eo/ActivitySave", "college", true));
	}

	// === source: http://lombok.demon.co.uk/tapestry5Demo/
	public static void contributeComponentClassResolver(
			Configuration<LibraryMapping> configuration) {
		// Creates a virtual root pacakge for pages,components.
		// configuration.add(new LibraryMapping("lombok", "net.sf.lombok"));
	}

	// public static void contributeRegexAuthorizer(
	// Configuration<String> configuration) {
	// String pattern =
	// "([^/.]+/)*[^/.]+\\.((css)|(js)|(jpg)|(jpeg)|(png)|(gif)|(html)|(xml))$";
	// configuration.add("^org/chenillekit/tapestry/core/" + pattern);
	// }

	// === source: http://tapestryjava.blogspot.com/2009/12/securing-tapestry-pages-with.html
	public static void contributeComponentRequestHandler(
			OrderedConfiguration configuration) {
//		configuration.addInstance("RequiresLogin", RequiresLoginFilter.class);
	}

	// === source: http://apache-tapestry-mailing-list-archives.1045711.n5.nabble.com/T5-2-update-problem-The-resource-path-was-not-within-an-aliased-path-td2803667.html
//	public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String>configuration){ 
//	        configuration.add("icons", "assets/icons"); 
//	}
}
