package edu.neu.webtools.ctrl;

import javax.annotation.security.PermitAll;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import edu.neu.webtools.domain.AuthRequest;
import edu.neu.webtools.domain.AuthResponse;
import edu.neu.webtools.domain.RegistrationRequest;
import edu.neu.webtools.domain.StatusObject;
import edu.neu.webtools.security.UserSession;
import edu.neu.webtools.service.SecurityService;
import edu.neu.webtools.util.CommonUtils;

@Controller
@Path("/auth")
public class RestAuthController {
	@Value("${security.hashing.md5.salt}")
	private String salt;

	@Autowired
	private SecurityService securityService;

	@Context
	private HttpServletRequest servletRequest;

	@POST
	@Path("/getToken")
	@Consumes("application/json")
	@Produces("application/json")
	@PermitAll
	public AuthResponse authenticate(AuthRequest request) {
		String username = request.getUsername();
		String password = request.getPassword();
		UserSession userSession = null;
		String authToken = null;
		AuthResponse authResponse = new AuthResponse(false, null, null);
		if (StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password)) {
			String passwordHash = CommonUtils.md5Hash(password);
			userSession = securityService.authenticate(username, passwordHash);
			if (userSession != null) {
				String usernameKey = "authtoken_for_user_" + username;
				authToken = "authtoken_" + CommonUtils.randomString();
				ServletContext servletContext = servletRequest.getServletContext();

				/** Remove any existing key **/
				String existingAuthKey = (String) servletContext.getAttribute(usernameKey);
				if (existingAuthKey != null) {
					servletContext.removeAttribute(usernameKey);
					servletContext.removeAttribute(existingAuthKey);
				}

				/** Add the new key **/
				servletContext.setAttribute(usernameKey, authToken);
				servletContext.setAttribute(authToken, userSession);
				authResponse = new AuthResponse(true, authToken, userSession);
			}
		}
		return authResponse;
	}

	@POST
	@Path("/register")
	@Consumes("application/json")
	@Produces("application/json")
	@PermitAll
	public StatusObject register(RegistrationRequest request) {
		StatusObject status = securityService.register(request);
		return status;
	}

}
