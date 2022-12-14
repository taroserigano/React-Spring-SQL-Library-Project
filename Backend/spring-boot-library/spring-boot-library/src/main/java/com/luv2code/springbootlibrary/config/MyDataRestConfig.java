package com.luv2code.springbootlibrary.config;

import com.luv2code.springbootlibrary.entity.Book;
import com.luv2code.springbootlibrary.entity.Message;
import com.luv2code.springbootlibrary.entity.Review;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private String theAllowedOrigins = "http://localhost:3000";  // this IP add will be allowed to access 

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config,
                                                     CorsRegistry cors) {
        // HTTP reqs to be blocked 
        HttpMethod[] theUnsupportedActions = {          
                HttpMethod.POST,
                HttpMethod.PATCH,
                HttpMethod.DELETE,
                HttpMethod.PUT};
        
        // specify primary IDs of these classes 
        config.exposeIdsFor(Book.class);
        config.exposeIdsFor(Review.class);
        config.exposeIdsFor(Message.class);
        
        // disable them 
        disableHttpMethods(Book.class, config, theUnsupportedActions);
        disableHttpMethods(Review.class, config, theUnsupportedActions);
        disableHttpMethods(Message.class, config, theUnsupportedActions);

        /* Configure CORS Mapping */
        cors.addMapping(config.getBasePath() + "/**")
                .allowedOrigins(theAllowedOrigins); // <- allow this IP add to access 
    }

    private void disableHttpMethods(Class theClass,  // class to disable 
                                    RepositoryRestConfiguration config,  // set the IDs 
                                    HttpMethod[] theUnsupportedActions)   // blocking HTP reqs 
    {
        
        config.getExposureConfiguration()        
                .forDomainType(theClass)        // specify class 
                .withItemExposure((metdata, httpMethods) ->        // single item 
                        httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) ->  // collection items 
                        httpMethods.disable(theUnsupportedActions));
    }
}
