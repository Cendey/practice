package edu.mit.lib.rest.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.utils.LoggingResponseFilter</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
public class LoggingResponseFilter implements ContainerResponseFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoggingResponseFilter.class);

    @Override
    public void filter(
        ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)
        throws IOException {
        String method = containerRequestContext.getMethod();

        logger.debug("Requesting " + method + " for path " + containerRequestContext.getUriInfo().getPath());
        Object entity = containerResponseContext.getEntity();
        if (entity != null) {
            logger.debug("Response " + new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(entity));
        }

    }
}
