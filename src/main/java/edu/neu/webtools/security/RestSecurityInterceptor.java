package edu.neu.webtools.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.core.Headers;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.core.interception.PostMatchContainerRequestContext;
import org.springframework.stereotype.Component;

@Provider
@Component
public class RestSecurityInterceptor implements ContainerRequestFilter {

	@Context
	private HttpServletRequest servletRequest;

	public static final String AUTH_HEADER_NAME = "Auth-Token";

	private static final ServerResponse ACCESS_DENIED_MSG = new ServerResponse("Access is denied to this resource", 401,
			new Headers<Object>());
	private static final ServerResponse ACCESS_FORBIDDEN = new ServerResponse("Not authorized to access this resource",
			403, new Headers<Object>());

	private static Logger LOG = Logger.getLogger(RestSecurityInterceptor.class.getName());

	public void filter(ContainerRequestContext context) throws IOException {
		LOG.info("Authenticating new request...");
		PostMatchContainerRequestContext pmContext = (PostMatchContainerRequestContext) context;
		Method method = pmContext.getResourceMethod().getMethod();

		if (method.isAnnotationPresent(DenyAll.class)) {
			LOG.info("Found DenyAll Returning 401");
			context.abortWith(ACCESS_DENIED_MSG);
		} else if (method.isAnnotationPresent(PermitAll.class)) {
			/** Do nothing. All are permitted **/
		} else if (method.isAnnotationPresent(RolesAllowed.class)) {
			String authKey = context.getHeaderString(AUTH_HEADER_NAME);
			if (authKey == null) {
				LOG.info("Null Auth-Token Returning 401");
				context.abortWith(ACCESS_DENIED_MSG);
			} else {
				RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
				Collection<String> allowedRoleSet = Arrays.asList(rolesAnnotation.value());
				if (!validateRole(authKey, allowedRoleSet)) {
					LOG.info("No Matching Roles Returning 403");
					context.abortWith(ACCESS_FORBIDDEN);
				}
			}
		} else {
			LOG.info("Returning 401 by default");
			context.abortWith(ACCESS_DENIED_MSG);
		}
	}

	private boolean validateRole(String authKey, Collection<String> allowedRoleSet) {
		ServletContext servletContext = servletRequest.getServletContext();
		UserSession userSession = (UserSession) servletContext.getAttribute(authKey);
		String role = userSession == null ? null : userSession.getRoleName();
		boolean roleExists = allowedRoleSet.contains(role) || allowedRoleSet.contains("GUEST");
		return roleExists;
	}

}
