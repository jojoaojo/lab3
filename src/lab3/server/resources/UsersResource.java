package lab3.server.resources;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.exception.ConstraintViolationException;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import lab3.api.User;
import lab3.api.rest.RestUsers;
import lab3.server.persistence.Hibernate;

@Singleton
public class UsersResource implements RestUsers {

	private final Hibernate hibernate;
	
	private static Logger Log = Logger.getLogger(UsersResource.class.getName());

	public UsersResource() {
		hibernate = Hibernate.getInstance();
	}

	@Override
	public String postUser(User user) {
		Log.info("postUser : " + user);

		// Check if user data is valid
		if (user.getName() == null || user.getPwd() == null || user.getDisplayName() == null || user.getDomain() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		try {
			hibernate.persist(user);
		} catch (ConstraintViolationException e) {
			e.printStackTrace(); //This exception is due to the user already existing...
			Log.info("User already exists.");
			throw new WebApplicationException(Status.CONFLICT);
		} catch( Exception x ) {
			x.printStackTrace(); // Un-expected exception. Signal internal server error.
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return user.getName() + "@" + user.getDomain();
	}

	@Override
	public User getUser(String name, String pwd) {
		Log.info("getUser : name = " + name + "; pwd = " + pwd);

		// Check if parameters are valid
		if (name == null || pwd == null) {
			Log.info("Name or password null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		User user = null;
		try {
			user = hibernate.get(User.class, name);
		} catch( Exception x ) {
			x.printStackTrace(); // Un-expected exception. Signal internal server error.
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		// Check if user exists and password matches
		if (user == null || !user.getPwd().equals(pwd)) {
			Log.info("User does not exist or password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		return user;
	}

	@Override
	public User updateUser(String name, String pwd, User info) {
		Log.info("updateUser : name = " + name + "; pwd = " + pwd + " ; info = " + info);

		// Check if parameters are valid
		if (name == null || pwd == null || info == null) {
			Log.info("Name, password or info null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		User user = null;
		try {
			user = hibernate.get(User.class, name);
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		// Check if user exists and password matches
		if (user == null || !user.getPwd().equals(pwd)) {
			Log.info("User does not exist or password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		// Update fields (only if not null in info)
		if (info.getPwd() != null)
			user.setPwd(info.getPwd());
		if (info.getDisplayName() != null)
			user.setDisplayName(info.getDisplayName());
		if (info.getDomain() != null)
			user.setDomain(info.getDomain());
		if (info.getPhoneNumbers() != null)
			user.setPhoneNumbers(info.getPhoneNumbers());

		try {
			hibernate.update(user);
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return user;
	}

	@Override
	public User deleteUser(String name, String pwd) {
		Log.info("deleteUser : name = " + name + "; pwd = " + pwd);

		// Check if parameters are valid
		if (name == null || pwd == null) {
			Log.info("Name or password null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		User user = null;
		try {
			user = hibernate.get(User.class, name);
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		// Check if user exists and password matches
		if (user == null || !user.getPwd().equals(pwd)) {
			Log.info("User does not exist or password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		try {
			hibernate.delete(user);
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return user;
	}

	@Override
	public List<User> searchUsers(String name, String pwd, String pattern) {
		Log.info("searchUsers : name = " + name + "; pwd = " + pwd + "; pattern = " + pattern);

		// Check if parameters are valid
		if (name == null || pwd == null) {
			Log.info("Name or password null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}

		// Verify that the user performing the search exists and has correct password
		User requestingUser = null;
		try {
			requestingUser = hibernate.get(User.class, name);
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		if (requestingUser == null || !requestingUser.getPwd().equals(pwd)) {
			Log.info("User does not exist or password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}

		// Perform search using JPQL (case-insensitive substring match)
		List<User> results = null;
		try {
			// If pattern is null or empty, return all users
			String searchPattern = (pattern == null || pattern.isEmpty()) ? "" : pattern.toLowerCase();
			String jpqlQuery = "SELECT u FROM User u WHERE LOWER(u.name) LIKE '%" + searchPattern + "%'";
			results = hibernate.jpql(jpqlQuery, User.class);
			
			// Set passwords to empty string as per specification
			for (User u : results) {
				u.setPwd("");
			}
		} catch( Exception x ) {
			x.printStackTrace();
			throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
		}

		return results;
	}
}
