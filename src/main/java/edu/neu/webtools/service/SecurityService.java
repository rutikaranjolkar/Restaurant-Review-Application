package edu.neu.webtools.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import edu.neu.webtools.dao.SecurityDao;
import edu.neu.webtools.domain.RegistrationRequest;
import edu.neu.webtools.domain.StatusObject;
import edu.neu.webtools.domain.User;
import edu.neu.webtools.security.UserSession;
import edu.neu.webtools.util.CommonUtils;
import edu.neu.webtools.util.EmailUtil;

@Service
public class SecurityService {
	@Autowired
	private SecurityDao securityDao;

	@Value("${security.hashing.md5.salt}")
	private String salt;

	public UserSession authenticate(String username, String passwordHash) {
		UserSession session = null;
		User user = securityDao.authenticate(username, passwordHash);
		if (user != null) {
			long userId = user.getId();
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String roleName = user.getRole();
			session = new UserSession(userId, username, firstName, lastName, roleName);
		}
		return session;
	}

	public StatusObject register(RegistrationRequest request) {
		StatusObject status;
		String username = request.getUsername();
		User existingUser = securityDao.getByUsername(username);
		String roleName = request.getRole();
		if (existingUser != null) {
			status = new StatusObject(false, "Username already in use.");
		} else if (StringUtils.isEmpty(request.getUsername())) {
			status = new StatusObject(false, "Please enter a username.");
		} else if (StringUtils.isEmpty(request.getPassword())) {
			status = new StatusObject(false, "Please enter a password.");
		} else if (StringUtils.isEmpty(request.getRole())) {
			status = new StatusObject(false, "Please select a role.");
		} else if (StringUtils.isEmpty(request.getFirstName())) {
			status = new StatusObject(false, "Please enter your first name.");
		} else if (StringUtils.isEmpty(request.getLastName())) {
			status = new StatusObject(false, "Please enter your last name.");
		} else if (StringUtils.isEmpty(request.getEmailAddress())) {
			status = new StatusObject(false, "Please enter an email address.");
		} else if (!EmailUtil.validate(request.getEmailAddress())) {
			status = new StatusObject(false, "Please enter a valid email address.");
		} else if (StringUtils.isEmpty(roleName)) {
			status = new StatusObject(false, "Please enter a valid role name.");
		} else {
			String password = request.getPassword();
			String hashedPassword = CommonUtils.md5Hash(password);
			User user = new User();
			user.setUsername(username);
			user.setFirstName(request.getFirstName());
			user.setLastName(request.getLastName());
			user.setPassword(hashedPassword);
			user.setRole(roleName);
			securityDao.saveNewUser(user);
			status = new StatusObject(true, null);
		}
		return status;
	}

	public User getByUsername(String username) {
		return securityDao.getByUsername(username);
	}
}
