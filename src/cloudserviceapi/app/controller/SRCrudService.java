package cloudserviceapi.app.controller;

import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.appspot.cloudserviceapi.data.EMF;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryDAO;
import com.appspot.cloudserviceapi.sci.dao.ServiceRegistryRepository;
import com.appspot.cloudserviceapi.sci.dao.SortedServiceRegistryImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Contact;
import io.swagger.annotations.Info;
import io.swagger.annotations.License;
import io.swagger.annotations.SwaggerDefinition;
import tapp.model.ServiceRegistry;

@Controller
@RequestMapping(value = "/jwt/fusr", 
consumes={"text/plain", "application/json", "*/*"},
produces={"application/json", "*/*"},
method = {RequestMethod.GET, RequestMethod.POST})
@Secured("ROLE_USER")
@SwaggerDefinition(
        info = @Info(
                title = "ServiceManager",	//"NOT USED" c.f. web.xml's swagger.api.title
                version = "V0.0.1",		//"NOT USED" c.f. web.xml's api.version
                description = "CRUD Servlet",
                termsOfService = "http://swagger.io/terms/",
                contact = @Contact(name = "Adcoolguy", email = "apiteam@swagger.io", url = "http://swagger.io"),
                license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ),						
        consumes = {"application/json", "application/x-www-form-urlencoded"},						
        produces = {"application/json"}
        ,
//        host = "http://chudoon3t.appspot.com" /*AppEngine.getHostName()*/,	//"NOT USED" c.f. web.xml swagger.api.basepath (needs to be prefixed with http(s)!!!)
        schemes = {SwaggerDefinition.Scheme.HTTPS
        	, SwaggerDefinition.Scheme.HTTP
        	}
)
@io.swagger.annotations.Api(value = "api/jwt/fusr", tags = "sr")
public class SRCrudService {
//	@Autowired
    ServiceRegistryRepository repository;
    SortedServiceRegistryImpl sortedRepository;
    EntityManager entityManager;

    public SRCrudService() {
        entityManager = EMF.get().createEntityManager();
    	JpaRepositoryFactory jpaRepositoryFactory = new JpaRepositoryFactory(entityManager);

    	repository = jpaRepositoryFactory.getRepository(ServiceRegistryRepository.class);

    	try {
			sortedRepository = new SortedServiceRegistryImpl(repository, entityManager);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@ApiOperation(httpMethod = "GET", value = "Resource to get all Items", nickname="all")
    @ApiImplicitParams({
	    	@ApiImplicitParam(name = "pagaSize", defaultValue = "6", value = "Max item per page", required = false, dataType = "integer", paramType = "query"),
	    	@ApiImplicitParam(name = "pageNumber", defaultValue = "0", value = "Current page number (start from 0)", required = false, dataType = "integer", paramType = "query")
    	}
    )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success")
		}
	)
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody Page<ServiceRegistry> getAllSRs(Pageable pageable) {
//        return repository.findAll(pageable);
        return sortedRepository.findAll(pageable);
    }

	@ApiOperation(httpMethod = "GET", value = "Resource to get an Item" , nickname="{id}")
    @ApiImplicitParams({
	    	@ApiImplicitParam(name = "id", value = "Item unique id", required = true, dataType = "integer", paramType = "path")
    	}
    )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success")
		}
	)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody ServiceRegistry getSR(@PathVariable("id") Long id) {
        return repository.findOne(id);
    }

	@ApiOperation(httpMethod = "POST", value = "Resource to create/change an item" , nickname="saveSR")
	@ApiImplicitParams({
	    	@ApiImplicitParam(name = "sr", defaultValue = "", value = "Service Registry JSON object", required = true, dataType = "tapp.model.ServiceRegistry", paramType = "body")
		}
	)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "Failure")
		}
	)
    @RequestMapping(value= "/saveSR", method = RequestMethod.POST, consumes = {"application/json;charset=UTF-8"}, produces = {"application/json;charset=UTF-8"})
	@Secured("ROLE_ADMIN")
    public @ResponseBody ResponseEntity<ServiceRegistry> createOrUpdate(@RequestBody ServiceRegistry sr){
        try{
            if (sr != null){
            	//repository.save(sr);
            	if(sr.getLastUpdated() == null) {
            		sr.setLastUpdated(new Date());
            	}
        		(new ServiceRegistryDAO()).save(sr);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<ServiceRegistry>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<ServiceRegistry>(HttpStatus.OK);
    }

	@ApiOperation(httpMethod = "POST", value = "Resource to delete an Item" , nickname="delete")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "Item unique id", required = true, dataType = "string", paramType = "body")	//SR2#2 datatype has to be a string due to Apimatic (https://mail.google.com/mail/u/0/#inbox/15252575a5c9dd9f)
    	}
    )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "Failure")
		}
	)
    @RequestMapping(value= "/delete", method = RequestMethod.POST, produces = {"application/json"})
	@Secured("ROLE_ADMIN")
    public @ResponseBody ResponseEntity<ServiceRegistry> delete(@RequestBody Long id) {
        System.out.println("REST request to delete ServiceRegistry: " + id);
        try{
            if (id > -1){
            	//caused: javax.persistence.PersistenceException: Problem with query <SELECT count(x) FROM ServiceRegistry x WHERE x.id = :id AND 1 = 1>: Unexpected expression type while parsing query: org.datanucleus.query.expression.Literal
                //repository.delete(id);
        		(new ServiceRegistryDAO()).remove(id);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<ServiceRegistry>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<ServiceRegistry>(HttpStatus.OK);
    }
	
	@ApiOperation(httpMethod = "GET", value = "Resource to check for existence of an Item" , nickname="existsSR/{service}")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "service", value = "Item unique service", required = true, dataType = "string", paramType = "path")
    	}
    )
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success", response = ServiceRegistry.class),
			@ApiResponse(code = 401, message = "Failure")
		}
	)
    @RequestMapping(value= "/existsSR/{service}", method = RequestMethod.GET, produces = {"application/json"})
    public @ResponseBody ServiceRegistry existsSR(@PathVariable("service") String service) {
        System.out.println("REST request to check a ServiceRegistry: " + service);
        ServiceRegistry ret = null;
        try{
            if (!StringUtils.isEmpty(service)) {
        		ret = (new ServiceRegistryDAO()).findServiceRegistryByService(service);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return ret;
    }
}