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
		// TODO: Complete method
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}

	@Override
	public User deleteUser(String name, String pwd) {
		Log.info("deleteUser : name = " + name + "; pwd = " + pwd);
		// TODO: Complete method
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}

	@Override
	public List<User> searchUsers(String name, String pwd, String pattern) {
		Log.info("searchUsers : name = " + name + "; pwd = " + pwd + "; pattern = " + pattern);
		// TODO: Complete method
		throw new WebApplicationException(Status.NOT_IMPLEMENTED);
	}
}
