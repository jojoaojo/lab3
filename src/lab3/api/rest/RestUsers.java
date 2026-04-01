package lab3.api.rest;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import lab3.api.User;

@Path(RestUsers.PATH)
public interface RestUsers {

	final static String PATH = "/users";
	final static String QUERY = "query";
	final static String NAME = "name";
	final static String PWD = "pwd";

	/**
	 * Creates a new user in the local domain.
	 *
	 * The operation succeeds when the name is not already in use; or, when the same user
	 * is already present in the system.
	 *
	 * @param user - User to be created
	 * @return 200 and the user address of the user: name@domain.
	 * 403 if the domain in the user does not match the domain of the server;
	 * 409 if the name is already associated with a different user (i.e., password or display name differ);
	 * 400 if the parameters are invalid (eg., null parameters, or a malformed user).
	 */
	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	String postUser(User user);

	/**
	 * Obtains the information on the user identified by name.
	 *
	 * @param name - the name of the user
	 * @param pwd - password of the user
	 * @return 200 and the user object, if the name exists and pwd matches the existing password.
	 * 403 if the password is incorrect or the user does not exist;
	 * 400 if the parameters are invalid (eg., null parameters).
	 */
	@GET
	@Path("/{" + NAME +"}")
	@Produces(MediaType.APPLICATION_JSON)
	User getUser(@PathParam(NAME) String name, @QueryParam(PWD) String pwd);

	/**
	 * Modifies the information of a user. Values of null in any field of the user will be
	 * considered as if the the fields is not to be modified (the name cannot be modified).
	 *
	 * @param name - the name of the user
	 * @param pwd - password of the user
	 * @param info - Updated information
	 * @return 200 and the updated user object, if the name exists and pwd matches the existing password.
	 * 403 if the password is incorrect or the user does not exist;
	 * 400 if the parameters are invalid (eg., null parameters).
	 */
	@PUT
	@Path("/{" + NAME +"}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	User updateUser(@PathParam(NAME) String name, @QueryParam(PWD) String pwd, User info);

	/**
	 * Deletes the user identified by name.
	 * All resources associated with this user in other services should also be eventually released, without waiting for completion.
	 *
	 * @param name - the name of the user
	 * @param pwd - password of the user
	 * @return 200 and the deleted user object, if the name exists and pwd matches the existing password.
	 * 403 if the password is incorrect or the user does not exist;
	 * 400 if the parameters are invalid (eg., null parameters).
	 */
	@DELETE
	@Path("/{" + NAME + "}")
	@Produces(MediaType.APPLICATION_JSON)
	User deleteUser(@PathParam(NAME) String name, @QueryParam(PWD) String pwd);

	/**
	 * Returns the list of users for which the pattern is a substring of the name, case-insensitive.
	 * The password of the users returned by the query must be set to the empty string "".
	 * Only existing users can perform a search.
	 *
	 * @param name - the name of the user performing the search
	 * @param pwd - password of the user
	 * @param pattern - substring to search
	 * @return 200 and the list of hits, may be empty.
	 * 403 if the password is incorrect or the user does not exist;
	 * 400 if the parameters are invalid (eg., null parameters).
	 */
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	List<User> searchUsers(@QueryParam(NAME) String name, @QueryParam(PWD) String pwd, @QueryParam(QUERY) String pattern);
}
