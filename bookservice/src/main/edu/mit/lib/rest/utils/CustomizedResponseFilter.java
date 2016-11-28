package edu.mit.lib.rest.utils;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;

/**
 * <p>Title: MIT Library Practice</p>
 * <p>Description: edu.mit.lib.rest.utils.CustomizedResponseFilter</p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: MIT Labs Co., Inc</p>
 *
 * @author <chao.deng@kewill.com>
 * @version 1.0
 * @since 8/5/2016
 */
public class CustomizedResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(
        ContainerRequestContext containerRequestContext, ContainerResponseContext containerResponseContext)
        throws IOException {

        MultivaluedMap<String, Object> headers = containerResponseContext.getHeaders();

        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Codingpedia");
    }
}
