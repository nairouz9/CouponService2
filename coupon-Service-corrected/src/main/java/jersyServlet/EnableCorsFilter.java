package jersyServlet;


import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

/*
 * CORS - Cross-origin resource sharing
 * 
 * From Wikipedia, the free encyclopedia
 * 
 * Cross-origin resource sharing (CORS) is a mechanism that allows restricted resources on a web page to be requested 
 * from another domain outside the domain from which the first resource was served.
 * 
 * A web page may freely embed cross-origin images, stylesheets, scripts, iframes, and videos.
 * 
 * Certain "cross-domain" requests, notably Ajax requests, are forbidden by default by the same-origin security policy.
 * 
 * CORS defines a way in which a browser and server can interact to determine whether or not it is safe to allow the 
 * cross-origin request. It allows for more freedom and functionality than purely same-origin requests, but is more secure 
 * than simply allowing all cross-origin requests.
 * */

/**
 * This class is a filter for all request turned at this web module. It adds the
 * needed HTTP response headers to enable cors (Cross-origin resource sharing)
 * so AJAX requests coming from angular CLI server are not blocked.
 */
@Provider
public class EnableCorsFilter implements ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) throws IOException {
		System.out.println("niroz cors filter");
		response.getHeaders().add("Access-Control-Allow-Origin", "http://localhost:4200"); // or "*"
		response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.getHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
	}
}
