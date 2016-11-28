package edu.mit.lib.rest.services;

import edu.mit.lib.rest.utils.CustomizedResponseFilter;
import edu.mit.lib.rest.utils.LoggingResponseFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.services.EndpointRegister</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
public class EndpointRegister extends ResourceConfig {

    public EndpointRegister() {
        register(RequestContextFilter.class);
        register(BookRestService.class);
        register(JacksonFeature.class);
        register(LoggingResponseFilter.class);
        register(CustomizedResponseFilter.class);
    }
}
